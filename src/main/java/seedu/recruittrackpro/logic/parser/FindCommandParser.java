package seedu.recruittrackpro.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.recruittrackpro.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.stream.Stream;

import seedu.recruittrackpro.logic.commands.FindCommand;
import seedu.recruittrackpro.logic.parser.exceptions.ParseException;
import seedu.recruittrackpro.logic.predicates.ContainsKeywordsPredicate;
import seedu.recruittrackpro.model.person.Address;
import seedu.recruittrackpro.model.person.Email;
import seedu.recruittrackpro.model.person.Name;
import seedu.recruittrackpro.model.person.Phone;
import seedu.recruittrackpro.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE);

        if (!hasAtLeastOnePrefixPresent(argMultimap,
                PREFIX_NAME, PREFIX_TAG, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Object[] nameKeywords = getKeywords(argMultimap, PREFIX_NAME);
        Object[] tagKeywords = getKeywords(argMultimap, PREFIX_TAG);
        Object[] addressKeywords = getKeywords(argMultimap, PREFIX_ADDRESS);
        Object[] emailKeywords = getKeywords(argMultimap, PREFIX_EMAIL);
        Object[] phoneKeywords = getKeywords(argMultimap, PREFIX_PHONE);

        return new FindCommand(new ContainsKeywordsPredicate(nameKeywords,
                tagKeywords, addressKeywords, emailKeywords, phoneKeywords));
    }

    /**
     * Returns true if at least one of the prefixes does not contain empty {@code Optional} values.
     */
    public boolean hasAtLeastOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix ... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private boolean containsPrefix(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }

    private Object[] getKeywords(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        Object[] keywordsArray = {prefix, new String[0]};

        if (containsPrefix(argMultimap, prefix)) {
            argMultimap.verifyNoDuplicatePrefixesFor(prefix);
            String keywords = argMultimap.getValue(prefix).orElse("");

            if (keywords.isEmpty()) {
                switch (prefix.toString()) {
                case "n/":
                    throw new ParseException(Name.MESSAGE_CONSTRAINTS);
                case "t/":
                    throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
                case "a/":
                    throw new ParseException(Address.MESSAGE_CONSTRAINTS);
                case "e/":
                    throw new ParseException(Email.MESSAGE_CONSTRAINTS);
                case "p/":
                    throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
                default:
                    break;
                }
            }

            keywordsArray[1] = keywords.split("\\s+");
        }

        return keywordsArray;
    }

}
