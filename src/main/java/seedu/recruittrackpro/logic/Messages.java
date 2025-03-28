package seedu.recruittrackpro.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.recruittrackpro.logic.parser.Prefix;
import seedu.recruittrackpro.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Returns an error message indicating the invalid prefixes used and the allowed prefixes.
     *
     * @param allowedPrefixes The prefixes that are permitted.
     * @param invalidPrefixes The prefixes that were found but are not allowed.
     * @return A formatted error message indicating which prefixes are allowed and which ones are invalid.
     */
    public static String getErrorMessageForInvalidPrefixes(Prefix[] allowedPrefixes, Prefix[] invalidPrefixes) {
        assert invalidPrefixes.length > 0;

        Set<String> invalidFields = Stream.of(invalidPrefixes)
                .map(Prefix::toString)
                .collect(Collectors.toSet());

        Set<String> allowedFields = Stream.of(allowedPrefixes)
                .map(Prefix::toString)
                .collect(Collectors.toSet());

        return "Invalid prefixes detected: " + String.join(" ", invalidFields)
                + ". Only the following prefixes are allowed: " + String.join(" ", allowedFields) + ".";
    }


    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Comments: ")
                .append(person.getComment())
                .append("; Tags: ");
        person.getTags().toStream().forEach(builder::append);
        return builder.toString();
    }

}
