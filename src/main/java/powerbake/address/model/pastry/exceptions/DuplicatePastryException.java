package powerbake.address.model.pastry.exceptions;

/**
 * Signals that the operation will result in duplicate Pastries
 * (Pastries are considered duplicates if they have the same identity).
 */
public class DuplicatePastryException extends RuntimeException {
    public DuplicatePastryException() {
        super("Operation would result in duplicate pastries");
    }
}
