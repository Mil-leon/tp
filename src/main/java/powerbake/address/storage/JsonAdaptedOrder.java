package powerbake.address.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import powerbake.address.commons.exceptions.IllegalValueException;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderId;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.model.person.Person;

/**
 * Jackson-friendly version of {@link Order}.
 */
public class JsonAdaptedOrder {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Order's %s field is missing!";

    private final String orderId;
    private final JsonAdaptedPerson customer;
    private final List<JsonAdaptedOrderItem> orderItems = new ArrayList<>();
    private final String orderDate;
    private final String status;

    /**
     * Constructs a {@code JsonAdaptedOrder} with the given order details.
     */
    @JsonCreator
    public JsonAdaptedOrder(@JsonProperty("orderId") String orderId,
                            @JsonProperty("customer") JsonAdaptedPerson customer,
                            @JsonProperty("orderItems") List<JsonAdaptedOrderItem> orderItems,
                            @JsonProperty("orderDate") String orderDate,
                            @JsonProperty("status") String status) {
        this.orderId = orderId;
        this.customer = customer;
        if (orderItems != null) {
            this.orderItems.addAll(orderItems);
        }
        this.orderDate = orderDate;
        this.status = status;
    }

    /**
     * Converts a given {@code Order} into this class for Jackson use.
     */
    public JsonAdaptedOrder(Order source) {
        orderId = source.getOrderId().toString();
        customer = new JsonAdaptedPerson(source.getCustomer());
        orderItems.addAll(source.getOrderItems().stream()
                .map(JsonAdaptedOrderItem::new)
                .collect(Collectors.toList()));
        orderDate = source.getOrderDate().toString();
        status = source.getStatus().toString();
    }

    /**
     * Converts this Jackson-friendly adapted order object into the model's {@code Order} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted order.
     */
    public Order toModelType() throws IllegalValueException {
        if (orderId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "orderId"));
        }
        if (!OrderId.isValidOrderId(orderId)) {
            throw new IllegalValueException(OrderId.MESSAGE_CONSTRAINTS);
        }
        final OrderId modelOrderId = new OrderId(orderId);

        if (customer == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "customer"));
        }
        final Person modelCustomer = customer.toModelType();

        final List<OrderItem> modelOrderItems = new ArrayList<>();
        for (JsonAdaptedOrderItem item : orderItems) {
            modelOrderItems.add(item.toModelType());
        }
        if (modelOrderItems.isEmpty()) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "orderItems"));
        }

        if (orderDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "orderDate"));
        }
        final LocalDateTime modelOrderDate;
        try {
            modelOrderDate = LocalDateTime.parse(orderDate);
        } catch (Exception e) {
            throw new IllegalValueException("Invalid order date format");
        }

        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "status"));
        }
        final OrderStatus modelStatus;
        try {
            modelStatus = getOrderStatusFromString(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException("Invalid order status");
        }

        return new Order(modelOrderId, modelCustomer, modelOrderItems, modelOrderDate, modelStatus);
    }

    /**
     * Converts a string representation of OrderStatus to the enum value.
     */
    private OrderStatus getOrderStatusFromString(String statusString) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.toString().equals(statusString)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status: " + statusString);
    }
}
