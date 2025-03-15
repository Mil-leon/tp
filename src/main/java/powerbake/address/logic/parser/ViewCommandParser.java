package powerbake.address.logic.parser;

import static java.util.Objects.requireNonNull;

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
            throw new ParseException(ViewCommand.MESSAGE_USAGE);
        }

        switch (trimmedArgs.toLowerCase()) {
        case "client":
            return new ViewCommand("client");
        case "pastry":
            return new ViewCommand("pastry");
        default:
            throw new ParseException(ViewCommand.MESSAGE_USAGE);
        }
    }
}
