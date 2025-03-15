package seedu.recruittrackpro.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.recruittrackpro.commons.util.StringUtil;
import seedu.recruittrackpro.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final boolean shouldContainAll;

    /**
     * Constructs a  {@code NameContainsKeywordsPredicate} with the specified options.
     */
    public NameContainsKeywordsPredicate(List<String> keywords, boolean shouldContainAll) {
        this.keywords = keywords;
        this.shouldContainAll = shouldContainAll;
    }

    @Override
    public boolean test(Person person) {
        if (shouldContainAll) {
            return keywords.stream()
                    .allMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
        }
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
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
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords)
                && (shouldContainAll == otherNameContainsKeywordsPredicate.shouldContainAll);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .add("shouldContainAll", shouldContainAll).toString();
    }
}
