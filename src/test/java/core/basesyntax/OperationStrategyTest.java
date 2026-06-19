package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.FruitTransaction;
import core.basesyntax.service.operation.BalanceOperationHandler;
import core.basesyntax.service.operation.OperationHandler;
import core.basesyntax.service.operation.OperationStrategy;
import core.basesyntax.service.operation.OperationStrategyImpl;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OperationStrategyTest {
    private OperationStrategy operationStrategy;
    private OperationHandler balanceHandler; //???

    @BeforeEach
    void setUp() {
        balanceHandler = new BalanceOperationHandler();
        Map<FruitTransaction.Operation, OperationHandler> handlers = new HashMap<>();
        handlers.put(FruitTransaction.Operation.BALANCE, balanceHandler);
        operationStrategy = new OperationStrategyImpl(handlers);
    }

    @Test
    void getValidOperation_ok() {
        OperationHandler actualHandler = operationStrategy.get(FruitTransaction.Operation.BALANCE);

        assertNotNull(actualHandler);
        assertEquals(balanceHandler, actualHandler);
    }

    @Test
    void getNullOperationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> operationStrategy.get(null));
    }

    @Test
    void getUnregisteredOperationThrowsException() {
        assertThrows(RuntimeException.class, () ->
                operationStrategy.get(FruitTransaction.Operation.PURCHASE));
    }
}
