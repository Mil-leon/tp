package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.model.Model;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all clients/pastry whose names and orders with client names that contain any of "
            + "\nthe specified keywords (case-insensitive) and displays them as a list with actual index numbers.\n"
            + "Parameters: TYPE (client/pastry/order) KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " client alice bob charlie\n"
            + "Example: " + COMMAND_WORD + " pastry croissant\n"
            + "Example: " + COMMAND_WORD + " order alice\n";

    private final String commandWord;
    private final String[] nameKeywords;

    /**
     * Creates a FindCommand to find the specified {@code Person/Pastry/Order}
     *
     * @param commandWord The type of entity to find (client/pastry/order).
     * @param nameKeywords The keywords to search for in the names of the specified entity.
     *                     The search is case-insensitive.
     */
    public FindCommand(String commandWord, String[] nameKeywords) {
        this.commandWord = commandWord;
        this.nameKeywords = nameKeywords;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (commandWord.equalsIgnoreCase("client")) {
            model.updateFilteredPersonList(
                    new powerbake.address.model.person.NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
            return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()),
                false, false, true, false, false);
        } else if (commandWord.equalsIgnoreCase("pastry")) {
            model.updateFilteredPastryList(
                    new powerbake.address.model.pastry.NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
            return new CommandResult(
                String.format(Messages.MESSAGE_PASTRIES_LISTED_OVERVIEW, model.getFilteredPastryList().size()),
                false, false, false, true, false);
        } else {
            model.updateFilteredOrderList(
                    new powerbake.address.model.order.NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
            return new CommandResult(
                String.format(Messages.MESSAGE_ORDERS_LISTED_OVERVIEW, model.getFilteredOrderList().size()),
                false, false, false, false, true);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return commandWord.equals(otherFindCommand.commandWord)
                && Arrays.equals(nameKeywords, otherFindCommand.nameKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("commandWord", commandWord)
                .add("nameKeywords", Arrays.toString(nameKeywords))
                .toString();
    }
}
