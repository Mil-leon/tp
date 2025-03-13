package powerbake.address.model.pastry;

import static java.util.Objects.requireNonNull;
import static powerbake.address.commons.util.AppUtil.checkArgument;

import powerbake.address.model.person.Phone;

/**
 * Represents a Pastry's price in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {
    public static final String MESSAGE_CONSTRAINTS =
        "Prices should only contain digits, such as 3, 6.5 or 123.57";
    public static final String VALIDATION_REGEX = "^\\d+(.\\d{1,2})?$";
    public final String amount;

    /**
     * Constructs a {@code Price}.
     *
     * @param amount A valid price.
     */
    public Price(String amount) {
        requireNonNull(amount);
        checkArgument(isValidPrice(amount), MESSAGE_CONSTRAINTS);
        this.amount = amount;
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Price otherPrice = (Price) other;
        return this.amount.equals(otherPrice.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }
}
