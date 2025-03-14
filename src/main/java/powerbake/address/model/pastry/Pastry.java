package powerbake.address.model.pastry;

import static powerbake.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import powerbake.address.commons.util.ToStringBuilder;

/**
 * Represents a Pastry in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Pastry {
    private final Name name;
    private final Price price;

    /**
     * Every field must be present and not null.
     */
    public Pastry(Name name, Price price) {
        requireAllNonNull(name, price);
        this.name = name;
        this.price = price;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    /**
     * Returns true if both pastries have the same name AND price.
     * This defines a weaker notion of equality between two pastries.
     */
    public boolean isSamePastry(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Pastry)) {
            return false;
        }

        Pastry otherPastry = (Pastry) other;
        return this.name.equals(otherPastry.name);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Pastry)) {
            return false;
        }

        Pastry otherPastry = (Pastry) other;
        return this.name.equals(otherPastry.name)
                && this.price.equals(otherPastry.price);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("price", price)
                .toString();
    }
}
