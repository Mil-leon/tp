package powerbake.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static powerbake.address.testutil.TypicalPersons.ALICE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.exceptions.IllegalValueException;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.testutil.OrderBuilder;

public class JsonAdaptedOrderTest {
    private static final String INVALID_ORDER_ID = "not-a-uuid";
    private static final String INVALID_ORDER_DATE = "not-a-date";
    private static final String INVALID_ORDER_STATUS = "not-a-status";

    private static final String VALID_ORDER_ID = "123e4567-e89b-12d3-a456-426614174000";
    private static final String VALID_ORDER_DATE = LocalDateTime.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private static final OrderStatus VALID_ORDER_STATUS = OrderStatus.PENDING;
    private static final JsonAdaptedPerson VALID_CUSTOMER = new JsonAdaptedPerson(ALICE);
    private static final List<JsonAdaptedOrderItem> VALID_ORDER_ITEMS = new ArrayList<>();

    @Test
    public void toModelType_validOrderDetails_returnsOrder() throws Exception {
        Order ord = new OrderBuilder().build();
        JsonAdaptedOrder order = new JsonAdaptedOrder(ord);
        assertEquals(ord, order.toModelType());
    }

    @Test
    public void toModelType_invalidOrderId_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                INVALID_ORDER_ID, VALID_CUSTOMER, VALID_ORDER_ITEMS,
                VALID_ORDER_DATE, VALID_ORDER_STATUS.name());
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_nullOrderId_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                null, VALID_CUSTOMER, VALID_ORDER_ITEMS,
                VALID_ORDER_DATE, VALID_ORDER_STATUS.name());
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_invalidOrderDate_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                VALID_ORDER_ID, VALID_CUSTOMER, VALID_ORDER_ITEMS,
                INVALID_ORDER_DATE, VALID_ORDER_STATUS.name());
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_nullOrderDate_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                VALID_ORDER_ID, VALID_CUSTOMER, VALID_ORDER_ITEMS,
                null, VALID_ORDER_STATUS.name());
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_invalidOrderStatus_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                VALID_ORDER_ID, VALID_CUSTOMER, VALID_ORDER_ITEMS,
                VALID_ORDER_DATE, INVALID_ORDER_STATUS);
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_nullOrderStatus_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                VALID_ORDER_ID, VALID_CUSTOMER, VALID_ORDER_ITEMS,
                VALID_ORDER_DATE, null);
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_nullCustomer_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                VALID_ORDER_ID, null, VALID_ORDER_ITEMS,
                VALID_ORDER_DATE, VALID_ORDER_STATUS.name());
        assertThrows(IllegalValueException.class, order::toModelType);
    }

    @Test
    public void toModelType_nullOrderItems_throwsIllegalValueException() {
        JsonAdaptedOrder order = new JsonAdaptedOrder(
                VALID_ORDER_ID, VALID_CUSTOMER, null,
                VALID_ORDER_DATE, VALID_ORDER_STATUS.name());
        assertThrows(IllegalValueException.class, order::toModelType);
    }
}
