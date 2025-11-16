package edu.westga.cs3211.p1.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.westga.cs3211.p1.model.ChangeLogStore;
import edu.westga.cs3211.p1.model.StockChange;

public class ChangeLogStoreTest {

    @BeforeEach
    public void resetStore() {
        ChangeLogStore.getChanges().clear();
    }

    @Test
    public void testAddChangeAndGetChangesSingle() {
        StockChange change = new StockChange("user", "item", 5, "Food");
        ChangeLogStore.addChange(change);
        List<StockChange> changes = ChangeLogStore.getChanges();
        assertEquals(1, changes.size());
        assertEquals(change, changes.get(0));
    }

    @Test
    public void testAddChangeAndGetChangesMultiple() {
        StockChange change1 = new StockChange("user1", "item1", 3, "Food");
        StockChange change2 = new StockChange("user2", "item2", 7, "Munitions");

        ChangeLogStore.addChange(change1);
        ChangeLogStore.addChange(change2);

        List<StockChange> changes = ChangeLogStore.getChanges();
        assertEquals(2, changes.size());
        assertEquals(change1, changes.get(0));
        assertEquals(change2, changes.get(1));
    }

    @Test
    public void testAddNullChange() {
        ChangeLogStore.addChange(null);
        List<StockChange> changes = ChangeLogStore.getChanges();
        assertEquals(1, changes.size());
        assertNull(changes.get(0));
    }

    @Test
    public void testGetChangesReturnsSameListInstance() {
        List<StockChange> list1 = ChangeLogStore.getChanges();
        List<StockChange> list2 = ChangeLogStore.getChanges();
        assertSame(list1, list2);
    }
}
