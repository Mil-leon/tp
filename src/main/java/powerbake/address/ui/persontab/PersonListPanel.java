package powerbake.address.ui.persontab;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import powerbake.address.commons.core.LogsCenter;
import powerbake.address.logic.Logic;
import powerbake.address.model.person.Person;
import powerbake.address.ui.UiPart;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private static final String CSS_BORDER = "-fx-border-width: 1.5px 1.5px 1.5px 1.5px;";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(Logic logic) {
        super(FXML);
        this.logic = logic;

        ObservableList<Person> personList = logic.getFilteredPersonList();
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            int originalIndex = logic.getOriginalIndex(person);
            setGraphic(new PersonCard(person, originalIndex + 1).getRoot());
            if (getIndex() == 0) {
                setStyle(CSS_BORDER);
            }
        }
    }

}
