package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class InventoryStore {

    private static final Inventory INVENTORY = new Inventory();
    private static List<StockChange> changeLog = new ArrayList<>();

    public static List<StockChange> getChangeLog() {
        return changeLog;
    }

    public static Inventory getInventory() {
        return INVENTORY;
    }

	public static void addChangeLogEntry(StockChange entry) {
		changeLog.add(entry);
	}
}
