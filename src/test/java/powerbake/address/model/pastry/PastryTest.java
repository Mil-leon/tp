package powerbake.address.model.pastry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.testutil.TypicalPastries.BAGEL;
import static powerbake.address.testutil.TypicalPastries.CROISSANT;

import org.junit.jupiter.api.Test;

import powerbake.address.testutil.PastryBuilder;
import powerbake.address.testutil.PersonBuilder;

public class PastryTest {
    @Test
    public void constructor_nullValues_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Pastry(null, new Price("1.00")));
        assertThrows(NullPointerException.class, () -> new Pastry(new Name("Apple Pie"), null));
        assertThrows(NullPointerException.class, () -> new Pastry(null, null));
    }

    @Test
    public void isSamePastry() {
        Pastry pastry = new PastryBuilder(CROISSANT).build();

        // same object -> returns true
        assertTrue(pastry.isSamePastry(pastry));

        // different price (but same name) -> returns true
        assertTrue(pastry.isSamePastry(new PastryBuilder(CROISSANT).withPrice("0.10").build()));

        // null -> returns false
        assertFalse(pastry.isSamePastry(null));

        // different type -> returns false
        assertFalse(pastry.isSamePastry(new PersonBuilder().build()));

        // different name -> returns false
        assertFalse(pastry.isSamePastry(new PastryBuilder(CROISSANT).withName("Apple Pie").build()));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = CROISSANT.getName().name + " ";
        Pastry editedCroissant = new PastryBuilder(CROISSANT).withName(nameWithTrailingSpaces).build();
        assertFalse(CROISSANT.isSamePastry(editedCroissant));
    }

    @Test
    public void equals() {
        Pastry pastry = new PastryBuilder(CROISSANT).build();

        // same object -> returns true
        assertTrue(pastry.equals(pastry));

        // same values -> returns true
        Pastry pastryCopy = new PastryBuilder(CROISSANT).build();
        assertTrue(pastry.equals(pastryCopy));

        // null -> returns false
        assertFalse(pastry.equals(null));

        // different type -> returns false
        assertFalse(pastry.equals(5));

        // different pastry -> returns false
        assertFalse(pastry.equals(new PastryBuilder(BAGEL).build()));

        // same name but different price -> returns false
        assertFalse(pastry.equals(new PastryBuilder(CROISSANT).withPrice("0.10").build()));

        // different name but same price -> returns false
        assertFalse(pastry.equals(new PastryBuilder(CROISSANT).withName("Apple Pie").build()));
    }

    @Test
    public void toStringMethod() {
        String expected = Pastry.class.getCanonicalName()
                + "{name=" + CROISSANT.getName()
                + ", price=" + CROISSANT.getPrice()
                + "}";
        assertTrue(CROISSANT.toString().equals(expected));
    }
}
