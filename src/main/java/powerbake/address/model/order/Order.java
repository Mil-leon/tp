package powerbake.address.model.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import powerbake.address.commons.util.CollectionUtil;
import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.model.person.Person;

/**
 * Represents an Order in the PowerBake system.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Order {
    // Unique identifier for the order
    private final OrderId orderId;

    // The customer who placed the order
    private final Person customer;

    // List of pastries in this order
    private final List<OrderItem> orderItems;

    // When the order was created
    private final LocalDateTime orderDate;

    // Current status of the order
    private final OrderStatus status;

    /**
     * Creates an Order with the specified details.
     */
    public Order(Person customer, List<OrderItem> orderItems) {
        CollectionUtil.requireAllNonNull(customer, orderItems);
        this.orderId = new OrderId();
        this.customer = customer;
        this.orderItems = new ArrayList<>(orderItems);
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    /**
     * Creates an Order with all details specified (including ID, date, and status).
     * This constructor is primarily used for loading saved orders.
     */
    public Order(OrderId orderId, Person customer, List<OrderItem> orderItems,
                LocalDateTime orderDate, OrderStatus status) {
        CollectionUtil.requireAllNonNull(orderId, customer, orderItems, orderDate, status);
        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = new ArrayList<>(orderItems);
        this.orderDate = orderDate;
        this.status = status;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Person getCustomer() {
        return customer;
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Returns a new Order with the updated status.
     */
    public Order withStatus(OrderStatus newStatus) {
        return new Order(this.orderId, this.customer, this.orderItems, this.orderDate, newStatus);
    }

    /**
     * Returns if status is valid.
     */
    public static boolean isValidStatus(String status) {
        return switch (status) {
        case "PENDING" -> true;
        case "PROCESSING" -> true;
        case "READY" -> true;
        case "DELIVERED" -> true;
        case "CANCELLED" -> true;
        default -> false;
        };
    }

    /**
     * Calculates the total price of the order based on all order items.
     */
    public double getTotalPrice() {
        return orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    /**
     * Returns true if both orders have id.
     */
    public boolean isSameOrder(Order otherOrder) {
        if (otherOrder == this) {
            return true;
        }

        return otherOrder != null
                && otherOrder.getOrderId().equals(this.getOrderId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Order)) {
            return false;
        }

        Order otherOrder = (Order) other;
        return orderId.equals(otherOrder.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("orderId", orderId)
                .add("customer", customer.getName())
                .add("orderDate", orderDate)
                .add("status", status)
                .add("totalPrice", getTotalPrice())
                .toString();
    }

}
