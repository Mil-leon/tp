package powerbake.address.logic.commands;

import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;

/**
 * Adds a person or pastry to the address book.
 */
public abstract class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person or pastry to the address book. "
            + "Type "
            + COMMAND_WORD + " " + PREFIX_CLIENT
            + "or "
            + COMMAND_WORD + " " + PREFIX_PASTRY
            + "to learn more";
}
