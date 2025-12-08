package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Stock;

class CompartmentTest {

    private Compartment compartment;

    @BeforeEach
    void setUp() {
        List<Stock.SpecialQuality> allowed = List.of(
            Stock.SpecialQuality.PERISHABLE,
            Stock.SpecialQuality.FLAMMABLE
        );
        compartment = new Compartment("compartment1", 10, allowed);
    }

    @Test
    void testGetName() {
        assertEquals("compartment1", compartment.getName());
    }

    @Test
    void testGetFreeSpaceEmptyCompartment() {
        assertEquals(10, compartment.getFreeSpace(), "Empty compartment free space should equal capacity");
    }

    @Test
    void testCanStoreTrue() {
        Stock stock = new Stock("Rum", 1, Stock.Condition.PERFECT,
                                List.of(Stock.SpecialQuality.FLAMMABLE),
                                null, 3);
        assertTrue(compartment.canStore(stock), "Stock fits and has allowed qualities");
    }

    @Test
    void testCanStoreFalseDueToSpace() {
        Stock stock1 = new Stock("Rum", 1, Stock.Condition.PERFECT,
                                 List.of(Stock.SpecialQuality.FLAMMABLE),
                                 null, 8);
        Stock stock2 = new Stock("Water", 1, Stock.Condition.PERFECT,
                                 List.of(Stock.SpecialQuality.PERISHABLE),
                                 null, 5);
        compartment.addStock(stock1);
        assertFalse(compartment.canStore(stock2), "Not enough free space for the stock");
    }

    @Test
    void testCanStoreFalseDueToQuality() {
        Stock stock = new Stock("Gun", 1, Stock.Condition.PERFECT,
                                List.of(Stock.SpecialQuality.LIQUID),
                                null, 2);
        assertFalse(compartment.canStore(stock), "Stock has disallowed qualities");
    }

    @Test
    void testAddStockSuccessfully() {
        Stock stock = new Stock("Rum", 1, Stock.Condition.PERFECT,
                                List.of(Stock.SpecialQuality.FLAMMABLE),
                                null, 3);
        compartment.addStock(stock);
        assertEquals(1, compartment.getStockList().size(), "Stock should be added");
        assertSame(stock, compartment.getStockList().get(0));
        assertEquals(7, compartment.getFreeSpace(), "Free space should decrease after adding stock");
    }

    @Test
    void testAddStockFails() {
        Stock stock = new Stock("Gun", 1, Stock.Condition.PERFECT,
                                List.of(Stock.SpecialQuality.LIQUID),
                                null, 3);
        compartment.addStock(stock);
        assertTrue(compartment.getStockList().isEmpty(), "Stock with disallowed quality should not be added");
        assertEquals(10, compartment.getFreeSpace(), "Free space remains unchanged");
    }

    @Test
    void testGetStockListReturnsActualList() {
        Stock stock = new Stock("Rum", 1, Stock.Condition.PERFECT,
                                List.of(Stock.SpecialQuality.FLAMMABLE),
                                null, 3);
        compartment.addStock(stock);
        List<Stock> list = compartment.getStockList();
        assertSame(list, compartment.getStockList(), "getStockList should always return same list instance");
    }
}
