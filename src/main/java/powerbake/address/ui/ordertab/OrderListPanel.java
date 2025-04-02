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
import powerbake.address.logic.Logic;
import powerbake.address.model.order.Order;
import powerbake.address.ui.UiPart;
import powerbake.address.ui.pastrytab.PastryListPanel;

/**
 * Panel containing the list of pastries.
 */
public class OrderListPanel extends UiPart<Region> {
    private static final String FXML = "OrderListPanel.fxml";
    private static final String CSS_BORDER = "-fx-border-width: 1px 1px 1px 1px;";
    private final Logger logger = LogsCenter.getLogger(PastryListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Order> orderListView;

    private final ObjectProperty<Order> selectedOrderProperty = new SimpleObjectProperty<>();

    /**
     * Creates a {@code OrderListPanel} with the given {@code ObservableList}.
     */
    public OrderListPanel(Logic logic) {
        super(FXML);
        this.logic = logic;

        ObservableList<Order> orderList = logic.getFilteredOrderList();
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

    public void selectList(int index) {
        orderListView.getSelectionModel().select(index);
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
                return;
            }

            int originalIndex = logic.getOriginalIndex(order);
            setGraphic(new OrderCard(order, originalIndex + 1).getRoot());
            setPrefHeight(70.0);
            if (getIndex() == 0) {
                setStyle(CSS_BORDER);
            }
        }
    }

}
