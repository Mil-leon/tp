package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CliSyntax.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.commands.AddOrderCommand;
import powerbake.address.logic.commands.DeleteCommand;
import powerbake.address.logic.parser.exceptions.ParseException;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.person.Name;
import powerbake.address.model.person.Person;

/**
 * Parses input arguments and creates a new AddOrderCommand object
 */
public class AddOrderCommandParser implements Parser<AddOrderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddOrderCommand
     * and returns an AddOrderCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddOrderCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ORDER, PREFIX_PASTRY_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_ORDER)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddOrderCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ORDER);

        // check if client index is valid here
        try {
            Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_ORDER).get());
            ArrayList<ArrayList<String>> unformattedOrderList = ParserUtil.parseUnformattedOrders(
                    argMultimap.getAllValues(PREFIX_PASTRY_NAME));
            return new AddOrderCommand(index, unformattedOrderList);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddOrderCommand.MESSAGE_USAGE), e);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }



}
