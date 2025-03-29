package seedu.recruittrackpro.model.util;

import java.util.List;

import seedu.recruittrackpro.logic.parser.exceptions.ParseException;
import seedu.recruittrackpro.model.ReadOnlyRecruitTrackPro;
import seedu.recruittrackpro.model.RecruitTrackPro;
import seedu.recruittrackpro.model.person.Address;
import seedu.recruittrackpro.model.person.Comment;
import seedu.recruittrackpro.model.person.Email;
import seedu.recruittrackpro.model.person.Name;
import seedu.recruittrackpro.model.person.Person;
import seedu.recruittrackpro.model.person.Phone;
import seedu.recruittrackpro.model.tag.Tags;

/**
 * Contains utility methods for populating {@code RecruitTrackPro} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        try {
            return new Person[]{
                new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    new Tags(List.of("Java")),
                    new Comment("Graduated with a bachelor's degree from NUS")),
                new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new Tags(List.of("Python", "JavaScript")),
                    new Comment("Pursuing a master's degree in NUS")),
                new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    new Tags(List.of("C#", "Swift")),
                    new Comment("Pursuing a bachelor's degree in NUS")),
                new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    new Tags(List.of(".NET")),
                    new Comment("Graduated with a bachelor's degree from NUS")),
                new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    new Tags(List.of("JavaScript", "SQL")),
                    new Comment("Graduated with a bachelor's degree from NUS")),
                new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    new Tags(List.of("Python")),
                    new Comment("Pursuing a bachelor's degree in NUS"))
            };
        } catch (ParseException e) {
            // This should never happen as sample data is guaranteed to be valid.
            throw new AssertionError("Sample data contains invalid tag(s): " + e.getMessage(), e);
        }
    }

    public static ReadOnlyRecruitTrackPro getSampleRecruitTrackPro() {
        RecruitTrackPro sampleAb = new RecruitTrackPro();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }
}
