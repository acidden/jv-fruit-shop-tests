package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.FileWriter;
import core.basesyntax.dao.FileWriterImpl;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileWriterTest {
    private final FileWriter fileWriter = new FileWriterImpl();

    @Test
    void write_nullOrEmptyPath_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("some data", null));
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("some data", ""));
    }

    @Test
    void write_nullData_throwsException(@TempDir Path tempDir) {
        Path tempFile = tempDir.resolve("test_output.csv");
        assertThrows(IllegalArgumentException.class, () ->
                fileWriter.write(null, tempFile.toString()));
    }
}
