package core.basesyntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import core.basesyntax.service.DataConverter;
import core.basesyntax.service.DataConverterImpl;
import core.basesyntax.service.FruitTransaction;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataConverterTest {
    private DataConverter dataConverter;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
    }

    @Test
    void convertToTransaction_ValidLinesWithHeader_ok() {
        List<String> inputLines = List.of(
                "type,fruit,quantity",
                "b,banana,20",
                "s,apple,100"
        );
        List<FruitTransaction> actualTransactions = dataConverter.convertToTransaction(inputLines);
        assertEquals(2, actualTransactions.size());

        FruitTransaction first = actualTransactions.get(0);
        assertEquals(FruitTransaction.Operation.BALANCE, first.getOperation());
        assertEquals("banana", first.getFruit());
        assertEquals(20, first.getQuantity());

        FruitTransaction second = actualTransactions.get(1);
        assertEquals(FruitTransaction.Operation.SUPPLY, second.getOperation());
        assertEquals("apple", second.getFruit());
        assertEquals(100, second.getQuantity());
    }

    @Test
    void convertToTransaction_ValidLinesWithSpaces_ok() {
        List<String> inputLines = List.of(
                "type,fruit,quantity",
                "b , banana , 20 ");
        List<FruitTransaction> actualTransactions = dataConverter.convertToTransaction(inputLines);
        FruitTransaction first = actualTransactions.get(0);
        assertEquals(FruitTransaction.Operation.BALANCE, first.getOperation());
        assertEquals("banana", first.getFruit());
        assertEquals(20, first.getQuantity());
    }

    @Test
    void convertToTransaction_NullLinesList_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                dataConverter.convertToTransaction(null));
    }

    @Test
    void convertToTransaction_NegativeQuantity_throwsException() {
        List<String> invalidLines = List.of(
                "type,fruit,quantity",
                "b,banana,-5"
        );
        assertThrows(RuntimeException.class, () ->
                dataConverter.convertToTransaction(invalidLines));
    }

    @Test
    void convertToTransaction_InvalidCsvFormat_throwsException() {
        List<String> invalidLines = List.of(
                "type,fruit,quantity",
                "b,banana"
        );
        DataConverter dataConverter = new DataConverterImpl();
        assertThrows(RuntimeException.class, () ->
                dataConverter.convertToTransaction(invalidLines));
    }
}
