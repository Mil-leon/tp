package powerbake.address.logic.commands;

import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;

/**
 * Adds a person, pastry or order to the address book.
 */
public abstract class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person, pastry or order to address book. "
            + "Type "
            + COMMAND_WORD + " " + PREFIX_CLIENT
            + "or "
            + COMMAND_WORD + " " + PREFIX_PASTRY
            + "or "
            + COMMAND_WORD + " " + PREFIX_ORDER
            + "to learn more";
}
