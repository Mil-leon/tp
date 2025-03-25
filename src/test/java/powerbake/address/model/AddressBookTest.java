package powerbake.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static powerbake.address.testutil.Assert.assertThrows;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalPastries.CROISSANT;
import static powerbake.address.testutil.TypicalPersons.ALICE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;
import powerbake.address.model.person.exceptions.DuplicatePersonException;
import powerbake.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }


    @Test
    public void hasOrder_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasOrder(null));
    }

    @Test
    public void hasOrder_orderNotInAddressBook_returnsFalse() {
        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));
        assertFalse(addressBook.hasOrder(order));
    }

    @Test
    public void hasOrder_orderInAddressBook_returnsTrue() {
        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));
        addressBook.addOrder(order);
        assertTrue(addressBook.hasOrder(order));
    }

    @Test
    public void addOrder_orderAdded_success() {
        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));
        addressBook.addOrder(order);
        assertTrue(addressBook.hasOrder(order));
        assertEquals(1, addressBook.getOrderList().size());
    }

    @Test
    public void setOrder_orderUpdated_success() {
        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order originalOrder = new Order(person, Collections.singletonList(orderItem));
        addressBook.addOrder(originalOrder);

        Order updatedOrder = new Order(originalOrder.getCustomer(), originalOrder.getOrderItems())
                .withStatus(OrderStatus.PROCESSING);
        addressBook.setOrder(originalOrder, updatedOrder);

        assertTrue(addressBook.hasOrder(updatedOrder));
        assertFalse(addressBook.hasOrder(originalOrder));
        assertEquals(OrderStatus.PROCESSING, addressBook.getOrderList().get(0).getStatus());
    }

    @Test
    public void removeOrder_orderRemoved_success() {
        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));
        addressBook.addOrder(order);
        addressBook.removeOrder(order);

        assertFalse(addressBook.hasOrder(order));
        assertEquals(0, addressBook.getOrderList().size());
    }

    @Test
    public void getOrderList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getOrderList().remove(0));
    }

    @Test
    public void equals_sameOrders_returnsTrue() {
        AddressBook firstAddressBook = new AddressBook();
        AddressBook secondAddressBook = new AddressBook();

        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));

        firstAddressBook.addOrder(order);
        secondAddressBook.addOrder(order);

        assertEquals(firstAddressBook, secondAddressBook);
    }

    @Test
    public void equals_differentOrders_returnsFalse() {
        AddressBook firstAddressBook = new AddressBook();
        AddressBook secondAddressBook = new AddressBook();

        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));

        firstAddressBook.addOrder(order);
        // secondAddressBook has no orders

        assertFalse(firstAddressBook.equals(secondAddressBook));
    }

    @Test
    public void resetData_withValidOrdersAndPersons_replacesData() {
        AddressBook originalAddressBook = new AddressBook();
        Person person = new PersonBuilder().build();
        OrderItem orderItem = new OrderItem(CROISSANT, 2);
        Order order = new Order(person, Collections.singletonList(orderItem));
        originalAddressBook.addPerson(person);
        originalAddressBook.addPastry(CROISSANT);
        originalAddressBook.addOrder(order);

        AddressBook newAddressBook = new AddressBook();
        newAddressBook.resetData(originalAddressBook);

        assertEquals(originalAddressBook, newAddressBook);
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
            + "{persons=" + addressBook.getPersonList() + ", "
            + "pastries=" + addressBook.getPastryList() + ", "
            + "orders=" + addressBook.getOrderList() + "}";

        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Pastry> pastries = FXCollections.observableArrayList();
        private final ObservableList<Order> orders = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Pastry> getPastryList() {
            return pastries;
        }

        @Override
        public ObservableList<Order> getOrderList() {
            return orders;
        }
    }

}
