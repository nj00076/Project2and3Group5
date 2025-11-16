package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.Stock;

public class StockTest {

    @Test
    public void testStockGetters() {
        Stock stock = new Stock("Apple", 5, "New", "2025-12-31", "Perishable");

        assertEquals("Apple", stock.getName());
        assertEquals(5, stock.getSize());
        assertEquals("New", stock.getCondition());
        assertEquals("2025-12-31", stock.getExpirationDate());
        assertEquals("Perishable", stock.getSpecialQuals());
    }
}
