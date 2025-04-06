package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY_NAME;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_QUANTITY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import powerbake.address.commons.core.index.Index;
import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * Adds an order to the address book.
 */
public class AddOrderCommand extends AddCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_ORDER
            + ": Adds a order to the address book. "
            + "Parameters: "
            + PREFIX_ORDER + "CLIENT_INDEX "
            + PREFIX_PASTRY_NAME + "PASTRY NAME " + PREFIX_QUANTITY + "QUANTITY "
            + "[" + PREFIX_PASTRY_NAME + "PASTRY NAME " + PREFIX_QUANTITY + "QUANTITY]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ORDER + "1 "
            + PREFIX_PASTRY_NAME + "Croissant "
            + PREFIX_QUANTITY + "10";

    public static final String MESSAGE_SUCCESS = "New order added: %1$s";
    public static final String MESSAGE_DUPLICATE_ORDER = "This order already exists in the address book";

    private Order toAddOrder;

    private final Index clientIndex;

    private final ArrayList<ArrayList<String>> unformattedOrderList;

    /**
     * Creates an AddOrderCommand to add index and unformattedOrderList to generate {@code Order}
     */
    public AddOrderCommand(Index index, ArrayList<ArrayList<String>> unformattedOrderList) {
        requireNonNull(index);
        clientIndex = index;
        this.unformattedOrderList = unformattedOrderList;
    }

    /**
     * Creates an AddOrderCommand to add the specified {@code Order}
     */
    public AddOrderCommand(Order order) {
        requireNonNull(order);
        toAddOrder = order;
        clientIndex = null;
        unformattedOrderList = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personWhoOrdered = getValidClient(model);
        ArrayList<OrderItem> orderList = getValidOrderItems(model);
        toAddOrder = new Order(personWhoOrdered, orderList);

        if (model.hasOrder(toAddOrder)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORDER);
        }

        model.addOrder(toAddOrder);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAddOrder)));
    }

    private Person getValidClient(Model model) throws CommandException {
        requireNonNull(clientIndex);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(clientIndex.getZeroBased());
    }

    private ArrayList<OrderItem> getValidOrderItems(Model model) throws CommandException {
        List<Pastry> lastShownList = model.getFilteredPastryList();
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        requireNonNull(unformattedOrderList);
        HashSet<String> uniquePastryNames = new HashSet<>();

        for (ArrayList<String> order : unformattedOrderList) {
            // Check for valid pastry name
            if (lastShownList.stream()
                    .noneMatch(pastry -> pastry
                            .getName()
                            .toString()
                            .equalsIgnoreCase(order.get(0)))) {
                throw new CommandException(Messages.MESSAGE_INVALID_PASTRY_DISPLAYED);
            }

            Pastry pastry = lastShownList.stream()
                    .filter(existingPastry -> existingPastry
                            .getName()
                            .toString()
                            .equalsIgnoreCase(order.get(0)))
                    .findFirst().get();

            // Check for duplicates
            if (uniquePastryNames.contains(pastry.getName().toString())) {
                throw new CommandException(Messages.MESSAGE_REPEATED_PASTRY_IN_ORDER);
            }
            // Create and add order Object
            int quantity = Integer.parseInt(order.get(1));
            OrderItem newOrder = new OrderItem(pastry, quantity);
            orderItems.add(newOrder);
            uniquePastryNames.add(pastry.getName().toString());
        }

        return orderItems;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddOrderCommand)) {
            return false;
        }

        AddOrderCommand otherOrderCommand = (AddOrderCommand) other;
        return toAddOrder.equals(otherOrderCommand.toAddOrder);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAddOrder", toAddOrder)
                .toString();
    }
}
