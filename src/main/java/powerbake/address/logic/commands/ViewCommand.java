package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static powerbake.address.model.Model.PREDICATE_SHOW_ALL_ORDERS;
import static powerbake.address.model.Model.PREDICATE_SHOW_ALL_PASTRIES;
import static powerbake.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import powerbake.address.commons.core.index.Index;
import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.order.Order;

/**
 * Lists all persons in the address book to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": views all clients/pastries in PowerBake, depending on the type specified.\n"
            + "Example: "
            + COMMAND_WORD + " client (OR) "
            + COMMAND_WORD + " pastry (OR) "
            + COMMAND_WORD + " order (OR) "
            + COMMAND_WORD + " order INDEX (must be a positive integer)";

    public static final String MESSAGE_SUCCESS = "Viewing full %1$s list";
    public static final String MESSAGE_SUCCESS_INDEX = "Viewing %1$s at index %2$d";

    private final String type;
    private final Index index;

    /**
     * Creates a ViewCommand to view all clients/pastries in address book.
     */
    public ViewCommand(String type) {
        this.type = type;
        this.index = null;
    }

    /**
     * Creates a ViewCommand to view a specific order in address book.
     */
    public ViewCommand(Index index) {
        this.type = "order";
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Order> lastShownList = model.getFilteredOrderList();

        if (type.equals("client")) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        } else if (type.equals("pastry")) {
            model.updateFilteredPastryList(PREDICATE_SHOW_ALL_PASTRIES);
        } else if (type.equals("order")) {
            if (index == null) {
                model.updateFilteredOrderList(PREDICATE_SHOW_ALL_ORDERS);
            }
        } else {
            // invalid type
            throw new CommandException(MESSAGE_USAGE);
        }

        if (index != null) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(String.format(
                            Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                            Messages.MESSAGE_INVALID_INDEX + "\n" + MESSAGE_USAGE)
                        );
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS_INDEX, type, index.getOneBased()), false, false,
                    type.equals("client"), type.equals("pastry"), type.equals("order"), index.getZeroBased());
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, type), false, false,
                    type.equals("client"), type.equals("pastry"), type.equals("order"));
        }
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
        if (this.index == null || otherViewCommand.index == null) {
            return this.type.equals(otherViewCommand.type) && otherViewCommand.index == null && this.index == null;
        } else {
            return this.type.equals(otherViewCommand.type) && this.index.equals(otherViewCommand.index);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("type", this.type)
                .add("index", this.index)
                .toString();
    }
}
