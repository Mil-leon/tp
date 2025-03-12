package powerbake.address.model.pastry;

import static java.util.Objects.requireNonNull;
import static powerbake.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import powerbake.address.model.pastry.exceptions.DuplicatePastryException;
import powerbake.address.model.pastry.exceptions.PastryNotFoundException;

/**
 * A list of pastries that enforces uniqueness between its elements and does not allow nulls.
 * A pastry is considered unique by comparing using {@code Pastry#isSamePastry(Pastry)}. As such, adding and updating of
 * pastries uses Pastry#isSamePastry(Pastry) for equality so as to ensure that the pastry being added or updated is
 * unique in terms of identity in the UniquePastryList. However, the removal of a pastry uses Pastry#equals(Object) so
 * as to ensure that the pastry with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Pastry#isSamePastry(Pastry)
 */
public class UniquePastryList implements Iterable<Pastry> {

    private final ObservableList<Pastry> internalList = FXCollections.observableArrayList();
    private final ObservableList<Pastry> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent pastry as the given argument.
     */
    public boolean contains(Pastry toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePastry);
    }

    /**
     * Adds a pastry to the list.
     * The pastry must not already exist in the list.
     */
    public void add(Pastry toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePastryException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the pastry {@code target} in the list with {@code editedPastry}.
     * {@code target} must exist in the list.
     * The pastry identity of {@code editedPastry} must not be the same as another existing pastry in the list.
     */
    public void setPastry(Pastry target, Pastry editedPastry) {
        requireAllNonNull(target, editedPastry);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PastryNotFoundException();
        }

        if (!target.isSamePastry(editedPastry) && contains(editedPastry)) {
            throw new DuplicatePastryException();
        }

        internalList.set(index, editedPastry);
    }

    /**
     * Removes the equivalent pastry from the list.
     * The pastry must exist in the list.
     */
    public void remove(Pastry toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PastryNotFoundException();
        }
    }

    public void setPastries(UniquePastryList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code pastries}.
     * {@code pastries} must not contain duplicate pastries.
     */
    public void setPastries(List<Pastry> pastries) {
        requireAllNonNull(pastries);
        if (!pastriesAreUnique(pastries)) {
            throw new DuplicatePastryException();
        }

        internalList.setAll(pastries);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Pastry> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Pastry> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniquePastryList)) {
            return false;
        }

        UniquePastryList otherUniquePastryList = (UniquePastryList) other;
        return internalList.equals(otherUniquePastryList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code pastries} contains only unique pastries.
     */
    private boolean pastriesAreUnique(List<Pastry> pastries) {
        for (int i = 0; i < pastries.size() - 1; i++) {
            for (int j = i + 1; j < pastries.size(); j++) {
                if (pastries.get(i).isSamePastry(pastries.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
