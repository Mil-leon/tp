package powerbake.address.model.order;

import java.util.Objects;

import powerbake.address.commons.util.CollectionUtil;
import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.model.pastry.Pastry;

/**
 * Represents an item within an Order, consisting of a pastry and its quantity.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OrderItem {
    private final Pastry pastry;
    private final int quantity;

    /**
     * Creates an OrderItem with the specified pastry and quantity.
     */
    public OrderItem(Pastry pastry, int quantity) {
        CollectionUtil.requireAllNonNull(pastry);
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.pastry = pastry;
        this.quantity = quantity;
    }

    public Pastry getPastry() {
        return pastry;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculates the subtotal for this order item (price × quantity).
     */
    public double getSubtotal() {
        return pastry.getPrice().getValueOfAmount() * quantity;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof OrderItem)) {
            return false;
        }

        OrderItem otherItem = (OrderItem) other;
        return pastry.equals(otherItem.pastry)
                && quantity == otherItem.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pastry, quantity);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("pastry", pastry.getName())
                .add("quantity", quantity)
                .add("subtotal", getSubtotal())
                .toString();
    }
}
