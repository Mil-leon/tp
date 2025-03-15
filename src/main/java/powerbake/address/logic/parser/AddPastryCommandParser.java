package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PRICE;

import java.util.stream.Stream;

import powerbake.address.logic.commands.AddPastryCommand;
import powerbake.address.logic.parser.exceptions.ParseException;
import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;


/**
 * Parses input arguments and creates a new AddPastryCommand object
 */
public class AddPastryCommandParser implements Parser<AddPastryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPastryCommand
     * and returns an AddPastryCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPastryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASTRY, PREFIX_PRICE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASTRY, PREFIX_PRICE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPastryCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PASTRY, PREFIX_PRICE);
        Name pastryName = ParserUtil.parsePastryName(argMultimap.getValue(PREFIX_PASTRY).get());
        Price price = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_PRICE).get());
        Pastry pastry = new Pastry(pastryName, price);
        return new AddPastryCommand(pastry);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
