package powerbake.address.ui.ordertab;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import powerbake.address.commons.core.LogsCenter;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.ui.UiPart;

/**
 * Panel containing the details of the selected order.
 */
public class OrderDetailsPanel extends UiPart<Region> {

    private static final String FXML = "OrderDetailsPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(OrderDetailsPanel.class);

    @FXML
    private Label selectedOrderIdLabel;

    @FXML
    private Label clientNameLabel;

    @FXML
    private Label orderDateTimeLabel;

    @FXML
    private ListView<OrderItem> itemsListView;

    @FXML
    private Label orderStatusLabel;

    @FXML
    private Label totalPriceLabel;

    /**
     * Creates a {@code OrderDetailsPanel}.
     */
    public OrderDetailsPanel() {
        super(FXML);
        itemsListView.setCellFactory(listView -> new OrderItemCell());
    }

    /**
     * Custom {@code ListCell} that displays {@code OrderItem}.
     */
    class OrderItemCell extends ListCell<OrderItem> {
        @Override
        protected void updateItem(OrderItem orderItem, boolean empty) {
            super.updateItem(orderItem, empty);

            if (empty || orderItem == null) {
                setGraphic(null);
                setText(null);
            } else {
                setText(
                        orderItem.getQuantity() + " x "
                        + orderItem.getPastry().getName().name
                        + " - $"
                        + String.format("%.2f", orderItem.getSubtotal()));
            }
        }
    }

    /**
     * Binds the labels and list view to the selected order property.
     *
     * @param selectedOrderProperty
     */
    public void bindToSelectedOrder(ObjectProperty<Order> selectedOrderProperty) {
        selectedOrderIdLabel.textProperty().bind(
                Bindings.createStringBinding(() -> selectedOrderProperty.get() == null
                        ? ""
                        : "Order ID: " + selectedOrderProperty.get().getOrderId().toString(),
                        selectedOrderProperty)
        );

        clientNameLabel.textProperty().bind(
                Bindings.createStringBinding(() -> selectedOrderProperty.get() == null
                        ? ""
                        : "Client: " + selectedOrderProperty.get().getCustomer().getName().fullName,
                        selectedOrderProperty)
        );

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("EEE, d MMM yyyy hh:mm a")
                .withLocale(Locale.US);
        orderDateTimeLabel.textProperty().bind(
                Bindings.createStringBinding(() -> selectedOrderProperty.get() == null
                        ? ""
                        : "Date: " + selectedOrderProperty.get().getOrderDate().format(dateTimeFormatter),
                        selectedOrderProperty)
        );

        orderStatusLabel.textProperty().bind(
                Bindings.createStringBinding(() -> selectedOrderProperty.get() == null
                        ? ""
                        : selectedOrderProperty.get().getStatus().toString().toUpperCase(),
                        selectedOrderProperty)
        );

        totalPriceLabel.textProperty().bind(
                Bindings.createStringBinding(() -> selectedOrderProperty.get() == null
                            ? ""
                            : "Total: $" + String.format("%.2f", selectedOrderProperty.get().getTotalPrice()),
                            selectedOrderProperty)
        );

        selectedOrderProperty.addListener((obs, oldOrder, newOrder) -> {
            if (newOrder != null) {
                itemsListView.getItems().setAll(newOrder.getOrderItems());
                setStatusColor(newOrder.getStatus());
            } else {
                itemsListView.getItems().clear();
                orderStatusLabel.getStyleClass().clear();
                orderStatusLabel.getStyleClass().add("status-box");
            }
        });
    }

    /**
     * Sets color of status label, depending on order status.
     */
    private void setStatusColor(OrderStatus orderStatus) {
        orderStatusLabel.getStyleClass().clear();
        orderStatusLabel.getStyleClass().add("status-box");
        switch (orderStatus) {
        case PENDING:
            orderStatusLabel.getStyleClass().addAll("pending");
            break;
        case PROCESSING:
            orderStatusLabel.getStyleClass().addAll("processing");
            break;
        case READY:
            orderStatusLabel.getStyleClass().addAll("ready");
            break;
        case DELIVERED:
            orderStatusLabel.getStyleClass().addAll("delivered");
            break;
        case CANCELLED:
            orderStatusLabel.getStyleClass().addAll("cancelled");
            break;
        default:
            orderStatusLabel.getStyleClass().addAll("pending");
        }
    }
}
