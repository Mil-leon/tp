package powerbake.address.ui.ordertab;

import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import powerbake.address.commons.core.LogsCenter;
import powerbake.address.model.order.Order;
import powerbake.address.ui.UiPart;
import powerbake.address.ui.pastrytab.PastryListPanel;

/**
 * Panel containing the list of pastries.
 */
public class OrderListPanel extends UiPart<Region> {
    private static final String FXML = "OrderListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PastryListPanel.class);

    @FXML
    private ListView<Order> orderListView;

    private final ObjectProperty<Order> selectedOrderProperty = new SimpleObjectProperty<>();

    /**
     * Creates a {@code OrderListPanel} with the given {@code ObservableList}.
     */
    public OrderListPanel(ObservableList<Order> orderList) {
        super(FXML);
        orderListView.setItems(orderList);
        orderListView.setCellFactory(listView -> new OrderListViewCell());

        // Bind selected order to property
        orderListView.getSelectionModel().selectedItemProperty().addListener((observable, oldOrder, newOrder) -> {
            selectedOrderProperty.set(newOrder);
        });
    }

    public ObjectProperty<Order> getSelectedOrderProperty() {
        return selectedOrderProperty;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Order} using a {@code OrderCard}.
     */
    class OrderListViewCell extends ListCell<Order> {
        @Override
        protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);

            if (empty || order == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new OrderCard(order, getIndex() + 1).getRoot());
            }
        }
    }
}
