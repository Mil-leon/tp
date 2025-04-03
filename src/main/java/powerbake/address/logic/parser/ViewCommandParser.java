package powerbake.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.commands.ViewCommand;
import powerbake.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewCommand object
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     */
    public ViewCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        String[] parts = trimmedArgs.split(" "); // Split into entity type and index

        if (parts.length > 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        boolean isClient = parts[0].equalsIgnoreCase("client");
        boolean isPastry = parts[0].equalsIgnoreCase("pastry");
        boolean isOrder = parts[0].equalsIgnoreCase("order");
        Index index = null;

        if ((isClient || isPastry) && parts.length == 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        // Check if the command is to view a specific order
        if (isOrder && parts.length == 2) {
            try {
                index = ParserUtil.parseIndex(parts[1]);
                parts[0] = parts[0] + "num";
            } catch (ParseException e) {
                throw new ParseException(String.format(MESSAGE_INVALID_ORDER_DISPLAYED_INDEX, e.getMessage()));
            }
        }

        switch (parts[0].toLowerCase()) {
        case "client":
            return new ViewCommand("client");
        case "pastry":
            return new ViewCommand("pastry");
        case "order":
            return new ViewCommand("order");
        case "ordernum":
            return new ViewCommand(index);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }
    }
}
