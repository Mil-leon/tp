package powerbake.address.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import powerbake.address.testutil.OrderBuilder;
import powerbake.address.testutil.PersonBuilder;

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
                Collections.singletonList("Bob"));
        assertTrue(predicate.test(new OrderBuilder().withCustomer(BOB).build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Bob", "Choo"));
        assertTrue(predicate.test(new OrderBuilder().withCustomer(BOB).build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Bob", "Choo"));
        assertTrue(predicate.test(new OrderBuilder().withCustomer(
                new PersonBuilder(BOB).withName("Bob New").build()).build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("pERson", "bOb"));
        assertTrue(predicate.test(new OrderBuilder().withCustomer(
            new PersonBuilder(BOB).withName("Bob New Person").build()).build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new OrderBuilder().withCustomer(BOB).build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("cake", "bagel"));
        assertFalse(predicate.test(new OrderBuilder().withCustomer(BOB).build()));
    }

    @Test
    public void test_toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
