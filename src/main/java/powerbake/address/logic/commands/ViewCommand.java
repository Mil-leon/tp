package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static powerbake.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import powerbake.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Listed all clients";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
