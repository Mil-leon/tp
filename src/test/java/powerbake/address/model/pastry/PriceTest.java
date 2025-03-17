package powerbake.address.model.pastry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PriceTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Price(null));
    }

    @Test
    public void constructor_invalidPrice_throwsIllegalArgumentException() {
        String invalidAmount = "";
        assertThrows(IllegalArgumentException.class, () -> new Price(invalidAmount));
    }

    @Test
    public void isValidPrice() {
        // null amount
        assertThrows(NullPointerException.class, () -> Price.isValidPrice(null));

        // invalid amounts
        assertFalse(Price.isValidPrice("")); // empty string
        assertFalse(Price.isValidPrice(" ")); // spaces only
        assertFalse(Price.isValidPrice("price")); // non-numeric
        assertFalse(Price.isValidPrice("9011p041")); // alphabets within digits
        assertFalse(Price.isValidPrice("9312 1534")); // spaces within digits
        assertFalse(Price.isValidPrice(".54")); // less than 1 number before decimal point
        assertFalse(Price.isValidPrice("1.5.4")); // more than 1 decimal point
        assertFalse(Price.isValidPrice("1.543")); // more than 2 decimal places

        // valid amounts
        assertTrue(Price.isValidPrice("911")); // number without decimal
        assertTrue(Price.isValidPrice("93121534")); // long number without decimal
        assertTrue(Price.isValidPrice("6.5")); // numbe with 1 decimal
        assertTrue(Price.isValidPrice("123.57")); // number with 2 decimal places
        assertTrue(Price.isValidPrice("124293843.57")); // long number with 2 decimal places
    }

    @Test
    public void equals() {
        Price price = new Price("9.70");
        
        // same object -> returns true
        assertTrue(price.equals(price));

        // same values -> returns true
        assertTrue(price.equals(new Price("9.70")));

        // null value -> returns false
        assertFalse(price.equals(null));
        
        // different types -> returns false
        assertFalse(price.equals(5.0f));

        // different values -> returns false
        assertFalse(price.equals(new Price("9.71")));
    }
}
