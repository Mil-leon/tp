package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.commands.CommandTestUtil.DESC_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.DESC_CINNAMONROLL;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PRICE_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PRICE_CINNAMONROLL;

import org.junit.jupiter.api.Test;

import powerbake.address.logic.commands.EditCommand.EditPastryDescriptor;
import powerbake.address.testutil.EditPastryDescriptorBuilder;

public class EditPastryDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPastryDescriptor descriptorWithSameValues = new EditPastryDescriptor(DESC_CHOCOLATECROISSANT);
        assertTrue(DESC_CHOCOLATECROISSANT.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_CINNAMONROLL.equals(DESC_CINNAMONROLL));

        // null -> returns false
        assertFalse(DESC_CINNAMONROLL.equals(null));

        // different types -> returns false
        assertFalse(DESC_CINNAMONROLL.equals(5));

        // different values -> returns false
        assertFalse(DESC_CINNAMONROLL.equals(DESC_CHOCOLATECROISSANT));

        // different name -> returns false
        EditPastryDescriptor editedCinnamonroll = new EditPastryDescriptorBuilder(DESC_CINNAMONROLL)
                .withName(VALID_NAME_CHOCOLATECROISSANT).build();

        assertFalse(DESC_CINNAMONROLL.equals(editedCinnamonroll));

        // different phone -> returns false
        editedCinnamonroll = new EditPastryDescriptorBuilder(DESC_CINNAMONROLL)
                .withPrice(VALID_PRICE_CHOCOLATECROISSANT).build();

        assertFalse(DESC_CINNAMONROLL.equals(editedCinnamonroll));

        // different email -> returns false
        editedCinnamonroll = new EditPastryDescriptorBuilder(DESC_CHOCOLATECROISSANT)
                .withPrice(VALID_PRICE_CINNAMONROLL).build();

        assertFalse(DESC_CHOCOLATECROISSANT.equals(editedCinnamonroll));
    }

    @Test
    public void toStringMethod() {
        EditPastryDescriptor editPastryDescriptor = new EditPastryDescriptor();
        String expected = EditPastryDescriptor.class.getCanonicalName() + "{name="
                + editPastryDescriptor.getName().orElse(null) + ", price="
                + editPastryDescriptor.getPrice().orElse(null) + "}";
        assertEquals(expected, editPastryDescriptor.toString());
    }
}
