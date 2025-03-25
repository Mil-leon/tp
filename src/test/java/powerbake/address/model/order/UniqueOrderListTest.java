package powerbake.address.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import powerbake.address.model.order.exceptions.DuplicateOrderException;
import powerbake.address.model.order.exceptions.OrderNotFoundException;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;
import powerbake.address.testutil.PersonBuilder;

public class UniqueOrderListTest {
    private final UniqueOrderList uniqueOrderList = new UniqueOrderList();
    private Person customer;
    private List<OrderItem> orderItems;
    private Pastry pastry;

    @BeforeEach
    public void setUp() {
        customer = new PersonBuilder().build();
        pastry = powerbake.address.testutil.TypicalPastries.BAGEL;
        OrderItem orderItem = new OrderItem(pastry, 2);
        orderItems = Collections.singletonList(orderItem);
    }

    @Test
    public void contains_orderNotInList_returnsFalse() {
        Order order = new Order(customer, orderItems);
        assertFalse(uniqueOrderList.contains(order));
    }

    @Test
    public void contains_orderInList_returnsTrue() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);
        assertTrue(uniqueOrderList.contains(order));
    }

    @Test
    public void contains_null() {
        assertThrows(NullPointerException.class, () -> uniqueOrderList.contains(null));
    }

    @Test
    public void contains_orderWithSameIdentityInList_returnsTrue() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);

        Order orderWithSameId = new Order(
                order.getOrderId(),
                new PersonBuilder().withName("Different Name").build(),
                Collections.singletonList(new OrderItem(pastry, 3)),
                LocalDateTime.now().plusDays(1),
                OrderStatus.DELIVERED
        );

        assertTrue(uniqueOrderList.contains(orderWithSameId));
    }

    @Test
    public void add_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOrderList.add(null));
    }

    @Test
    public void add_duplicateOrder_throwsDuplicateOrderException() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);

        Order orderWithSameId = new Order(
                order.getOrderId(),
                customer,
                orderItems,
                LocalDateTime.now(),
                OrderStatus.PENDING
        );

        assertThrows(DuplicateOrderException.class, () -> uniqueOrderList.add(orderWithSameId));
    }

    @Test
    public void setOrder_targetNotInList_throwsOrderNotFoundException() {
        Order target = new Order(customer, orderItems);
        Order editedOrder = new Order(customer, orderItems);

        assertThrows(OrderNotFoundException.class, () -> uniqueOrderList.setOrder(target, editedOrder));
    }

    @Test
    public void setOrder_editedOrderIsSameOrder_success() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);
        uniqueOrderList.setOrder(order, order);

        assertTrue(uniqueOrderList.contains(order));
        assertEquals(1, uniqueOrderList.asUnmodifiableObservableList().size());
    }

    @Test
    public void setOrder_editedOrderHasSameIdentity_success() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);

        Order editedOrder = order.withStatus(OrderStatus.PROCESSING);

        uniqueOrderList.setOrder(order, editedOrder);

        assertTrue(uniqueOrderList.contains(editedOrder));
        assertEquals(1, uniqueOrderList.asUnmodifiableObservableList().size());
    }

    @Test
    public void setOrder_editedOrderHasDifferentIdentity_success() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);

        Order editedOrder = new Order(customer, orderItems);

        uniqueOrderList.setOrder(order, editedOrder);

        assertTrue(uniqueOrderList.contains(editedOrder));
        assertFalse(uniqueOrderList.contains(order));
        assertEquals(1, uniqueOrderList.asUnmodifiableObservableList().size());
    }

    @Test
    public void setOrder_editedOrderHasNonUniqueIdentity_throwsDuplicateOrderException() {
        Order order1 = new Order(customer, orderItems);
        Order order2 = new Order(customer, orderItems);
        uniqueOrderList.add(order1);
        uniqueOrderList.add(order2);

        Order editedOrder = new Order(
                order2.getOrderId(),
                customer,
                orderItems,
                LocalDateTime.now(),
                OrderStatus.PROCESSING
        );

        assertThrows(DuplicateOrderException.class, () -> uniqueOrderList.setOrder(order1, editedOrder));
    }

    @Test
    public void remove_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOrderList.remove(null));
    }

    @Test
    public void remove_orderDoesNotExist_throwsOrderNotFoundException() {
        Order order = new Order(customer, orderItems);
        assertThrows(OrderNotFoundException.class, () -> uniqueOrderList.remove(order));
    }

    @Test
    public void remove_existingOrder_removesOrder() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);
        uniqueOrderList.remove(order);

        assertFalse(uniqueOrderList.contains(order));
        assertEquals(0, uniqueOrderList.asUnmodifiableObservableList().size());
    }

    @Test
    public void setOrders_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOrderList.setOrders((List<Order>) null));
    }

    @Test
    public void setOrders_list_replacesExistingList() {
        Order order1 = new Order(customer, orderItems);
        uniqueOrderList.add(order1);

        List<Order> newOrders = Collections.singletonList(new Order(customer, orderItems));
        uniqueOrderList.setOrders(newOrders);

        assertTrue(uniqueOrderList.contains(newOrders.get(0)));
        assertEquals(1, uniqueOrderList.asUnmodifiableObservableList().size());
    }

    @Test
    public void setOrders_listWithDuplicateOrders_throwsDuplicateOrderException() {
        Order order = new Order(customer, orderItems);
        List<Order> listWithDuplicates = Arrays.asList(order, order);

        assertThrows(DuplicateOrderException.class, () -> uniqueOrderList.setOrders(listWithDuplicates));
    }

    @Test
    public void setOrders_uniqueOrderList_replacesExistingList() {
        uniqueOrderList.add(new Order(customer, orderItems));

        UniqueOrderList newList = new UniqueOrderList();
        Order newOrder = new Order(customer, orderItems);
        newList.add(newOrder);

        uniqueOrderList.setOrders(newList);

        assertTrue(uniqueOrderList.contains(newOrder));
        assertEquals(1, uniqueOrderList.asUnmodifiableObservableList().size());
    }

    @Test
    public void iterator_hasNextAndNext_worksAsExpected() {
        Order order1 = new Order(customer, orderItems);
        Order order2 = new Order(customer, orderItems);

        uniqueOrderList.add(order1);
        uniqueOrderList.add(order2);

        Iterator<Order> iterator = uniqueOrderList.iterator();

        assertTrue(iterator.hasNext());
        Order firstOrder = iterator.next();
        assertTrue(uniqueOrderList.contains(firstOrder));

        assertTrue(iterator.hasNext());
        Order secondOrder = iterator.next();
        assertTrue(uniqueOrderList.contains(secondOrder));

        assertFalse(iterator.hasNext());
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        Order order = new Order(customer, orderItems);
        uniqueOrderList.add(order);

        ObservableList<Order> observableList = uniqueOrderList.asUnmodifiableObservableList();

        assertThrows(UnsupportedOperationException.class, () -> observableList.add(order));
    }
}
