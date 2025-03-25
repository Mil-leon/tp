package powerbake.address.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;

public class OrderItemTest {
    private Pastry pastry;

    @BeforeEach
    public void setUp() {
        pastry = powerbake.address.testutil.TypicalPastries.CAKE;
    }

    @Test
    public void constructor_validValues_success() {
        OrderItem orderItem = new OrderItem(pastry, 3);

        assertEquals(pastry, orderItem.getPastry());
        assertEquals(3, orderItem.getQuantity());
    }

    @Test
    public void constructor_zeroQuantity_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(pastry, 0);
        });
    }

    @Test
    public void constructor_negativeQuantity_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(pastry, -1);
        });
    }

    @Test
    public void constructor_nullPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new OrderItem(null, 1);
        });
    }

    @Test
    public void getPastry_returnsCorrectPastry() {
        OrderItem orderItem = new OrderItem(pastry, 3);

        assertEquals(pastry, orderItem.getPastry());
    }

    @Test
    public void getQuantity_returnsCorrectQuantity() {
        OrderItem orderItem = new OrderItem(pastry, 3);

        assertEquals(3, orderItem.getQuantity());
    }

    @Test
    public void getSubtotal_calculatesCorrectSubtotal() {
        OrderItem orderItem = new OrderItem(pastry, 3);
        double correctPrice = pastry.getPrice().getValueOfAmount() * 3;

        assertEquals(correctPrice, orderItem.getSubtotal());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        OrderItem orderItem1 = new OrderItem(pastry, 3);
        OrderItem orderItem2 = new OrderItem(pastry, 3);

        assertTrue(orderItem1.equals(orderItem2));
    }

    @Test
    public void equals_differentPastry_returnsFalse() {
        OrderItem orderItem1 = new OrderItem(pastry, 3);

        Pastry differentPastry = new Pastry(new Name("Eclair"), new Price("4.75"));
        OrderItem orderItem2 = new OrderItem(differentPastry, 3);

        assertFalse(orderItem1.equals(orderItem2));
    }

    @Test
    public void equals_differentQuantity_returnsFalse() {
        OrderItem orderItem1 = new OrderItem(pastry, 3);
        OrderItem orderItem2 = new OrderItem(pastry, 4);

        assertFalse(orderItem1.equals(orderItem2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        OrderItem orderItem = new OrderItem(pastry, 3);

        assertTrue(orderItem.equals(orderItem));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        OrderItem orderItem = new OrderItem(pastry, 3);

        assertFalse(orderItem.equals(null));
    }

    @Test
    public void equals_differentObjectType_returnsFalse() {
        OrderItem orderItem = new OrderItem(pastry, 3);

        assertFalse(orderItem.equals("not an order item"));
    }

    @Test
    public void hashCode_sameValues_returnsSameHashCode() {
        OrderItem orderItem1 = new OrderItem(pastry, 3);
        OrderItem orderItem2 = new OrderItem(pastry, 3);

        assertEquals(orderItem1.hashCode(), orderItem2.hashCode());
    }

    @Test
    public void toString_containsRelevantInformation() {
        OrderItem orderItem = new OrderItem(pastry, 3);
        String itemString = orderItem.toString();

        assertTrue(itemString.contains(pastry.getName().toString()));
        assertTrue(itemString.contains(String.valueOf(3)));
        assertTrue(itemString.contains(String.valueOf(orderItem.getSubtotal())));
    }
}
