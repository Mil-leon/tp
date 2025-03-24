package powerbake.address.model.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class OrderIdTest {

    @Test
    public void constructor_null_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new OrderId(null);
        });
    }

    @Test
    public void constructor_defaultConstructor_createsValidUuid() {
        OrderId orderId = new OrderId();

        assertNotNull(orderId.toString());
        assertTrue(OrderId.isValidOrderId(orderId.toString()));

        assertDoesNotThrow(() -> {
            UUID.fromString(orderId.toString());
        });
    }

    @Test
    public void constructor_withValidId_success() {
        assertDoesNotThrow(() -> {
            String validUuid = UUID.randomUUID().toString();
            OrderId orderId = new OrderId(validUuid);

            assertEquals(validUuid, orderId.toString());
        });
    }

    @Test
    public void constructor_withInvalidId_throwsException() {
        String invalidUuid = "not-a-uuid";

        assertThrows(IllegalArgumentException.class, () -> {
            new OrderId(invalidUuid);
        });
    }

    @Test
    public void isValidOrderId_validUuid_returnsTrue() {
        String validUuid = UUID.randomUUID().toString();

        assertTrue(OrderId.isValidOrderId(validUuid));
    }

    @Test
    public void isValidOrderId_invalidUuid_returnsFalse() {
        String invalidUuid = "invalid-uuid-format";

        assertFalse(OrderId.isValidOrderId(invalidUuid));
    }

    @Test
    public void isValidOrderId_null_returnsFalse() {
        assertFalse(OrderId.isValidOrderId(null));
    }

    @Test
    public void isValidOrderId_emptyString_returnsFalse() {
        assertFalse(OrderId.isValidOrderId(""));
    }

    @Test
    public void toString_returnsUuidString() {
        String uuidString = UUID.randomUUID().toString();
        OrderId orderId = new OrderId(uuidString);

        assertEquals(uuidString, orderId.toString());
    }

    @Test
    public void equals_sameOrderId_returnsTrue() {
        String uuidString = UUID.randomUUID().toString();
        OrderId orderId1 = new OrderId(uuidString);
        OrderId orderId2 = new OrderId(uuidString);

        assertTrue(orderId1.equals(orderId2));
    }

    @Test
    public void equals_differentOrderId_returnsFalse() {
        OrderId orderId1 = new OrderId();
        OrderId orderId2 = new OrderId();

        assertFalse(orderId1.equals(orderId2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        OrderId orderId = new OrderId();

        assertTrue(orderId.equals(orderId));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        OrderId orderId = new OrderId();

        assertFalse(orderId.equals(null));
    }

    @Test
    public void equals_differentObjectType_returnsFalse() {
        OrderId orderId = new OrderId();

        assertFalse(orderId.equals("not an order id"));
    }

    @Test
    public void hashCode_sameOrderId_returnsSameHashCode() {
        String uuidString = UUID.randomUUID().toString();
        OrderId orderId1 = new OrderId(uuidString);
        OrderId orderId2 = new OrderId(uuidString);

        assertEquals(orderId1.hashCode(), orderId2.hashCode());
    }

    @Test
    public void hashCode_differentOrderId_returnsDifferentHashCode() {
        OrderId orderId1 = new OrderId();
        OrderId orderId2 = new OrderId();

        assertNotEquals(orderId1.hashCode(), orderId2.hashCode());
    }
}
