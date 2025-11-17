package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.westga.cs3211.p1.model.Stock;
import edu.westga.cs3211.p1.model.StockChange;

class StockChangeTest {

    private Stock stock;
    private StockChange change;
    private String username = "crew";

    @BeforeEach
    void setUp() {
        stock = new Stock("Rum", 10, Stock.Condition.PERFECT,
                List.of(Stock.SpecialQuality.FLAMMABLE, Stock.SpecialQuality.PERISHABLE),
                LocalDate.of(2025, 12, 31),
                5);
        change = new StockChange(username, stock, "compartment1", 50);
    }

    @Test
    void testGetters() {
        assertEquals(username, change.getUsername());
        assertSame(stock, change.getStock());
        assertEquals("compartment1", change.getCompartmentName());
        assertEquals(50, change.getRemaining());
        assertNotNull(change.getTimestamp());
        assertTrue(change.getTimestamp() instanceof LocalDateTime);
    }

    @Test
    void testToStringContainsAllInfo() {
        String result = change.toString();
        assertTrue(result.contains("CrewMate: " + username));
        assertTrue(result.contains("Stock: " + stock.getName()));
        assertTrue(result.contains("Quantity: " + stock.getSize()));
        assertTrue(result.contains("Compartment: compartment1"));
        assertTrue(result.contains("Remaining Space: " + change.getRemaining()));
        assertTrue(result.contains("Date Added: " + change.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yy"))));
        assertTrue(result.contains("Special Qualities: FLAMMABLE, PERISHABLE"));
        assertTrue(result.contains("Expiration Date: " + stock.getExpirationDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yy"))));
    }

    
    @Test
    void testToStringWithQualitiesNoExpiration() {
        Stock stockQual = new Stock("Gun", 5, Stock.Condition.USABLE,
                List.of(Stock.SpecialQuality.FLAMMABLE),
                null,
                5);
        StockChange changeQual = new StockChange("mate", stockQual, "compartment1", 20);
        String result = changeQual.toString();
        assertTrue(result.contains("FLAMMABLE"));
        assertFalse(result.contains("Expiration Date"));
    }

    @Test
    void testToStringWithExpirationNoQualities() {
        Stock stockExp = new Stock("Rum", 8, Stock.Condition.PERFECT,
                List.of(),
                LocalDate.of(2026, 1, 1),
                8);
        StockChange changeExp = new StockChange("crew", stockExp, "compartment1", 10);
        String result = changeExp.toString();
        assertFalse(result.contains("Special Qualities"));
        assertTrue(result.contains("01/01/26"));
    }


    @Test
    void testToStringWithNoQualitiesOrExpiration() {
        Stock stockNoQual = new Stock("Water", 3, Stock.Condition.PERFECT,
                List.of(),
                null,
                3);
        StockChange changeNoQual = new StockChange("mate", stockNoQual, "compartment1", 100);
        String result = changeNoQual.toString();
        assertTrue(result.contains("mate"));
        assertTrue(result.contains("Water"));
        assertTrue(result.contains("compartment1"));
        assertFalse(result.contains("Special Qualities"));
        assertFalse(result.contains("Expiration Date"));
    }
}
