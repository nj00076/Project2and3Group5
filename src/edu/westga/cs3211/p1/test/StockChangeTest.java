package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.StockChange;

public class StockChangeTest {

    @Test
    public void testStockChangeGettersAndToString() {
        StockChange change = new StockChange("user", "Apple", 5, "Food");

        assertEquals("user", change.getUsername());
        assertEquals("Apple", change.getItemName());
        assertEquals(5, change.getQuantity());
        assertEquals("Food", change.getCompartment());
        assertNotNull(change.getTimestamp());

        String str = change.toString();
        assertTrue(str.contains("user"));
        assertTrue(str.contains("Apple"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("Food"));
    }
}
