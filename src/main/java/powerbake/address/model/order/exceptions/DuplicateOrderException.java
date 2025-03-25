package powerbake.address.model.order.exceptions;

/**
 * Signals that the operation will result in duplicate Orders.
 */
public class DuplicateOrderException extends RuntimeException {
    public DuplicateOrderException() {
        super("Operation would result in duplicate orders");
    }
}
