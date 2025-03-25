package powerbake.address.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OrderStatusTest {

    @Test
    public void toString_pending_returnsCorrectDisplayName() {
        assertEquals("Pending", OrderStatus.PENDING.toString());
    }

    @Test
    public void toString_processing_returnsCorrectDisplayName() {
        assertEquals("Processing", OrderStatus.PROCESSING.toString());
    }

    @Test
    public void toString_ready_returnsCorrectDisplayName() {
        assertEquals("Ready for delivery", OrderStatus.READY.toString());
    }

    @Test
    public void toString_delivered_returnsCorrectDisplayName() {
        assertEquals("Delivered", OrderStatus.DELIVERED.toString());
    }

    @Test
    public void toString_cancelled_returnsCorrectDisplayName() {
        assertEquals("Cancelled", OrderStatus.CANCELLED.toString());
    }
}
