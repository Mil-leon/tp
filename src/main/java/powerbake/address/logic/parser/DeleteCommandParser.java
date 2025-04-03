package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.commands.DeleteCommand;
import powerbake.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link DeleteCommand} object.
 *
 * A {@code DeleteCommandParser} is responsible for interpreting user input
 * for the "delete" command, identifying the entity type and index from the
 * given arguments, and creating a valid {@link DeleteCommand} instance for execution.
 *
 * Example usage:
 *    DeleteCommandParser parser = new DeleteCommandParser();
 *    DeleteCommand command = parser.parse("client 1"); // Outputs a DeleteCommand for deleting client at index 1
 *
 * If the input does not meet the required format (e.g., missing entity type or invalid index),
 * a {@link ParseException} is thrown.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@link DeleteCommand}
     * and returns a new {@link DeleteCommand} object for execution.
     *
     * The input must contain a valid entity type ("client", "pastry" or "order") followed by an integer index.
     * For example:
     * <pre>
     *     delete client 1
     *     delete pastry 2
     *     delete order 3
     * </pre>
     *
     * @param args The user input string to parse, expected to contain the entity type
     *             and index (e.g., "client", "pastry" or "order").
     * @return A {@link DeleteCommand} object based on the parsed arguments.
     * @throws ParseException If the user input does not conform to the expected format,
     *                        or if the entity type or index is invalid.
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] parts = trimmedArgs.split(" ", 2); // Split into entity type and index

        if (parts.length != 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String entityType = parts[0].toLowerCase(); // Normalize the entity type
        String indexString = parts[1]; // The second part is the index

        try {
            Index index = ParserUtil.parseIndex(indexString);

            return new DeleteCommand(entityType, index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, pe.getMessage())
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        }
    }
}
