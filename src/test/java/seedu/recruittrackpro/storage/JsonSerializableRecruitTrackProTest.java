package seedu.recruittrackpro.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.recruittrackpro.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.recruittrackpro.commons.exceptions.IllegalValueException;
import seedu.recruittrackpro.commons.util.JsonUtil;
import seedu.recruittrackpro.model.RecruitTrackPro;
import seedu.recruittrackpro.testutil.TypicalPersons;

public class JsonSerializableRecruitTrackProTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableRecruitTrackProTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsRecruitTrackPro.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonRecruitTrackPro.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonRecruitTrackPro.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableRecruitTrackPro dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableRecruitTrackPro.class).get();
        RecruitTrackPro recruitTrackProFromFile = dataFromFile.toModelType();
        RecruitTrackPro typicalPersonsRecruitTrackPro = TypicalPersons.getTypicalRecruitTrackPro();
        assertEquals(recruitTrackProFromFile, typicalPersonsRecruitTrackPro);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableRecruitTrackPro dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableRecruitTrackPro.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableRecruitTrackPro dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableRecruitTrackPro.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableRecruitTrackPro.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

}
