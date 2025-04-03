package powerbake.address.logic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import powerbake.address.logic.parser.Prefix;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The client index provided is invalid";
    public static final String MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX = "The pastry index provided is invalid";
    public static final String MESSAGE_INVALID_PASTRY_DISPLAYED = "The pastry name provided is invalid";
    public static final String MESSAGE_REPEATED_PASTRY_IN_ORDER = "The pastry name is repeated in order command";
    public static final String MESSAGE_INVALID_ORDER_DISPLAYED_INDEX = "The order index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d client listed!";
    public static final String MESSAGE_PASTRIES_LISTED_OVERVIEW = "%1$d pastries listed!";
    public static final String MESSAGE_ORDERS_LISTED_OVERVIEW = "%1$d orders listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_ENTITY = "Invalid entity. Only 'client' and 'pastry' are supported.";
    public static final String MESSAGE_CONSTRAINTS_ORDERSTATUS =
            "Order status should only be either: pending, "
                    + "processing, "
                    + "ready, "
                    + "delivered, "
                    + "cancelled";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).map(String::trim).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code pastry} for display to the user.
     */
    public static String format(Pastry pastry) {
        final StringBuilder builder = new StringBuilder();
        builder.append(pastry.getName())
                .append("; Price: ")
                .append(pastry.getPrice().amount);
        return builder.toString();
    }

    /**
     * Formats the {@code order} for display to the user.
     */
    public static String format(Order order) {
        final StringBuilder builder = new StringBuilder();
        builder.append("; Client Name: ")
                .append(order.getCustomer().getName().toString());
        List<OrderItem> orderList = order.getOrderItems();
        for (OrderItem orderItem : orderList) {
            builder.append("; Pastry Name: ")
                    .append(orderItem.getPastry().getName())
                    .append("; Quantity: ")
                    .append(orderItem.getQuantity());
        }
        builder.append("; Status: ").append(order.getStatus());
        return builder.toString();
    }

}
