package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.westga.cs3211.p1.model.ChangeLogStore;
import edu.westga.cs3211.p1.model.StockChange;

class ChangeLogStoreTest {

    @BeforeEach
    void setUp() {
        ChangeLogStore.getChanges().clear();
    }

    @Test
    void testStaticListInitialization() {
        List<StockChange> list = ChangeLogStore.getChanges();
        assertNotNull(list);
    }

    @Test
    void testAddChangeAndGetChanges() {
        StockChange change1 = new StockChange("crew", null, "Compartment1", 0);
        StockChange change2 = new StockChange("mate", null, "Compartment2", 5);

        assertTrue(ChangeLogStore.getChanges().isEmpty());

        ChangeLogStore.addChange(change1);
        List<StockChange> afterFirstAdd = ChangeLogStore.getChanges();
        assertEquals(1, afterFirstAdd.size());
        assertSame(change1, afterFirstAdd.get(0));

        ChangeLogStore.addChange(change2);
        List<StockChange> afterSecondAdd = ChangeLogStore.getChanges();
        assertEquals(2, afterSecondAdd.size());
        assertSame(change2, afterSecondAdd.get(1));
    }

    @Test
    void testAddNullChange() {
        ChangeLogStore.addChange(null);
        List<StockChange> list = ChangeLogStore.getChanges();
        assertEquals(1, list.size());
        assertNull(list.get(0));
    }

    @Test
    void testGetChangesAlwaysReturnsSameList() {
        List<StockChange> list1 = ChangeLogStore.getChanges();
        StockChange change = new StockChange("crew", null, "Compartment3", 1);
        ChangeLogStore.addChange(change);
        List<StockChange> list2 = ChangeLogStore.getChanges();
        assertSame(list1, list2);
        assertEquals(1, list2.size());
    }

    @Test
    void testMultipleGetChangesAndAdditions() {
        List<StockChange> listBefore = ChangeLogStore.getChanges();
        StockChange change1 = new StockChange("crew", null, "Compartment4", 2);
        StockChange change2 = new StockChange("mate", null, "Compartment5", 3);

        ChangeLogStore.addChange(change1);
        ChangeLogStore.addChange(change2);

        List<StockChange> listAfter = ChangeLogStore.getChanges();
        assertSame(listBefore, listAfter);
        assertEquals(2, listAfter.size());
        assertSame(change1, listAfter.get(0));
        assertSame(change2, listAfter.get(1));
    }
}
