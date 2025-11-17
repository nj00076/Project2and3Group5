package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.Stock;

class StockTest {

    @Test
    void testConstructorAndGetters() {
        LocalDate expiration = LocalDate.of(2025, 12, 31);
        List<Stock.SpecialQuality> qualities = List.of(Stock.SpecialQuality.FLAMMABLE, Stock.SpecialQuality.PERISHABLE);

        Stock stock = new Stock(
            "Rum",
            10,
            Stock.Condition.PERFECT,
            qualities,
            expiration,
            5
        );

        assertEquals("Rum", stock.getName(), "Name should match constructor value");
        assertEquals(10, stock.getQuantity(), "Quantity should match constructor value");
        assertEquals(Stock.Condition.PERFECT, stock.getCondition(), "Condition should match constructor value");
        assertEquals(qualities, stock.getQualities(), "Qualities should match constructor value");
        assertEquals(expiration, stock.getExpirationDate(), "Expiration date should match constructor value");
        assertEquals(5, stock.getSize(), "Size should match constructor value");
    }

    @Test
    void testEmptyQualitiesAndNullExpiration() {
        Stock stock = new Stock(
            "Water",
            20,
            Stock.Condition.USABLE,
            List.of(),
            null,
            2
        );

        assertEquals("Water", stock.getName());
        assertEquals(20, stock.getQuantity());
        assertEquals(Stock.Condition.USABLE, stock.getCondition());
        assertTrue(stock.getQualities().isEmpty(), "Qualities should be empty");
        assertNull(stock.getExpirationDate(), "Expiration date should be null");
        assertEquals(2, stock.getSize());
    }
}
