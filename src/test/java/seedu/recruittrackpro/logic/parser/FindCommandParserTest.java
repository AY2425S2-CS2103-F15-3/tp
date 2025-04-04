package seedu.recruittrackpro.logic.parser;

import static seedu.recruittrackpro.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recruittrackpro.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_COMMENT;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.recruittrackpro.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.recruittrackpro.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.recruittrackpro.logic.commands.FindCommand;
import seedu.recruittrackpro.logic.predicates.ContainsKeywordsPredicate;
import seedu.recruittrackpro.model.person.Address;
import seedu.recruittrackpro.model.person.Email;
import seedu.recruittrackpro.model.person.Name;
import seedu.recruittrackpro.model.person.Phone;
import seedu.recruittrackpro.model.tag.Tag;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_invalidFormat_throwsParseException() {
        // missing arguments
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // blank argument
        assertParseFailure(parser, " " + PREFIX_NAME, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " " + PREFIX_TAG, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " " + PREFIX_ADDRESS, Address.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " " + PREFIX_EMAIL, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " " + PREFIX_PHONE, Phone.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + " " + PREFIX_NAME + "Alice Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // containsAll preamble
        Object[] nameKeywords = {PREFIX_NAME, new String[]{"Bernice", "Yu"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(true, nameKeywords));
        assertParseSuccess(parser, "--contain-all " + PREFIX_NAME + "Bernice Yu",
                expectedFindCommand);
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Object[] nameKeywords = {PREFIX_NAME, new String[]{"Alice", "Bob"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(false, nameKeywords));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_NAME + " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Object[] tagKeywords = {PREFIX_TAG, new String[]{"friend", "neighbour"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(false, tagKeywords));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend neighbour", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_TAG + " \n friend \n\t neighbour\t", expectedFindCommand);
    }

    @Test
    public void parse_validAddressArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Object[] addressKeywords = {PREFIX_ADDRESS, new String[]{"5", "Boundary", "Road"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(false, addressKeywords));
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "5 Boundary Road", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + " \n 5 Boundary Road", expectedFindCommand);
    }

    @Test
    public void parse_validEmailArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Object[] emailKeywords = {PREFIX_EMAIL, new String[]{"johndoe@example.com"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(false, emailKeywords));
        assertParseSuccess(parser, " " + PREFIX_EMAIL + "johndoe@example.com", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_EMAIL + " \n johndoe@example.com", expectedFindCommand);
    }

    @Test
    public void parse_validPhoneArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Object[] phoneKeywords = {PREFIX_PHONE, new String[]{"911"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(false, phoneKeywords));
        assertParseSuccess(parser, " " + PREFIX_PHONE + "911", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_PHONE + " \n 911", expectedFindCommand);
    }

    @Test
    public void parse_validCommentArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Object[] commentKeywords = {PREFIX_COMMENT, new String[]{"test"}};
        FindCommand expectedFindCommand = new FindCommand(new ContainsKeywordsPredicate(false, commentKeywords));
        assertParseSuccess(parser, " " + PREFIX_COMMENT + "test", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_COMMENT + " \n test", expectedFindCommand);
    }
}
