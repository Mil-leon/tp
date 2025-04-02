package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT_NOSPACE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ORDER_NOSPACE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY_NOSPACE;

import powerbake.address.logic.commands.AddCommand;
import powerbake.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLIENT_NOSPACE, PREFIX_PASTRY_NOSPACE, PREFIX_ORDER_NOSPACE);

        boolean isClient = argMultimap.getValue(PREFIX_CLIENT_NOSPACE).isPresent();
        boolean isPastry = argMultimap.getValue(PREFIX_PASTRY_NOSPACE).isPresent();
        boolean isOrder = argMultimap.getValue(PREFIX_ORDER_NOSPACE).isPresent();
        if (!(isClient ^ isPastry ^ isOrder)
                || !argMultimap.getPreamble().isEmpty()
                || (isClient & isPastry & isOrder)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (isClient) {
            return new AddClientCommandParser().parse(args);
        } else if (isPastry) {
            return new AddPastryCommandParser().parse(args);
        } else {
            return new AddOrderCommandParser().parse(args);
        }
    }
}
