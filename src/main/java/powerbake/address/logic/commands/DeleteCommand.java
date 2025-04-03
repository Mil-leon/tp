package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import powerbake.address.commons.core.index.Index;
import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.order.Order;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * Represents a command to delete a client or pastry identified by its index number
 * in the application's displayed list.
 *
 * The `DeleteCommand` handles two different types of entities: Client & Pastry
 *
 * A successful execution of this command removes the specified entity from the application model.
 *
 * Command usage:
 *  delete client 1
 *  delete pastry 2
 *
 * Requires a valid entity type ("client" or "pastry") and an integer index
 * referencing the position of the entity in the displayed list. If the index is invalid,
 * an exception will be raised.
 */
public class DeleteCommand extends Command {

    /**
     * The keyword for invoking the delete command.
     */
    public static final String COMMAND_WORD = "delete";

    /**
     * The usage pattern for the delete command.
     * Describes the expected syntax and provides an example.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the client/pastry/order identified by the index in the displayed list.\n"
            + "Parameters: TYPE (client/pastry/order) INDEX (must be a positive integer)\n"
            + "Example: delete client 1\n"
            + "Example: delete pastry 2\n"
            + "Example: delete order 3";

    /**
     * The success message displayed after deleting a client.
     */
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Client: %1$s";

    /**
     * The success message displayed after deleting a pastry.
     */
    public static final String MESSAGE_DELETE_PASTRY_SUCCESS = "Deleted Pastry: %1$s";

    /**
     * The success message displayed after deleting an order.
     */
    public static final String MESSAGE_DELETE_ORDER_SUCCESS = """
            Deleted Order ID: %1$s\s
            ---------------------------------------------------------------\s
            Date:  %2$s\s
            Client:  %3$s\s
            Items:  %4$s\s
            Total Price:  %5$s
            """;

    /**
     * The entity type to be deleted, either "client" or "pastry."
     */
    private final String entityType;

    /**
     * The zero-based index of the entity to delete from the displayed list.
     */
    private final Index targetIndex;

    /**
     * Creates a DeleteCommand to delete the specified entity type at the given index.
     *
     * @param entityType The type of entity to delete ("client" or "pastry").
     * @param targetIndex The index of the entity in the displayed list to be deleted.
     */
    public DeleteCommand(String entityType, Index targetIndex) {
        this.entityType = entityType.toLowerCase();
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the delete command based on the specified entity type and index.
     *
     * @param model The model on which the command should operate.
     * @return The result message of the operation, to be shown to the user.
     * @throws CommandException If the specified entity type is invalid,
     *         or if the index is out of bounds for the specified entity type's list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        return switch (entityType) {
        case "client" -> deleteClient(model);
        case "pastry" -> deletePastry(model);
        case "order" -> deleteOrder(model);
        default -> throw new CommandException("Invalid entity type for deletion: " + entityType);
        };
    }

    /**
     * Deletes a client at the specified index from the displayed client list.
     *
     * @param model The model containing the client list.
     * @return The result message indicating successful deletion.
     * @throws CommandException If the specified index is invalid or out of bounds.
     */
    private CommandResult deleteClient(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                        Messages.MESSAGE_INVALID_INDEX)
                    );
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete.getName()));
    }

    /**
     * Deletes a pastry at the specified index from the displayed pastry list.
     *
     * @param model The model containing the pastry list.
     * @return The result message indicating successful deletion.
     * @throws CommandException If the specified index is invalid or out of bounds.
     */
    private CommandResult deletePastry(Model model) throws CommandException {
        List<Pastry> lastShownList = model.getFilteredPastryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX,
                        Messages.MESSAGE_INVALID_INDEX)
                    );
        }

        Pastry pastryToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePastry(pastryToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName()));
    }

    /**
     * Deletes an order at the specified index from the displayed order list.
     *
     * @param model The model containing the order list.
     * @return The result message indicating successful deletion.
     * @throws CommandException If the specified index is invalid or out of bounds.
     */
    private CommandResult deleteOrder(Model model) throws CommandException {
        List<Order> lastShownList = model.getFilteredOrderList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX,
                        Messages.MESSAGE_INVALID_INDEX)
                    );
        }

        Order orderToDelete = lastShownList.get(targetIndex.getZeroBased());

        List<String> orderItemsSummary = orderToDelete.getOrderItems()
                .stream()
                .map(item -> item.getQuantity() + "x " + item.getPastry().getName())
                .collect(Collectors.toList());

        String orderItemsString = String.join(",  ", orderItemsSummary);
        String price = String.format("$%.2f", orderToDelete.getTotalPrice());

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("EEE, d MMM yyyy hh:mm a")
                .withLocale(Locale.US);
        String orderDate = orderToDelete.getOrderDate().format(dateTimeFormatter);

        model.deleteOrder(orderToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_ORDER_SUCCESS,
                orderToDelete.getOrderId(),
                orderDate,
                orderToDelete.getCustomer().getName(),
                orderItemsString,
                price));
    }

    /**
     * Compares this DeleteCommand object with another object for equality.
     *
     * @param other The object to compare with.
     * @return true if the other object is the same command with the same fields,
     *         false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return entityType.equals(otherDeleteCommand.entityType)
                && targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    /**
     * Returns the string representation of this command, mainly for debugging.
     *
     * @return A string containing the details of the command's target index.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex.getOneBased())
                .toString();
    }
}
