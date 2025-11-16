package edu.westga.cs3211.p1.model;

public class InventoryStore {

    private static final Inventory INVENTORY = new Inventory();

    public static Inventory getInventory() {
        return INVENTORY;
    }
}
