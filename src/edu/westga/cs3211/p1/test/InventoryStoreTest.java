package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.StockChange;

public class InventoryStoreTest {

    @BeforeEach
    public void resetStore() {
        InventoryStore.getChangeLog().clear();
    }

    @Test
    public void testGetInventoryReturnsSingleton() {
        Inventory inventory1 = InventoryStore.getInventory();
        Inventory inventory2 = InventoryStore.getInventory();
        assertSame(inventory1, inventory2);
    }

    @Test
    public void testAddChangeLogEntrySingle() {
        StockChange change = new StockChange("user", "item", 5, "Food");
        InventoryStore.addChangeLogEntry(change);
        List<StockChange> log = InventoryStore.getChangeLog();
        assertEquals(1, log.size());
        assertEquals(change, log.get(0));
    }

    @Test
    public void testAddChangeLogEntryMultiple() {
        StockChange change1 = new StockChange("user1", "item1", 3, "Food");
        StockChange change2 = new StockChange("user2", "item2", 7, "Munitions");

        InventoryStore.addChangeLogEntry(change1);
        InventoryStore.addChangeLogEntry(change2);

        List<StockChange> log = InventoryStore.getChangeLog();
        assertEquals(2, log.size());
        assertEquals(change1, log.get(0));
        assertEquals(change2, log.get(1));
    }

    @Test
    public void testAddChangeLogEntryNull() {
        InventoryStore.addChangeLogEntry(null);
        List<StockChange> log = InventoryStore.getChangeLog();
        assertEquals(1, log.size());
        assertNull(log.get(0));
    }

    @Test
    public void testGetChangeLogReturnsSameListInstance() {
        List<StockChange> log1 = InventoryStore.getChangeLog();
        List<StockChange> log2 = InventoryStore.getChangeLog();
        assertSame(log1, log2);
    }
}
