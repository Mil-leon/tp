package powerbake.address.logic.commands;

import static powerbake.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import powerbake.address.logic.Messages;
import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.testutil.PastryBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddPastryCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPastry_success() {
        Pastry validPastry = new PastryBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPastry(validPastry);

        assertCommandSuccess(new AddPastryCommand(validPastry), model,
                String.format(AddPastryCommand.MESSAGE_SUCCESS, Messages.format(validPastry)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePastry_throwsCommandException() {
        Pastry pastryInList = model.getAddressBook().getPastryList().get(0);
        assertCommandFailure(new AddPastryCommand(pastryInList), model,
                AddPastryCommand.MESSAGE_DUPLICATE_PASTRY);
    }

}
