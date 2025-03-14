package powerbake.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import powerbake.address.commons.core.LogsCenter;
import powerbake.address.model.pastry.Pastry;

/**
 * Panel containing the list of pastries.
 */
public class PastryListPanel extends UiPart<Region> {
    private static final String FXML = "PastryListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PastryListPanel.class);

    @FXML
    private ListView<Pastry> pastryListView;

    /**
     * Creates a {@code PastryListPanel} with the given {@code ObservableList}.
     */
    public PastryListPanel(ObservableList<Pastry> pastryList) {
        super(FXML);
        pastryListView.setItems(pastryList);
        pastryListView.setCellFactory(listView -> new PastryListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Pastry} using a {@code PastryCard}.
     */
    class PastryListViewCell extends ListCell<Pastry> {
        @Override
        protected void updateItem(Pastry pastry, boolean empty) {
            super.updateItem(pastry, empty);

            if (empty || pastry == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PastryCard(pastry, getIndex() + 1).getRoot());
            }
        }
    }

}
