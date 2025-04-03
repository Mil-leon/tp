package powerbake.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_NAME;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.commands.EditCommand;
import powerbake.address.logic.commands.EditCommand.EditOrderDescriptor;
import powerbake.address.logic.commands.EditCommand.EditPastryDescriptor;
import powerbake.address.logic.commands.EditCommand.EditPersonDescriptor;
import powerbake.address.logic.parser.exceptions.ParseException;
import powerbake.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_PRICE, PREFIX_TAG, PREFIX_NAME, PREFIX_STATUS
        );

        String[] preambleTokens = argMultimap.getPreamble().split("\\s+");
        if (preambleTokens.length < 2 || preambleTokens.length > 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        String entityType = preambleTokens[0].trim().toLowerCase();
        if (!(entityType.equals("client") || entityType.equals("pastry") || entityType.equals("order"))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preambleTokens[1].trim());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_NAME,
                PREFIX_PRICE, PREFIX_TAG, PREFIX_STATUS);

        if (entityType.equals("client")) {
            return parseEditClientCommand(argMultimap, index);
        } else if (entityType.equals("pastry")) {
            return parseEditPastryCommand(argMultimap, index);
        } else {
            return parseEditOrderCommand(argMultimap, index);
        }
    }

    /**
     * Parses arguments to create an EditCommand for a client.
     */
    private EditCommand parseEditClientCommand(ArgumentMultimap argMultimap, Index index) throws ParseException {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED_CLIENT);
        }

        return new EditCommand("client", index, editPersonDescriptor, true);
    }

    /**
     * Parses arguments to create an EditCommand for a pastry.
     */
    private EditCommand parseEditPastryCommand(ArgumentMultimap argMultimap, Index index) throws ParseException {
        EditPastryDescriptor editPastryDescriptor = new EditPastryDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPastryDescriptor.setName(ParserUtil.parsePastryName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PRICE).isPresent()) {
            editPastryDescriptor.setPrice(ParserUtil.parsePrice(argMultimap.getValue(PREFIX_PRICE).get()));
        }

        if (!editPastryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED_PASTRY);
        }

        return new EditCommand("pastry", index, editPastryDescriptor, false);
    }

    /**
     * Parses arguments to create an EditCommand for an order.
     */
    private EditCommand parseEditOrderCommand(ArgumentMultimap argMultimap, Index index) throws ParseException {
        EditOrderDescriptor editOrderDescriptor = new EditOrderDescriptor();

        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            editOrderDescriptor.setStatus(ParserUtil.parseOrderStatus(argMultimap.getValue(PREFIX_STATUS).get()));
        }

        if (!editOrderDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED_ORDER);
        }

        return new EditCommand("order", index, editOrderDescriptor, false);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contains only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}
