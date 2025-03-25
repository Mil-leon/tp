package powerbake.address.testutil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderId;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * A utility class containing a list of {@code Order} objects to be used in tests.
 */
public class TypicalOrders {
    private static final Person ALICE = TypicalPersons.ALICE;
    private static final Person CARL = TypicalPersons.CARL;
    private static final Pastry CROISSANT = TypicalPastries.CROISSANT;
    private static final Pastry BAGEL = TypicalPastries.BAGEL;

    public static final Order ORDER_ONE = new OrderBuilder()
            .withOrderId(new OrderId("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"))
            .withCustomer(ALICE)
            .withOrderItems(List.of(new OrderItem(CROISSANT, 2)))
            .withOrderDate(LocalDateTime.parse("2021-10-10T10:00:20"))
            .withStatus(OrderStatus.PENDING)
            .build();

    public static final Order ORDER_TWO = new OrderBuilder()
            .withOrderId(new OrderId("f81daaae-7dec-11d0-a765-00a0c91e6bf6"))
            .withCustomer(CARL)
            .withOrderItems(List.of(
                new OrderItem(CROISSANT, 1),
                new OrderItem(BAGEL, 3)
            ))
            .withOrderDate(LocalDateTime.parse("2021-10-10T10:00:20"))
            .withStatus(OrderStatus.PROCESSING)
            .build();

    public static final Order ORDER_THREE = new OrderBuilder()
            .withOrderId(new OrderId("111d4fae-7dec-11d0-a765-00a0c91e6bf6"))
            .withCustomer(ALICE)
            .withOrderItems(List.of(new OrderItem(CROISSANT, 3)))
            .withOrderDate(LocalDateTime.parse("2021-10-10T10:00:20"))
            .withStatus(OrderStatus.DELIVERED)
            .build();

    private TypicalOrders() {} // prevents instantiation

    public static List<Order> getTypicalOrders() {
        return new ArrayList<>(Arrays.asList(ORDER_ONE, ORDER_TWO, ORDER_THREE));
    }
}
