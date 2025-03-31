package powerbake.address.ui.common;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import powerbake.address.commons.core.GuiSettings;
import powerbake.address.commons.core.LogsCenter;
import powerbake.address.logic.Logic;
import powerbake.address.logic.commands.CommandResult;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.logic.parser.exceptions.ParseException;
import powerbake.address.ui.UiPart;
import powerbake.address.ui.ordertab.OrderDetailsPanel;
import powerbake.address.ui.ordertab.OrderListPanel;
import powerbake.address.ui.pastrytab.PastryListPanel;
import powerbake.address.ui.persontab.PersonListPanel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private PastryListPanel pastryListPanel;
    private OrderListPanel orderListPanel;
    private OrderDetailsPanel orderDetailsPanel;

    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane pastryListPanelPlaceholder;

    @FXML
    private StackPane orderListPanelPlaceholder;

    @FXML
    private StackPane orderDetailsPanelPlaceholder;


    @FXML
    private AnchorPane statusbarPlaceholder;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab clientTab;

    @FXML
    private Tab pastryTab;

    @FXML
    private Tab orderTab;

    @FXML
    private SplitPane splitPane;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    public void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        pastryListPanel = new PastryListPanel(logic.getFilteredPastryList());
        pastryListPanelPlaceholder.getChildren().add(pastryListPanel.getRoot());

        orderListPanel = new OrderListPanel(logic.getFilteredOrderList());
        orderListPanelPlaceholder.getChildren().add(orderListPanel.getRoot());

        orderDetailsPanel = new OrderDetailsPanel();
        orderDetailsPanelPlaceholder.getChildren().add(orderDetailsPanel.getRoot());

        // binding to selected order
        orderDetailsPanel.bindToSelectedOrder(orderListPanel.getSelectedOrderProperty());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        AnchorPane.setTopAnchor(commandBox.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(commandBox.getRoot(), 0.0);
        AnchorPane.setLeftAnchor(commandBox.getRoot(), 0.0);
        AnchorPane.setRightAnchor(commandBox.getRoot(), 0.0);

        AnchorPane.setTopAnchor(resultDisplay.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(resultDisplay.getRoot(), 0.0);
        AnchorPane.setLeftAnchor(resultDisplay.getRoot(), 0.0);
        AnchorPane.setRightAnchor(resultDisplay.getRoot(), 0.0);

        AnchorPane.setTopAnchor(statusBarFooter.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(statusBarFooter.getRoot(), 0.0);
        AnchorPane.setLeftAnchor(statusBarFooter.getRoot(), 0.0);
        AnchorPane.setRightAnchor(statusBarFooter.getRoot(), 0.0);

        splitPane.setDividerPositions(0.4);
        SplitPane.setResizableWithParent(orderListPanelPlaceholder, false);
        SplitPane.setResizableWithParent(orderDetailsPanelPlaceholder, false);

    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    public PastryListPanel getPastryListPanel() {
        return pastryListPanel;
    }

    public OrderListPanel getOrderListPanel() {
        return orderListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see powerbake.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            // logic to switch tabs depending on the view command
            if (commandResult.isShowClient()) {
                tabPane.getSelectionModel().select(clientTab);
            } else if (commandResult.isShowPastry()) {
                tabPane.getSelectionModel().select(pastryTab);
            } else if (commandResult.isShowOrder()) {
                tabPane.getSelectionModel().select(orderTab);
                orderListPanel.selectList(commandResult.getOrderIndex());
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
