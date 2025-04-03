package powerbake.address.model.order;

import java.util.List;
import java.util.function.Predicate;

import powerbake.address.commons.util.StringUtil;
import powerbake.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Order}'s Customers {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Order> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Order order) {
        return keywords.stream()
                .anyMatch(keyword
                        -> StringUtil.containsWordIgnoreCase(order.getCustomer().getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
