package seedu.recruittrackpro.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.recruittrackpro.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Arrays;
import java.util.stream.Stream;

import seedu.recruittrackpro.logic.commands.FindCommand;
import seedu.recruittrackpro.logic.parser.exceptions.ParseException;
import seedu.recruittrackpro.model.person.Name;
import seedu.recruittrackpro.model.person.NameContainsKeywordsPredicate;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        String preamble = argMultimap.getPreamble();
        boolean shouldContainAll = preamble.equals("--contain-all");

        if (!hasAtLeastOnePrefixPresent(argMultimap, PREFIX_NAME)
                || (!preamble.isEmpty() && !shouldContainAll)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);
        String name = argMultimap.getValue(PREFIX_NAME).orElse("");

        if (name.isEmpty()) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }

        String[] nameKeywords = name.split("\\s+");
        return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords), shouldContainAll));
    }

    /**
     * Returns true if at least one of the prefixes does not contain empty {@code Optional} values.
     */
    public boolean hasAtLeastOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix ... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
