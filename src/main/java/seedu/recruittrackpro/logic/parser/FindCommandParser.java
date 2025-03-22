package seedu.recruittrackpro.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.recruittrackpro.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
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

        Object[] nameKeywords = getNameKeywords(argMultimap);
        Object[] tagKeywords = getTagKeywords(argMultimap);
        Object[] addressKeywords = getAddressKeywords(argMultimap);
        Object[] emailKeywords = getEmailKeywords(argMultimap);
        Object[] phoneKeywords = getPhoneKeywords(argMultimap);

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

    private Object[] getNameKeywords(ArgumentMultimap argumentMultimap) throws ParseException {
        Object[] nameKeywordArray = {PREFIX_NAME, new String[0]};

        if (containsPrefix(argumentMultimap, PREFIX_NAME)) {
            argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);
            String name = argumentMultimap.getValue(PREFIX_NAME).orElse("");

            if (name.isEmpty()) {
                throw new ParseException(Name.MESSAGE_CONSTRAINTS);
            }

            nameKeywordArray[1] = name.split("\\s+");
        }

        return nameKeywordArray;
    }

    private Object[] getTagKeywords(ArgumentMultimap argumentMultimap) throws ParseException {
        Object[] tagKeywordArray = {PREFIX_TAG, new String[0]};

        if (containsPrefix(argumentMultimap, PREFIX_TAG)) {
            Set<Tag> tagList = ParserUtil.parseTags(argumentMultimap.getAllValues(PREFIX_TAG));
            tagKeywordArray[1] = tagList.stream().map(tag -> tag.tagName).toArray(String[]::new);
        }
        return tagKeywordArray;
    }

    private Object[] getAddressKeywords(ArgumentMultimap argumentMultimap) throws ParseException {
        Object[] addressKeywordArray = {PREFIX_ADDRESS, new String[0]};

        if (containsPrefix(argumentMultimap, PREFIX_ADDRESS)) {
            String address = argumentMultimap.getValue(PREFIX_ADDRESS).orElse("");

            if (address.isEmpty()) {
                throw new ParseException(Address.MESSAGE_CONSTRAINTS);
            }

            addressKeywordArray[1] = address.split("\\s+");
        }
        return addressKeywordArray;
    }

    private Object[] getEmailKeywords(ArgumentMultimap argumentMultimap) throws ParseException {
        Object[] emailKeywordArray = {PREFIX_EMAIL, new String[0]};

        if (containsPrefix(argumentMultimap, PREFIX_EMAIL)) {
            String email = argumentMultimap.getValue(PREFIX_EMAIL).orElse("");

            if (email.isEmpty()) {
                throw new ParseException(Email.MESSAGE_CONSTRAINTS);
            }

            emailKeywordArray[1] = email.split("\\s+");

        }
        return emailKeywordArray;
    }

    private Object[] getPhoneKeywords(ArgumentMultimap argumentMultimap) throws ParseException {
        Object[] phoneKeywordArray = {PREFIX_PHONE, new String[0]};

        if (containsPrefix(argumentMultimap, PREFIX_PHONE)) {
            String phone = argumentMultimap.getValue(PREFIX_PHONE).orElse("");

            if (phone.isEmpty()) {
                throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
            }

            phoneKeywordArray[1] = phone.split("\\s+");
        }

        return phoneKeywordArray;
    }


}
