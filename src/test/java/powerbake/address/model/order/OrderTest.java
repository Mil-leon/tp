package powerbake.address.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;
import powerbake.address.testutil.PersonBuilder;

public class OrderTest {
    private Person customer;
    private List<OrderItem> orderItems;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private Pastry pastry1;
    private Pastry pastry2;

    @BeforeEach
    public void setUp() {
        customer = new PersonBuilder().build();

        pastry1 = powerbake.address.testutil.TypicalPastries.CAKE;
        pastry2 = powerbake.address.testutil.TypicalPastries.BAGEL;

        orderItem1 = new OrderItem(pastry1, 2);
        orderItem2 = new OrderItem(pastry2, 3);

        orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
    }

    @Test
    public void constructor_nullValues_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new Order(null, orderItems);
        });
        assertThrows(NullPointerException.class, () -> {
            new Order(null, customer, orderItems, LocalDateTime.now(), OrderStatus.PENDING);
        });
    }

    @Test
    public void constructor_basicConstructor_success() {
        Order order = new Order(customer, orderItems);

        assertNotNull(order.getOrderId());
        assertEquals(customer, order.getCustomer());
        assertEquals(orderItems.size(), order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(orderItem1));
        assertTrue(order.getOrderItems().contains(orderItem2));
        assertNotNull(order.getOrderDate());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    public void constructor_fullConstructor_success() {
        OrderId orderId = new OrderId();
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PROCESSING;

        Order order = new Order(orderId, customer, orderItems, orderDate, status);

        assertEquals(orderId, order.getOrderId());
        assertEquals(customer, order.getCustomer());
        assertEquals(orderItems.size(), order.getOrderItems().size());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(status, order.getStatus());
    }

    @Test
    public void getOrderId_returnsCorrectOrderId() {
        OrderId orderId = new OrderId();
        Order order = new Order(orderId, customer, orderItems, LocalDateTime.now(), OrderStatus.PENDING);

        assertEquals(orderId, order.getOrderId());
    }

    @Test
    public void getCustomer_returnsCorrectCustomer() {
        Order order = new Order(customer, orderItems);

        assertEquals(customer, order.getCustomer());
    }

    @Test
    public void getOrderItems_returnsUnmodifiableList() {
        Order order = new Order(customer, orderItems);
        List<OrderItem> returnedItems = order.getOrderItems();

        assertEquals(orderItems.size(), returnedItems.size());
        assertThrows(UnsupportedOperationException.class, () -> {
            returnedItems.add(new OrderItem(pastry1, 1));
        });
    }

    @Test
    public void getOrderDate_returnsCorrectDate() {
        LocalDateTime orderDate = LocalDateTime.now();
        Order order = new Order(new OrderId(), customer, orderItems, orderDate, OrderStatus.PENDING);

        assertEquals(orderDate, order.getOrderDate());
    }

    @Test
    public void getStatus_returnsCorrectStatus() {
        OrderStatus status = OrderStatus.DELIVERED;
        Order order = new Order(new OrderId(), customer, orderItems, LocalDateTime.now(), status);

        assertEquals(status, order.getStatus());
    }

    @Test
    public void withStatus_returnsNewOrderWithUpdatedStatus() {
        Order originalOrder = new Order(customer, orderItems);

        assertEquals(OrderStatus.PENDING, originalOrder.getStatus());
        Order updatedOrder = originalOrder.withStatus(OrderStatus.PROCESSING);

        assertEquals(OrderStatus.PENDING, originalOrder.getStatus());
        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
        assertEquals(originalOrder.getOrderId(), updatedOrder.getOrderId());
        assertEquals(originalOrder.getCustomer(), updatedOrder.getCustomer());
        assertEquals(originalOrder.getOrderItems().size(), updatedOrder.getOrderItems().size());
        assertEquals(originalOrder.getOrderDate(), updatedOrder.getOrderDate());
    }

    @Test
    public void getTotalPrice_calculatesCorrectTotal() {
        Order order = new Order(customer, orderItems);

        double expectedTotal = (pastry1.getPrice().getValueOfAmount() * 2)
            + (pastry2.getPrice().getValueOfAmount() * 3);
        assertEquals(expectedTotal, order.getTotalPrice());
    }

    @Test
    public void equals_sameOrderId_returnsTrue() {
        OrderId orderId = new OrderId();
        Order order1 = new Order(orderId, customer, orderItems, LocalDateTime.now(), OrderStatus.PENDING);

        List<OrderItem> differentItems = new ArrayList<>();
        differentItems.add(new OrderItem(pastry1, 1));
        Person differentCustomer = new PersonBuilder().withName("Different Customer").build();

        Order order2 = new Order(orderId, differentCustomer, differentItems,
                LocalDateTime.now().plusDays(1), OrderStatus.DELIVERED);

        assertTrue(order1.equals(order2));
    }

    @Test
    public void equals_differentOrderId_returnsFalse() {
        Order order1 = new Order(customer, orderItems);
        Order order2 = new Order(customer, orderItems);

        assertFalse(order1.equals(order2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Order order = new Order(customer, orderItems);

        assertTrue(order.equals(order));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        Order order = new Order(customer, orderItems);

        assertFalse(order.equals(null));
    }

    @Test
    public void equals_differentObjectType_returnsFalse() {
        Order order = new Order(customer, orderItems);

        assertFalse(order.equals("not an order"));
    }

    @Test
    public void hashCode_sameOrderId_returnsSameHashCode() {
        OrderId orderId = new OrderId();
        Order order1 = new Order(orderId, customer, orderItems, LocalDateTime.now(), OrderStatus.PENDING);
        Order order2 = new Order(orderId, customer, orderItems, LocalDateTime.now(), OrderStatus.PROCESSING);

        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    public void toString_containsRelevantInformation() {
        Order order = new Order(customer, orderItems);
        String orderString = order.toString();

        assertTrue(orderString.contains(order.getOrderId().toString()));
        assertTrue(orderString.contains(customer.getName().toString()));
        assertTrue(orderString.contains(OrderStatus.PENDING.toString()));
        assertTrue(orderString.contains(String.valueOf(order.getTotalPrice())));
    }
}
