package powerbake.address.model;

import javafx.collections.ObservableList;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the pastries list.
     * This list will not contain any duplicate pastries.
     */
    ObservableList<Pastry> getPastryList();

}
