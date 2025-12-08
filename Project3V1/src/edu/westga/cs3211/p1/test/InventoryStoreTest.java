package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.Stock;
import edu.westga.cs3211.p1.model.StockChange;

class InventoryStoreTest {

    @BeforeEach
    void clearChangeLog() {
        InventoryStore.getChangeLog().clear();
    }

    @Test
    void testGetInventoryNotNull() {
        Inventory inventory = InventoryStore.getInventory();
        assertNotNull(inventory);
    }

    @Test
    void testGetInventoryAlwaysSameInstance() {
        Inventory first = InventoryStore.getInventory();
        Inventory second = InventoryStore.getInventory();
        assertSame(first, second);
    }

    @Test
    void testChangeLogInitiallyEmpty() {
        List<StockChange> log = InventoryStore.getChangeLog();
        assertTrue(log.isEmpty());
    }

    @Test
    void testAddChangeLogEntry() {
        Stock stock = new Stock("Rum", 5, Stock.Condition.PERFECT,
                List.of(Stock.SpecialQuality.FLAMMABLE), null, 5);
        StockChange change = new StockChange("user1", stock, "compartment1", 5);
        InventoryStore.addChangeLogEntry(change);

        List<StockChange> log = InventoryStore.getChangeLog();
        assertEquals(1, log.size());
        assertSame(change, log.get(0));
    }

    @Test
    void testAddMultipleChangeLogEntries() {
        Stock stock1 = new Stock("Rum", 5, Stock.Condition.PERFECT,
                List.of(Stock.SpecialQuality.FLAMMABLE), null, 5);
        StockChange change1 = new StockChange("user1", stock1, "compartment1", 5);

        Stock stock2 = new Stock("Water", 2, Stock.Condition.USABLE,
                List.of(Stock.SpecialQuality.PERISHABLE), null, 2);
        StockChange change2 = new StockChange("user2", stock2, "compartment2", 2);

        InventoryStore.addChangeLogEntry(change1);
        InventoryStore.addChangeLogEntry(change2);

        List<StockChange> log = InventoryStore.getChangeLog();
        assertEquals(2, log.size());
        assertSame(change1, log.get(0));
        assertSame(change2, log.get(1));
    }

    @Test
    void testGetChangeLogAlwaysSameListInstance() {
        List<StockChange> firstCall = InventoryStore.getChangeLog();
        List<StockChange> secondCall = InventoryStore.getChangeLog();
        assertSame(firstCall, secondCall);
    }
}
