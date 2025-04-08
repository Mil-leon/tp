package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_TAG;

import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddClientCommand extends AddCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_CLIENT
            + ": Adds a client to the address book. "
            + "Parameters: "
            + PREFIX_CLIENT + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLIENT + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New client added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This client already exists in PowerBake, "
            + "duplicate clients are checked exclusively by phone number";

    private final Person toAdd;

    /**
     * Creates an AddClientCommand to add the specified {@code Person}
     */
    public AddClientCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)),
                false, false, true, false, true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddClientCommand)) {
            return false;
        }

        AddClientCommand otherAddCommand = (AddClientCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
