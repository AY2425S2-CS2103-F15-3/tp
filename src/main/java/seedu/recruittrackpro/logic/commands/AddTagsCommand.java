package seedu.recruittrackpro.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.recruittrackpro.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import seedu.recruittrackpro.commons.core.index.Index;
import seedu.recruittrackpro.commons.util.ToStringBuilder;
import seedu.recruittrackpro.logic.Messages;
import seedu.recruittrackpro.logic.commands.exceptions.CommandException;
import seedu.recruittrackpro.logic.descriptors.EditPersonDescriptor;
import seedu.recruittrackpro.logic.util.EditPersonUtil;
import seedu.recruittrackpro.model.Model;
import seedu.recruittrackpro.model.person.Person;
import seedu.recruittrackpro.model.tag.Tags;

/**
 * Adds one or more tags to an existing candidate in RecruitTrackPro.
 * <p>
 * This command checks for existing tags before adding and avoids duplicates.
 * Only new tags are added; repeated tags are reported as duplicates.
 */
public class AddTagsCommand extends Command {

    public static final String COMMAND_WORD = "add-tags";

    public static final String SHORT_MESSAGE_USAGE = COMMAND_WORD + ": Adds one or more tags to a candidate "
            + "using the index number from the displayed person list.";

    public static final String MESSAGE_USAGE = SHORT_MESSAGE_USAGE
            + " New tags will be appended to the person's existing tag list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG1] "
            + "[" + PREFIX_TAG + "TAG2]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "Junior Java Developer at Microsoft "
            + PREFIX_TAG + "Masters in Computer Science";

    public static final String MESSAGE_ADD_TAGS_SUCCESS = "New tags added to %1$s: %2$s";
    public static final String MESSAGE_NO_TAGS_FOUND = "At least one tag must be provided.";
    public static final String MESSAGE_DUPLICATE_TAGS = "The following tags already exist for %1$s: %2$s";

    private final Index index;
    private final Tags tagsToAdd;

    /**
     * Constructs an {@code AddTagsCommand} with the given index and tags to add.
     *
     * @param index     The index of the person to edit in the filtered person list.
     * @param tagsToAdd The tags to be added.
     */
    public AddTagsCommand(Index index, Tags tagsToAdd) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);

        this.index = index;
        this.tagsToAdd = tagsToAdd;
    }

    /**
     * Executes the AddTagsCommand to append tags to a candidate.
     *
     * @param model The model containing the person list and data context.
     * @return CommandResult with success or duplicate warning message.
     * @throws CommandException if the index is invalid or no new tags were added.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (tagsToAdd.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS_FOUND);
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        Person targetPerson = getTargetPerson(lastShownList);

        Tags currentTags = targetPerson.getTags();
        Tags.TagSeparationResult result = currentTags.separateNewFromExisting(tagsToAdd);
        Tags uniqueTagsToAdd = result.newTags();
        Tags duplicateTags = result.duplicateTags();

        if (uniqueTagsToAdd.isEmpty()) {
            return new CommandResult(
                    String.format(MESSAGE_DUPLICATE_TAGS, targetPerson.getName(), duplicateTags)
            );
        }

        Person updatedPerson = addTagsToPerson(targetPerson, uniqueTagsToAdd);
        model.setPerson(targetPerson, updatedPerson);

        return new CommandResult(constructResultMessage(targetPerson, uniqueTagsToAdd, duplicateTags));
    }

    /**
     * Retrieves the person at the given index.
     *
     * @param lastShownList The list of currently displayed persons.
     * @return The person to update.
     * @throws CommandException if the index is out of bounds.
     */
    private Person getTargetPerson(List<Person> lastShownList) throws CommandException {
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return lastShownList.get(index.getZeroBased());
    }

    /**
     * Returns a new {@code Person} object with the new tags added.
     *
     * @param original     The original Person.
     * @param tagsToAppend The tags to be appended.
     * @return A new Person object with updated tag list.
     */
    private Person addTagsToPerson(Person original, Tags tagsToAppend) {
        Tags combinedTags = original.getTags().combineTags(tagsToAppend);
        EditPersonDescriptor editedDescriptor = new EditPersonDescriptor();
        editedDescriptor.setTags(combinedTags);
        return EditPersonUtil.createEditedPerson(original, editedDescriptor);
    }

    /**
     * Formats a result message string based on added and duplicate tags.
     *
     * @param person         The person being modified.
     * @param addedTags      Newly added tags.
     * @param duplicateTags  Tags that already existed.
     * @return Result message string.
     */
    private String constructResultMessage(Person person, Tags addedTags, Tags duplicateTags) {
        StringBuilder result = new StringBuilder(
                String.format(
                        MESSAGE_ADD_TAGS_SUCCESS,
                        person.getName(),
                        addedTags)
        );

        if (!duplicateTags.isEmpty()) {
            result.append("\n").append(
                    String.format(
                            MESSAGE_DUPLICATE_TAGS,
                            person.getName(),
                            duplicateTags
                    )
            );
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddTagsCommand)) {
            return false;
        }
        AddTagsCommand otherCmd = (AddTagsCommand) other;
        return index.equals(otherCmd.index) && tagsToAdd.equals(otherCmd.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }
}
