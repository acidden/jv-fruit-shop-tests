package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.FileReader;
import core.basesyntax.dao.FileReaderImpl;
import org.junit.jupiter.api.Test;

class FileReaderTest {
    private final FileReader fileReader = new FileReaderImpl();

    @Test
    void read_nonExistingFile_throwsException() {
        String nonExistingPath = "non_existing_file_12345.csv";
        assertThrows(RuntimeException.class, () -> fileReader.read(nonExistingPath));
    }

    @Test
    void read_nullOrEmptyPath_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> fileReader.read(null));
        assertThrows(IllegalArgumentException.class, () -> fileReader.read("   "));
    }
}
