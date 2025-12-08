package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.Stock;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testDefaultCompartmentsExist() {
        List<Compartment> compartments = inventory.getCompartments();
        assertEquals(3, compartments.size(), "Inventory should start with 3 default compartments");
        assertEquals("Food", compartments.get(0).getName());
        assertEquals("Munitions", compartments.get(1).getName());
        assertEquals("Other", compartments.get(2).getName());
    }

    @Test
    void testAddCompartment() {
        Compartment extra = new Compartment("something", 50, List.of(Stock.SpecialQuality.PERISHABLE));
        inventory.addCompartment(extra);
        List<Compartment> compartments = inventory.getCompartments();
        assertEquals(4, compartments.size(), "Inventory should have 4 compartments after adding one");
        assertSame(extra, compartments.get(3), "Added compartment should be last in the list");
    }

    @Test
    void testGetCompatibleCompartmentsWithFitAndAllowedQualities() {
        Stock stock = new Stock("Rum", 10, Stock.Condition.PERFECT, 
                List.of(Stock.SpecialQuality.FLAMMABLE), null, 10);
        List<Compartment> compatible = inventory.getCompatibleCompartments(stock);
        assertFalse(compatible.isEmpty(), "There should be compartments that can store this stock");
        for (Compartment c : compatible) {
            assertTrue(c.canStore(stock), "Each returned compartment should be able to store the stock");
        }
    }

    @Test
    void testGetCompatibleCompartmentsWithNoFit() {
        Stock stock = new Stock("Water", 500, Stock.Condition.PERFECT,
                List.of(Stock.SpecialQuality.FLAMMABLE), null, 500);
        List<Compartment> compatible = inventory.getCompatibleCompartments(stock);
        assertTrue(compatible.isEmpty(), "No compartment should be able to store a stock that is too large");
    }

    @Test
    void testGetCompatibleCompartmentsWithDisallowedQualities() {
        Stock stock = new Stock("Water", 5, Stock.Condition.PERFECT,
                List.of(Stock.SpecialQuality.LIQUID), null, 5);
        List<Compartment> compatible = inventory.getCompatibleCompartments(stock);
        assertFalse(compatible.isEmpty(), "Some compartments (Food/Other) allow LIQUID, so compatible list is not empty");
    }
}
