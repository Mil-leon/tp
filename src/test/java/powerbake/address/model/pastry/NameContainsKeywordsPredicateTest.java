package powerbake.address.model.pastry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import powerbake.address.testutil.PastryBuilder;

public class NameContainsKeywordsPredicateTest {
    @Test
    public void equals() {

        List<String> firstKeywordList = Collections.singletonList("first");
        List<String> secondKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondKeywordList);

        // same object -> return true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> return true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> return false
        assertFalse(firstPredicate.equals(firstKeywordList));

        // null -> return false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> return false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("Croissant"));
        assertTrue(predicate.test(new PastryBuilder().withName("croissant").build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("croissant", "bagel"));
        assertTrue(predicate.test(new PastryBuilder().withName("Croissant Bagel").build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("croissant", "bagel"));
        assertTrue(predicate.test(new PastryBuilder().withName("Bagel cake").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("cROIssAnT", "cAke", "BAGeL"));
        assertTrue(predicate.test(new PastryBuilder().withName("Chocolate Croissant Cake").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PastryBuilder().withName("Croissant").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("cake", "bagel"));
        assertFalse(predicate.test(new PastryBuilder().withName("Croissant").build()));

        // Keywords match price, but does not match name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("3.50"));
        assertFalse(predicate.test(new PastryBuilder().withName("Croissant").withPrice("3.50").build()));
    }

    @Test
    public void test_toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
