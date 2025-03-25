package powerbake.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import powerbake.address.commons.exceptions.IllegalValueException;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.pastry.Pastry;

/**
 * Jackson-friendly version of {@link OrderItem}.
 */
public class JsonAdaptedOrderItem {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "OrderItem's %s field is missing!";
    public static final String INVALID_QUANTITY = "Quantity must be a positive integer";

    private final JsonAdaptedPastry pastry;
    private final Integer quantity;

    /**
     * Constructs a {@code JsonAdaptedOrderItem} with the given order item details.
     */
    @JsonCreator
    public JsonAdaptedOrderItem(@JsonProperty("pastry") JsonAdaptedPastry pastry,
                               @JsonProperty("quantity") Integer quantity) {
        this.pastry = pastry;
        this.quantity = quantity;
    }

    /**
     * Converts a given {@code OrderItem} into this class for Jackson use.
     */
    public JsonAdaptedOrderItem(OrderItem source) {
        pastry = new JsonAdaptedPastry(source.getPastry());
        quantity = source.getQuantity();
    }

    /**
     * Converts this Jackson-friendly adapted order item object into the model's {@code OrderItem} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted order item.
     */
    public OrderItem toModelType() throws IllegalValueException {
        if (pastry == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "pastry"));
        }
        final Pastry modelPastry = pastry.toModelType();

        if (quantity == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "quantity"));
        }
        if (quantity <= 0) {
            throw new IllegalValueException(INVALID_QUANTITY);
        }

        return new OrderItem(modelPastry, quantity);
    }
}
