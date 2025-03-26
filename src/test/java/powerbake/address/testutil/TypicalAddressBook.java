package powerbake.address.testutil;

import powerbake.address.model.AddressBook;
import powerbake.address.model.order.Order;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;


/**
 * A utility class containing a typical {@code AddressBook} object to be used in tests.
 */
public class TypicalAddressBook {

    private TypicalAddressBook() {} //prevent instantiation

    /**
    * Returns an {@code AddressBook} with all the typical persons and pastries.
    */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : TypicalPersons.getTypicalPersons()) {
            ab.addPerson(person);
        }
        for (Pastry pastry : TypicalPastries.getTypicalPastries()) {
            ab.addPastry(pastry);
        }
        for (Order order : TypicalOrders.getTypicalOrders()) {
            ab.addOrder(order);
        }
        return ab;
    }
}
