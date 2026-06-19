package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.service.FruitTransaction;
import core.basesyntax.service.operation.BalanceOperationHandler;
import core.basesyntax.service.operation.OperationHandler;
import core.basesyntax.service.operation.PurchaseOperationHandler;
import core.basesyntax.service.operation.ReturnOperationHandler;
import core.basesyntax.service.operation.SupplyOperationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationHandlersTest {
    private OperationHandler balanceHandler;
    private OperationHandler supplyHandler;
    private OperationHandler purchaseHandler;
    private OperationHandler returnHandler;

    @BeforeEach
    void setUp() {
        Storage.clear();
        balanceHandler = new BalanceOperationHandler();
        supplyHandler = new SupplyOperationHandler();
        purchaseHandler = new PurchaseOperationHandler();
        returnHandler = new ReturnOperationHandler();
    }

    @Test
    void balanceHandler_validTransaction_ok() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "banana", 100);
        balanceHandler.handle(transaction);
        assertEquals(100, Storage.getQuantity("banana"));
    }

    @Test
    void supplyHandler_newAndExistingFruit_ok() {
        // Поставка нового фрукта
        FruitTransaction supplyNew = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "apple", 50);
        supplyHandler.handle(supplyNew);
        assertEquals(50, Storage.getQuantity("apple"));

        FruitTransaction supplyExisting = new FruitTransaction(FruitTransaction.Operation.SUPPLY,
                "apple", 30);
        supplyHandler.handle(supplyExisting);
        assertEquals(80, Storage.getQuantity("apple"));
    }

    @Test
    void returnHandler_ok() {
        FruitTransaction returnTx = new FruitTransaction(FruitTransaction.Operation.RETURN,
                "orange", 10);
        returnHandler.handle(returnTx);
        assertEquals(10, Storage.getQuantity("orange"));
    }

    @Test
    void purchaseHandler_validPurchase_ok() {
        Storage.addOrUpdate("banana", 20);
        FruitTransaction purchase = new FruitTransaction(FruitTransaction.Operation.PURCHASE,
                "banana", 5);
        purchaseHandler.handle(purchase);
        assertEquals(15, Storage.getQuantity("banana"));
    }

    @Test
    void purchaseHandler_productNotFound_throwsException() {
        FruitTransaction purchase = new FruitTransaction(FruitTransaction.Operation.PURCHASE,
                "pear", 5);
        
        assertThrows(RuntimeException.class, () -> purchaseHandler.handle(purchase));
    }

    @Test
    void purchaseHandler_notEnoughQuantity_throwsException() {
        Storage.addOrUpdate("banana", 10);
        FruitTransaction purchase = new FruitTransaction(FruitTransaction.Operation.PURCHASE,
                "banana", 15);
        assertThrows(RuntimeException.class, () -> purchaseHandler.handle(purchase));
    }
}
