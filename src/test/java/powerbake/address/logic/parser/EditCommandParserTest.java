package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_NAME;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_TAG;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
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
        assertParseFailure(parser, "pastry 1", EditCommand.MESSAGE_NOT_EDITED);

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
        assertParseFailure(parser, "client 1 -n " + "James&", Name.MESSAGE_CONSTRAINTS);

        // Invalid phone
        assertParseFailure(parser, "client 1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // Invalid email
        assertParseFailure(parser, "client 1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);

        // Invalid address
        assertParseFailure(parser, "client 1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);

        // Invalid tag
        assertParseFailure(parser, "client 1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // Invalid phone followed by a valid email
        assertParseFailure(parser, "client 1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // Multiple invalid fields, only the first invalid field should be reported
        assertParseFailure(parser,
                "client 1 -n " + INVALID_NAME_DESC + INVALID_EMAIL_DESC + PHONE_DESC_AMY + ADDRESS_DESC_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        // Arrange: All details specified for editing a client
        String userInput = "client 1"
                + " " + PREFIX_NAME + "Jane Doe"
                + " " + PREFIX_PHONE + "98765432"
                + " " + PREFIX_EMAIL + "jane.doe@example.com"
                + " " + PREFIX_ADDRESS + "456, Some Street, #12-34"
                + " " + PREFIX_TAG + "colleague";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Jane Doe")
                .withPhone("98765432")
                .withEmail("jane.doe@example.com")
                .withAddress("456, Some Street, #12-34")
                .withTags("colleague")
                .build();

        EditCommand expectedCommand = new EditCommand("client", Index.fromOneBased(1), descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }


    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = "client " + targetIndex.getOneBased() + " " + PHONE_DESC_BOB + " " + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY)
                .build();

        EditCommand expectedCommand = new EditCommand("client", targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = "client " + targetIndex.getOneBased()
                + " " + PREFIX_NAME + "Jane Doe";
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Jane Doe").build();
        EditCommand expectedCommand = new EditCommand("client", targetIndex, descriptor, true);
        assertParseSuccess(parser, userInput, expectedCommand);


        // phone
        userInput = "client " + targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand("client", targetIndex, descriptor, true);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = "client " + targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand("client", targetIndex, descriptor, true);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = "client " + targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand("client", targetIndex, descriptor, true);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = "client " + targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand("client", targetIndex, descriptor, true);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // valid followed by invalid (only duplicate `-p`)
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = "client " + targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid (only duplicate `-p`)
        userInput = "client " + targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
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
        String userInput = "client " + targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand("client", targetIndex, descriptor, true);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
