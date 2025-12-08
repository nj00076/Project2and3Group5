package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A store for all stock changes in the inventory system.
 * Maintains a static list of StockChange objects.
 * Provides methods to add and retrieve changes.
 * @author nj00076
 * @version cs3211
 */
public class ChangeLogStore {

    /**
     * The list of stock changes.
     */
    private static final List<StockChange> CHANGES = new ArrayList<>();

    /**
     * Adds a StockChange entry to the change log.
     *
     * @param change the StockChange to add
     */
    public static void addChange(StockChange change) {
        CHANGES.add(change);
    }

    /**
     * Returns the list of all stock changes.
     * Modifications to the returned list affect the internal change log.
     *
     * @return the list of StockChange entries
     */
    public static List<StockChange> getChanges() {
        return CHANGES;
    }
}
