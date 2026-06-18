package core.basesyntax;

import core.basesyntax.service.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.service.ShopService;
import core.basesyntax.service.ShopServiceImpl;
import core.basesyntax.service.operation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        Storage.clear();

        Map<FruitTransaction.Operation, OperationHandler> handlers = new HashMap<>();
        handlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperationHandler());
        handlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperationHandler());
        handlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperationHandler());
        handlers.put(FruitTransaction.Operation.RETURN, new ReturnOperationHandler());
        
        OperationStrategy strategy = new OperationStrategyImpl(handlers);
        shopService = new ShopServiceImpl(strategy);
    }

    @Test
    void process_validTransactionsList_ok() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 100),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 20),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, "apple", 50)
        );

        shopService.process(transactions);

        assertEquals(80, Storage.getQuantity("banana"));
        assertEquals(50, Storage.getQuantity("apple"));
    }

    @Test
    void process_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> shopService.process(null));
    }

    @Test
    void process_listWithNullElement_throwsException() {
        List<FruitTransaction> transactions = new ArrayList<>();
        transactions.add(new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 10));
        transactions.add(null); // Элемент списка равен null

        assertThrows(IllegalArgumentException.class, () -> shopService.process(transactions));
    }
}