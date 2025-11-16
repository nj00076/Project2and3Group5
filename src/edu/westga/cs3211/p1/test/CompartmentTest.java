package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Stock;

public class CompartmentTest {

    private Compartment compartment;

    @BeforeEach
    public void setup() {
        compartment = new Compartment("Food", 10);
    }

    @Test
    public void testGetName() {
        assertEquals("Food", compartment.getName());
    }

    @Test
    public void testAddStockAndGetStockList() {
        Stock stock = new Stock("Apple", 3, "New", "2025-12-31", "Perishable");
        compartment.addStock(stock);

        assertEquals(1, compartment.getStockList().size());
        assertEquals(stock, compartment.getStockList().get(0));
    }

    @Test
    public void testHasFreeSpaceAndGetFreeSpace() {
        assertTrue(compartment.hasFreeSpace());
        assertEquals(10, compartment.getFreeSpace());

        Stock stock = new Stock("Apple", 5, "New", "2025-12-31", "Perishable");
        compartment.addStock(stock);
        assertEquals(5, compartment.getFreeSpace());
        assertTrue(compartment.hasFreeSpace());

        Stock stock2 = new Stock("Banana", 5, "New", "2025-12-31", "Perishable");
        compartment.addStock(stock2);
        assertEquals(0, compartment.getFreeSpace());
        assertFalse(compartment.hasFreeSpace());
    }

    @Test
    public void testAddStockWhenFull() {
        Stock stock1 = new Stock("Apple", 10, "New", "2025-12-31", "Perishable");
        compartment.addStock(stock1);

        Stock stock2 = new Stock("Banana", 1, "New", "2025-12-31", "Perishable");
        compartment.addStock(stock2);

        assertEquals(1, compartment.getStockList().size());
    }
}
