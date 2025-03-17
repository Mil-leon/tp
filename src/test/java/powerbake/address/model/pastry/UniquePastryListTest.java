package powerbake.address.model.pastry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.testutil.TypicalPastries.BAGEL;
import static powerbake.address.testutil.TypicalPastries.CAKE;
import static powerbake.address.testutil.TypicalPastries.CROISSANT;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import powerbake.address.model.pastry.exceptions.DuplicatePastryException;
import powerbake.address.model.pastry.exceptions.PastryNotFoundException;
import powerbake.address.testutil.PastryBuilder;

public class UniquePastryListTest {

    private UniquePastryList uniquePastryList;

    @BeforeEach
    public void setUp() {
        uniquePastryList = new UniquePastryList();
    }

    @Test
    public void contains_nullPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePastryList.contains(null));
    }

    @Test
    public void contains_pastryNotInList_returnsFalse() {
        assertFalse(uniquePastryList.contains(CROISSANT));
    }

    @Test
    public void contains_pastryInList_returnsTrue() {
        uniquePastryList.add(CROISSANT);
        assertTrue(uniquePastryList.contains(CROISSANT));
    }

    @Test
    public void contains_pastryWithSameIdentityFieldsInList_returnsTrue() {
        uniquePastryList.add(CROISSANT);
        Pastry editedCroissant = new PastryBuilder()
                .withName("Croissant")
                .withPrice("4.50")
                .build();
        assertTrue(uniquePastryList.contains(editedCroissant));
    }

    @Test
    public void add_nullPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePastryList.add(null));
    }

    @Test
    public void add_duplicatePastry_throwsDuplicatePastryException() {
        uniquePastryList.add(CROISSANT);
        assertThrows(DuplicatePastryException.class, () -> uniquePastryList.add(CROISSANT));
    }

    @Test
    public void add_validPastry_success() {
        uniquePastryList.add(CROISSANT);
        assertTrue(uniquePastryList.contains(CROISSANT));
    }

    @Test
    public void setPastry_nullTargetPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePastryList.setPastry(null, CROISSANT));
    }

    @Test
    public void setPastry_nullEditedPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePastryList.setPastry(CROISSANT, null));
    }

    @Test
    public void setPastry_targetPastryNotInList_throwsPastryNotFoundException() {
        assertThrows(PastryNotFoundException.class, () -> uniquePastryList.setPastry(CROISSANT, CROISSANT));
    }

    @Test
    public void set_editedPastryHasSameIdentity_success() {
        uniquePastryList.add(CROISSANT);
        Pastry editedCroissant = new PastryBuilder()
                .withName("Croissant")
                .withPrice("4.50")
                .build();
        uniquePastryList.setPastry(CROISSANT, editedCroissant);
        assertTrue(uniquePastryList.contains(editedCroissant));
    }

    @Test
    public void set_editedPastryHasDifferentIdentityInList_throwsDuplicatePastryException() {
        uniquePastryList.add(CROISSANT);
        Pastry editedPastry = new PastryBuilder()
                .withName("Apple Pie")
                .withPrice("4.50")
                .build();
        uniquePastryList.add(editedPastry);
        assertThrows(DuplicatePastryException.class, () -> uniquePastryList.setPastry(CROISSANT, editedPastry));
    }

    @Test
    public void set_editedPastryHasDifferentIdentityNotInList_success() {
        uniquePastryList.add(CROISSANT);
        Pastry editedPastry = new PastryBuilder()
                .withName("Apple Pie")
                .withPrice("4.50")
                .build();
        uniquePastryList.setPastry(CROISSANT, editedPastry);
        assertTrue(uniquePastryList.contains(editedPastry));
    }

    @Test
    public void remove_nullPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePastryList.remove(null));
    }

    @Test
    public void remove_pastryNotInList_throwsPastryNotFoundException() {
        uniquePastryList.add(BAGEL);
        uniquePastryList.add(CAKE);
        assertThrows(PastryNotFoundException.class, () -> uniquePastryList.remove(CROISSANT));
    }

    @Test
    public void remove_existingPastry_success() {
        uniquePastryList.add(CROISSANT);
        uniquePastryList.remove(CROISSANT);
        assertFalse(uniquePastryList.contains(CROISSANT));
    }

    @Test
    public void setPastries_nullUniquePastryList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePastryList.setPastries((UniquePastryList) null));
    }

    @Test
    public void setPastries_validUniquePastryList_success() {
        UniquePastryList replacement = new UniquePastryList();
        replacement.add(BAGEL);
        replacement.add(CAKE);

        uniquePastryList.setPastries(replacement);
        assertEquals(uniquePastryList, replacement);
    }

    @Test
    public void setPastries_validList_success() {
        List<Pastry> pastryList = Arrays.asList(BAGEL, CAKE);
        uniquePastryList.setPastries(pastryList);

        UniquePastryList expectedUniquePastryList = new UniquePastryList();
        expectedUniquePastryList.add(BAGEL);
        expectedUniquePastryList.add(CAKE);

        assertEquals(expectedUniquePastryList, uniquePastryList);
    }

    @Test
    public void setPastries_duplicatePastry_throwsDuplicatePastryException() {
        List<Pastry> duplicatePastryList = Arrays.asList(BAGEL, BAGEL);
        assertThrows(DuplicatePastryException.class, () -> uniquePastryList.setPastries(duplicatePastryList));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                uniquePastryList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void equals() {
        UniquePastryList pastryList = new UniquePastryList();
        pastryList.add(CROISSANT);

        // same list -> returns true
        assertTrue(uniquePastryList.equals(uniquePastryList));

        // null -> returns false
        assertFalse(uniquePastryList.equals(null));

        // different type -> returns false
        assertFalse(uniquePastryList.equals(5));

        // different list -> returns false
        assertFalse(uniquePastryList.equals(pastryList));
    }
}
