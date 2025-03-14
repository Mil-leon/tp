package powerbake.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import powerbake.address.model.pastry.Pastry;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PastryCard extends UiPart<Region> {

    private static final String FXML = "PastryListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Pastry pastry;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label price;

    /**
     * Creates a {@code PastryCard} with the given {@code Pastry} and index to display.
     */
    public PastryCard(Pastry pastry, int displayedIndex) {
        super(FXML);
        this.pastry = pastry;
        id.setText(displayedIndex + ". ");
        name.setText(pastry.getName().name);
        price.setText(pastry.getPrice().amount);
    }
}
