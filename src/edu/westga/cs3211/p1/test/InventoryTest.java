package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.Stock;

public class InventoryTest {

    @Test
    public void testInventoryInitialization() {
        Inventory inventory = new Inventory();
        assertEquals(3, inventory.getCompartments().size());
        assertTrue(inventory.hasFreeSpace());
    }

    @Test
    public void testHasFreeSpaceWithFullCompartments() {
        Inventory inventory = new Inventory();
        for (Compartment comp : inventory.getCompartments()) {
            comp.addStock(new Stock("Item", 100, "New", "2025-12-31", "N/A"));
        }
        assertFalse(inventory.hasFreeSpace());
    }
}
