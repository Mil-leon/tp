package powerbake.address.model.pastry;

import static java.util.Objects.requireNonNull;
import static powerbake.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Pastry's price in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {
    public static final String MESSAGE_CONSTRAINTS =
        "Prices should only contain digits and up to 2 decimal points, such as 3, 12.5 or 0.99";
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
        this.amount = String.format("%.2f", Double.valueOf(amount));
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the value of the price as a double.
     */
    public double getValueOfAmount() {
        return Double.valueOf(amount);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Price)) {
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
