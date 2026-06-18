package core.basesyntax;

import core.basesyntax.db.Storage;
import core.basesyntax.service.ReportGenerator;
import core.basesyntax.service.ReportGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {
    private final ReportGenerator reportGenerator = new ReportGeneratorImpl();

    @BeforeEach
    void setUp() {
        Storage.clear();
    }

    @Test
    void getReport_emptyStorage_returnsOnlyHeader() {
        String expected = "fruit,quantity";
        String actual = reportGenerator.getReport();
        assertEquals(expected, actual);
    }

    @Test
    void getReport_withData_ok() {
        Storage.addOrUpdate("banana", 152);
        Storage.addOrUpdate("apple", 90);

        String actual = reportGenerator.getReport();

        assertTrue(actual.contains("fruit,quantity"));
        assertTrue(actual.contains("banana,152"));
        assertTrue(actual.contains("apple,90"));
    }
}