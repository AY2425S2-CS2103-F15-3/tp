package seedu.recruittrackpro.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.recruittrackpro.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.recruittrackpro.commons.core.index.Index;
import seedu.recruittrackpro.commons.util.ToStringBuilder;
import seedu.recruittrackpro.logic.Messages;
import seedu.recruittrackpro.logic.commands.exceptions.CommandException;
import seedu.recruittrackpro.model.Model;
import seedu.recruittrackpro.model.person.Person;
import seedu.recruittrackpro.model.tag.Tag;

/**
 * Adds one or more tags to an existing candidate in the address book.
 */
public class AddTagsCommand extends Command {

    public static final String COMMAND_WORD = "add-tags";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds one or more tags to a candidate "
            + "using the index number from the displayed person list. "
            + "New tags will be appended to the person's existing tag list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "Junior Java Developer at Microsoft "
            + PREFIX_TAG + "Masters in Computer Science";

    public static final String MESSAGE_ADD_TAGS_SUCCESS = "New tags added to %1$s: %2$s";
    public static final String MESSAGE_NO_TAGS_FOUND = "At least one tag must be provided.";
    public static final String MESSAGE_DUPLICATE_TAGS = "The following tags already exist for %1$s: %2$s";

    private final Index index;
    private final Set<Tag> tagsToAdd;

    /**
     * Creates an AddTagsCommand to add the specified {@code tags} to the person at {@code index}.
     */
    public AddTagsCommand(Index index, Set<Tag> tagsToAdd) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);

        this.index = index;
        this.tagsToAdd = new HashSet<>(tagsToAdd); // Defensive copy
    }

    /**
     * Retrieves the person at the given index from the last shown list.
     *
     * @param lastShownList List of persons currently displayed.
     * @return The person to be updated.
     * @throws CommandException if the index is invalid.
     */
    private Person getTargetPerson(List<Person> lastShownList) throws CommandException {
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return lastShownList.get(index.getZeroBased());
    }

    /**
     * Creates a new updated person with the added tags.
     *
     * @param targetPerson The original person.
     * @param newlyAddedTags The new tags to be added.
     * @param currentTags The current tags of the person.
     * @return A new Person object with updated tags.
     */
    private Person createUpdatedPerson(Person targetPerson, Set<Tag> newlyAddedTags, Set<Tag> currentTags) {
        currentTags.addAll(newlyAddedTags);
        return new Person(targetPerson.getName(), targetPerson.getPhone(),
                targetPerson.getEmail(), targetPerson.getAddress(), currentTags, targetPerson.getComment());
    }

    /**
     * Constructs the result message indicating which tags were added and which were duplicates.
     *
     * @param targetPerson The person whose tags were updated.
     * @param newlyAddedTags The newly added tags.
     * @param duplicateTags The tags that were already present.
     * @return A formatted success message.
     */
    private String constructResultMessage(Person targetPerson, Set<Tag> newlyAddedTags, Set<Tag> duplicateTags) {
        String formattedNewTags = formatTags(newlyAddedTags);
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(String.format(MESSAGE_ADD_TAGS_SUCCESS, targetPerson.getName(), formattedNewTags));

        if (!duplicateTags.isEmpty()) {
            String formattedDuplicateTags = formatTags(duplicateTags);
            resultMessage.append("\n").append(String.format(MESSAGE_DUPLICATE_TAGS,
                    targetPerson.getName(), formattedDuplicateTags));
        }
        return resultMessage.toString();
    }

    /**
     * Formats a set of tags into a string representation: ["Tag1", "Tag2"]
     */
    private static String formatTags(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> "\"" + tag.toString().replaceAll("^[\\[]|[\\]]$", "") + "\"")
                .collect(Collectors.joining(", ", "[", "]"));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (tagsToAdd.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS_FOUND);
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        Person targetPerson = getTargetPerson(lastShownList);
        Set<Tag> currentTags = new HashSet<>(targetPerson.getTags());

        Set<Tag> newlyAddedTags = new HashSet<>();
        Set<Tag> duplicateTags = new HashSet<>();

        for (Tag tag : tagsToAdd) {
            if (currentTags.add(tag)) {
                newlyAddedTags.add(tag);
            } else {
                duplicateTags.add(tag);
            }
        }

        if (newlyAddedTags.isEmpty()) {
            return new CommandResult(
                    String.format(MESSAGE_DUPLICATE_TAGS, targetPerson.getName(), formatTags(duplicateTags))
            );
        }

        Person updatedPerson = createUpdatedPerson(targetPerson, newlyAddedTags, currentTags);
        model.setPerson(targetPerson, updatedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(constructResultMessage(targetPerson, newlyAddedTags, duplicateTags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddTagsCommand)) {
            return false;
        }

        AddTagsCommand otherCommand = (AddTagsCommand) other;
        return index.equals(otherCommand.index)
                && tagsToAdd.equals(otherCommand.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }
}
