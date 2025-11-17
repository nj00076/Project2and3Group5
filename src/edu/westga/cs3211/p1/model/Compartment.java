package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class Compartment {

    private String name;
    private int capacity;
    private List<Stock> stockList;
    private List<Stock.SpecialQuality> allowedQualities;

    public Compartment(String name, int capacity, List<Stock.SpecialQuality> allowedQualities) {
        this.name = name;
        this.capacity = capacity;
        this.stockList = new ArrayList<>();
        this.allowedQualities = allowedQualities;
    }

    public String getName() {
        return this.name;
    }

    public int getFreeSpace() {
        int usedSpace = this.stockList.stream().mapToInt(Stock::getSize).sum();
        return this.capacity - usedSpace;
    }

    public boolean canStore(Stock stock) {
        if (this.getFreeSpace() < stock.getSize()) {
            return false;
        }
        return this.allowedQualities.containsAll(stock.getQualities());
    }

    public void addStock(Stock stock) {
        if (this.canStore(stock)) {
            this.stockList.add(stock);
        }
    }

    public List<Stock> getStockList() {
        return this.stockList;
    }
}
