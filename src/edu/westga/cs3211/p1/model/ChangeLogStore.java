package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class ChangeLogStore {

    private static final List<StockChange> CHANGES = new ArrayList<>();

    public static void addChange(StockChange change) {
        CHANGES.add(change);
    }

    public static List<StockChange> getChanges() {
        return CHANGES;
    }
}
