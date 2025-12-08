package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a singleton-style access to the Inventory and
 * a log of stock changes. All methods and fields are static.
 * @author nj00076
 * @version cs3211
 */
public class InventoryStore {

    private static final Inventory INVENTORY = new Inventory();
    private static List<StockChange> changeLog = new ArrayList<>();

    /**
     * Returns the singleton Inventory instance.
     *
     * @return the inventory
     */
    public static Inventory getInventory() {
        return INVENTORY;
    }

    /**
     * Returns the list of stock changes. Modifications to this list
     * will affect the stored change log.
     *
     * @return the list of stock changes
     */
    public static List<StockChange> getChangeLog() {
        return changeLog;
    }

    /**
     * Adds a stock change entry to the change log.
     *
     * @param entry the StockChange to add
     */
    public static void addChangeLogEntry(StockChange entry) {
        changeLog.add(entry);
    }
}
