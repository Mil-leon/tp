package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PRICE;

import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.pastry.Pastry;

/**
 * Adds a person to the address book.
 */
public class AddPastryCommand extends AddCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_PASTRY
            + ": Adds a pastry to Powerbake. "
            + "Parameters: "
            + PREFIX_PASTRY + "NAME "
            + PREFIX_PRICE + "PRICE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PASTRY + "Croissant "
            + PREFIX_PRICE + "5.99 ";


    public static final String MESSAGE_SUCCESS = "New pastry added: %1$s";
    public static final String MESSAGE_DUPLICATE_PASTRY = "This pastry already exists in Powerbake";

    private final Pastry toAddPastry;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddPastryCommand(Pastry pastry) {
        requireNonNull(pastry);
        toAddPastry = pastry;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPastry(toAddPastry)) {
            throw new CommandException(MESSAGE_DUPLICATE_PASTRY);
        }

        model.addPastry(toAddPastry);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAddPastry)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddPastryCommand otherAddCommand = (AddPastryCommand) other;
        return toAddPastry.equals(otherAddCommand.toAddPastry);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAddPastry)
                .toString();
    }
}
