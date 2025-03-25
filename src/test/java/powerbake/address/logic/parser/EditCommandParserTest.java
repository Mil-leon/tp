package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_NAME_JAMES;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.NAME_EDIT_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.NAME_EDIT_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.PRICE_DESC_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PRICE_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_NAME;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_TAG;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static powerbake.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.EditCommand;
import powerbake.address.logic.commands.EditCommand.EditPersonDescriptor;
import powerbake.address.model.person.Address;
import powerbake.address.model.person.Email;
import powerbake.address.model.person.Name;
import powerbake.address.model.person.Phone;
import powerbake.address.model.tag.Tag;
import powerbake.address.testutil.EditPastryDescriptorBuilder;
import powerbake.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, PREFIX_PASTRY + "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // Invalid name
        assertParseFailure(parser, PREFIX_CLIENT + "1 " + PREFIX_NAME
                + INVALID_NAME_JAMES, Name.MESSAGE_CONSTRAINTS);

        // Invalid phone
        assertParseFailure(parser, PREFIX_CLIENT + "1 " + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // Invalid email
        assertParseFailure(parser, PREFIX_CLIENT + "1 " + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);

        // Invalid address
        assertParseFailure(parser, PREFIX_CLIENT + "1 " + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);

        // Invalid tag
        assertParseFailure(parser, PREFIX_CLIENT + "1 " + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // Invalid phone followed by a valid email
        assertParseFailure(parser, PREFIX_CLIENT + "1 " + INVALID_PHONE_DESC + EMAIL_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // Multiple invalid fields, only the first invalid field should be reported
        assertParseFailure(parser,
                PREFIX_CLIENT + "1 " + PREFIX_NAME + INVALID_NAME_DESC + INVALID_EMAIL_DESC
                        + PHONE_DESC_AMY + ADDRESS_DESC_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        // Arrange: All details specified for editing a client
        String userInput = PREFIX_CLIENT + "1" + NAME_EDIT_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND)
                .build();

        EditCommand expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1), descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }


    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = PREFIX_CLIENT.toString()
                + targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY)
                .build();

        EditCommand expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased()
                + NAME_EDIT_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);


        // phone
        userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // valid followed by invalid (only duplicate `-p`)
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid (only duplicate `-p`)
        userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // Detects duplicates for phone, email, and address
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // Detects duplicates for invalid fields: phone, email, and address
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = PREFIX_CLIENT.toString() + targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_editPastry_success() {
        // Test editing the pastry name
        Index targetIndex = INDEX_FIRST_PASTRY;
        String userInput = PREFIX_PASTRY.toString() + targetIndex.getOneBased() + NAME_EDIT_CHOCOLATECROISSANT;

        // Create descriptor with new name
        EditCommand.EditPastryDescriptor descriptor = new EditPastryDescriptorBuilder()
                .withName(VALID_NAME_CHOCOLATECROISSANT).build();

        EditCommand expectedCommand = new EditCommand(PREFIX_PASTRY.toString().trim(),
                targetIndex, descriptor, false);

        assertParseSuccess(parser, userInput, expectedCommand);

        // Test editing only the price
        userInput = PREFIX_PASTRY.toString() + targetIndex.getOneBased() + PRICE_DESC_CHOCOLATECROISSANT;

        // Create descriptor with new price
        descriptor = new EditPastryDescriptorBuilder()
                .withPrice(VALID_PRICE_CHOCOLATECROISSANT).build();

        expectedCommand = new EditCommand(PREFIX_PASTRY.toString().trim(),
                targetIndex, descriptor, false);

        assertParseSuccess(parser, userInput, expectedCommand);

        // Test editing both name and price
        userInput = PREFIX_PASTRY.toString() + targetIndex.getOneBased()
                + NAME_EDIT_CHOCOLATECROISSANT + PRICE_DESC_CHOCOLATECROISSANT;

        // Create descriptor with new name and price
        descriptor = new EditPastryDescriptorBuilder()
                .withName(VALID_NAME_CHOCOLATECROISSANT)
                .withPrice(VALID_PRICE_CHOCOLATECROISSANT).build();

        expectedCommand = new EditCommand(PREFIX_PASTRY.toString().trim(),
                targetIndex, descriptor, false);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
