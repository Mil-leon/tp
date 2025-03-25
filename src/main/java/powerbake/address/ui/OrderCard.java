package powerbake.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderStatus;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class OrderCard extends UiPart<Region> {

    private static final String FXML = "OrderListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Order order;

    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label client;
    @FXML
    private Label status;

    /**
     * Creates a {@code OrderCard} with the given {@code Order} and index to display.
     */
    public OrderCard(Order order, int displayedIndex) {
        super(FXML);
        this.order = order;
        id.setText(displayedIndex + ". ");
        name.setText(String.format(
            "Order #%s", 
            order.getOrderId().toString().substring(0,8)));
        client.setText(order.getCustomer().getName().fullName);
        
        OrderStatus orderStatus = order.getStatus();
        status.setText(orderStatus.toString().toUpperCase());
        setStatusColor(orderStatus);
    }

    /**
     * Sets color of status label, depending on order status. 
     */
    private void setStatusColor(OrderStatus orderStatus) {
        status.getStyleClass().clear();
        status.getStyleClass().add("status-box");
        switch (orderStatus) {
        case PENDING:
            status.getStyleClass().addAll("pending");
            break;
        case PROCESSING:
            status.getStyleClass().addAll("processing");
        break;
        case READY:
            status.getStyleClass().addAll("ready");
            break;
        case DELIVERED:
            status.getStyleClass().addAll("delivered");
            break;
        case CANCELLED:
            status.getStyleClass().addAll("cancelled");
            break;
        default:
            status.getStyleClass().addAll("pending");
        }
    }
}
