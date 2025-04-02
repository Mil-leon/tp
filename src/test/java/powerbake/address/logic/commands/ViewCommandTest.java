package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_ORDER;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ViewCommand.
 */
public class ViewCommandTest {
    private static final String CLIENT = "client";
    private static final String PASTRY = "pastry";
    private static final String ORDER = "order";
    private static final String MESSAGE_SUCCESS_CLIENT = String.format(ViewCommand.MESSAGE_SUCCESS, CLIENT);
    private static final String MESSAGE_SUCCESS_PASTRY = String.format(ViewCommand.MESSAGE_SUCCESS, PASTRY);
    private static final String MESSAGE_SUCCESS_ORDER = String.format(ViewCommand.MESSAGE_SUCCESS, ORDER);
    private static final String MESSAGE_SUCCESS_INDEX =
            String.format(ViewCommand.MESSAGE_SUCCESS_INDEX, ORDER, INDEX_FIRST_ORDER.getOneBased());

    private Model model;
    private Model expectedModel;



    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_clientListNotFiltered_showsSameList() {
        assertCommandSuccess(new ViewCommand(CLIENT), model, MESSAGE_SUCCESS_CLIENT, expectedModel);
    }

    @Test
    public void execute_pastryListNotFiltered_showsSameList() {
        assertCommandSuccess(new ViewCommand(PASTRY), model, MESSAGE_SUCCESS_PASTRY, expectedModel);
    }

    @Test
    public void execute_orderListNotFiltered_showsSameList() {
        assertCommandSuccess(new ViewCommand(ORDER), model, MESSAGE_SUCCESS_ORDER, expectedModel);
        assertCommandSuccess(new ViewCommand(INDEX_FIRST_PERSON), model, MESSAGE_SUCCESS_INDEX, expectedModel);
    }

    @Test
    public void execute_clientListFiltered_showsFullList() {
        // changes model state to filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // testing if view command goes back to showing full list
        assertCommandSuccess(new ViewCommand(CLIENT), model, MESSAGE_SUCCESS_CLIENT, expectedModel);
    }

    @Test
    public void execute_invalidType_throwsCommandException() {
        assertCommandFailure(new ViewCommand("apple"), model, ViewCommand.MESSAGE_USAGE);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        assertCommandFailure(new ViewCommand(Index.fromOneBased(9999)), model, MESSAGE_INVALID_ORDER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ViewCommand viewClientCommand = new ViewCommand(CLIENT);
        ViewCommand viewPastryCommand = new ViewCommand(PASTRY);
        ViewCommand viewOrderCommand = new ViewCommand(ORDER);
        ViewCommand viewOrderCommand2 = new ViewCommand(INDEX_FIRST_PERSON);
        Index test = null;
        ViewCommand viewOrderCommand3 = new ViewCommand(test);

        // same object -> returns true
        assertTrue(viewClientCommand.equals(viewClientCommand));

        // same values -> returns true
        ViewCommand viewClientCommandCopy = new ViewCommand(CLIENT);
        assertTrue(viewClientCommand.equals(viewClientCommandCopy));

        // different command types -> returns false
        assertFalse(viewClientCommand.equals(viewPastryCommand));
        assertFalse(viewClientCommand.equals(viewOrderCommand));
        assertFalse(viewClientCommand.equals(viewOrderCommand2));
        assertFalse(viewPastryCommand.equals(viewOrderCommand));
        assertFalse(viewPastryCommand.equals(viewOrderCommand2));
        assertFalse(viewOrderCommand.equals(viewOrderCommand2));

        // different types -> returns false
        assertFalse(viewClientCommand.equals(1));

        // null -> returns false
        assertFalse(viewClientCommand.equals(null));

        // null = null -> returns true
        assertTrue(viewOrderCommand3.equals(viewOrderCommand3));
    }

    @Test
    public void toStringMethod() {
        // Test for the "client" type
        ViewCommand clientCommand = new ViewCommand(CLIENT);
        String expectedClientString = ViewCommand.class.getCanonicalName() + "{type=client, index=null}";
        assertEquals(expectedClientString, clientCommand.toString());

        // Test for the "pastry" type
        ViewCommand pastryCommand = new ViewCommand(PASTRY);
        String expectedPastryString = ViewCommand.class.getCanonicalName() + "{type=pastry, index=null}";
        assertEquals(expectedPastryString, pastryCommand.toString());

        // Test for the "order" type
        ViewCommand orderCommand = new ViewCommand(ORDER);
        String expectedOrderString = ViewCommand.class.getCanonicalName() + "{type=order, index=null}";
        assertEquals(expectedOrderString, orderCommand.toString());

        // Test for the "order" type with an index
        ViewCommand orderCommand2 = new ViewCommand(INDEX_FIRST_PERSON);
        String expectedOrderString2 = ViewCommand.class.getCanonicalName()
                + "{type=order, index=" + INDEX_FIRST_PERSON + "}";
        assertEquals(expectedOrderString2, orderCommand2.toString());
    }
}
