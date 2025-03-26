package powerbake.address.testutil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderId;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * A utility class to help with building Order objects.
 */
public class OrderBuilder {
    private static final Person DEFAULT_CUSTOMER = TypicalPersons.ALICE;
    private static final List<OrderItem> DEFAULT_ORDER_ITEMS = new ArrayList<>();
    private static final Pastry DEFAULT_PASTRY = TypicalPastries.CROISSANT;
    private static final int DEFAULT_QUANTITY = 2;
    private static final String DEFAULT_ORDER_ID_STRING = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6";

    static {
        DEFAULT_ORDER_ITEMS.add(new OrderItem(DEFAULT_PASTRY, DEFAULT_QUANTITY));
    }

    private OrderId orderId;
    private Person customer;
    private List<OrderItem> orderItems;
    private LocalDateTime orderDate;
    private OrderStatus status;

    /**
     * Creates a {@code OrderBuilder} with the default details.
     */
    public OrderBuilder() {
        orderId = new OrderId();
        customer = DEFAULT_CUSTOMER;
        orderItems = new ArrayList<>(DEFAULT_ORDER_ITEMS);
        orderDate = LocalDateTime.now();
        status = OrderStatus.PENDING;
    }

    /**
     * Initializes the OrderBuilder with the data of {@code orderToCopy}.
     */
    public OrderBuilder(Order orderToCopy) {
        orderId = orderToCopy.getOrderId();
        customer = orderToCopy.getCustomer();
        orderItems = new ArrayList<>(orderToCopy.getOrderItems());
        orderDate = orderToCopy.getOrderDate();
        status = orderToCopy.getStatus();
    }

    /**
     * Sets the {@code OrderId} of the {@code Order} that we are building.
     */
    public OrderBuilder withOrderId(OrderId orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Sets the {@code Person} customer of the {@code Order} that we are building.
     */
    public OrderBuilder withCustomer(Person customer) {
        this.customer = customer;
        return this;
    }

    /**
     * Sets the {@code List<OrderItem>} of the {@code Order} that we are building.
     */
    public OrderBuilder withOrderItems(List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
        return this;
    }

    /**
     * Adds an {@code OrderItem} to the {@code Order} that we are building.
     */
    public OrderBuilder withOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        return this;
    }

    /**
     * Sets the {@code LocalDateTime} of the {@code Order} that we are building.
     */
    public OrderBuilder withOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * Sets the {@code OrderStatus} of the {@code Order} that we are building.
     */
    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Builds an {@code Order} with the current attributes.
     */
    public Order build() {
        return new Order(orderId, customer, orderItems, orderDate, status);
    }
}
