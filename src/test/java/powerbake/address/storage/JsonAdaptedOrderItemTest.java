package powerbake.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static powerbake.address.testutil.TypicalPastries.CROISSANT;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.exceptions.IllegalValueException;
import powerbake.address.model.order.OrderItem;

public class JsonAdaptedOrderItemTest {
    private static final int VALID_QUANTITY = 5;
    private static final int INVALID_QUANTITY = -1;
    private static final JsonAdaptedPastry VALID_PASTRY = new JsonAdaptedPastry(CROISSANT);

    @Test
    public void toModelType_validOrderItemDetails_returnsOrderItem() throws Exception {
        OrderItem orderItem = new OrderItem(CROISSANT, VALID_QUANTITY);
        JsonAdaptedOrderItem adaptedOrderItem = new JsonAdaptedOrderItem(orderItem);
        assertEquals(orderItem, adaptedOrderItem.toModelType());
    }

    @Test
    public void toModelType_invalidQuantity_throwsIllegalValueException() {
        JsonAdaptedOrderItem orderItem = new JsonAdaptedOrderItem(VALID_PASTRY, INVALID_QUANTITY);
        assertThrows(IllegalValueException.class, orderItem::toModelType);
    }

    @Test
    public void toModelType_nullPastry_throwsIllegalValueException() {
        JsonAdaptedOrderItem orderItem = new JsonAdaptedOrderItem(null, VALID_QUANTITY);
        assertThrows(IllegalValueException.class, orderItem::toModelType);
    }
}
