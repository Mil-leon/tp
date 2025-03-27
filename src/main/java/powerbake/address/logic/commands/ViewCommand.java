package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static powerbake.address.model.Model.PREDICATE_SHOW_ALL_PASTRIES;
import static powerbake.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": views all clients/pastries in address book, depending on the type specified.\n"
            + "Example: "
            + COMMAND_WORD + " client (OR) "
            + COMMAND_WORD + " pastry";

    public static final String MESSAGE_SUCCESS = "Viewing full %1$s list";

    private final String type;

    public ViewCommand(String type) {
        this.type = type;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (type.equals("client")) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        } else if (type.equals("pastry")) {
            model.updateFilteredPastryList(PREDICATE_SHOW_ALL_PASTRIES);
        } else {
            // invalid type
            throw new CommandException(MESSAGE_USAGE);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, type), false, false,
                type.equals("client"), type.equals("pastry"), false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand otherViewCommand = (ViewCommand) other;
        return this.type.equals(otherViewCommand.type);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("type", this.type)
                .toString();
    }

}
