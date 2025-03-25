package powerbake.address.model.order;

/**
 * Represents the status of an Order in the PowerBake system.
 */
public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    READY("Ready for delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
