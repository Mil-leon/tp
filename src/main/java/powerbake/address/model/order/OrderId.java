package powerbake.address.model.order;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique identifier for an Order.
 * Guarantees: immutable; is valid as declared in {@link #isValidOrderId(String)}
 */
public class OrderId {
    public static final String MESSAGE_CONSTRAINTS = "Order IDs should be valid UUID strings";

    private final String id;

    /**
     * Constructs an OrderId with a new random UUID.
     */
    public OrderId() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constructs an OrderId with the specified ID.
     *
     * @param id A valid order ID.
     */
    public OrderId(String id) {
        if (!isValidOrderId(id)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.id = id;
    }

    /**
     * Returns true if a given string is a valid order ID.
     */
    public static boolean isValidOrderId(String test) {
        try {
            UUID.fromString(test);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof OrderId)) {
            return false;
        }

        OrderId otherId = (OrderId) other;
        return id.equals(otherId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
