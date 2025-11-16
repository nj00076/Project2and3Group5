package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class Compartment {
    private String name;
    private int capacity;
    private List<Stock> stockList;

    public Compartment(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.stockList = new ArrayList<>();
    }

    public String getName() { return name; }

    public List<Stock> getStockList() { return stockList; }

    public boolean hasFreeSpace() {
        return getFreeSpace() > 0;
    }

    public int getFreeSpace() {
        int usedSpace = stockList.stream().mapToInt(Stock::getSize).sum();
        return capacity - usedSpace;
    }

    public void addStock(Stock stock) {
        if (getFreeSpace() >= stock.getSize()) {
            stockList.add(stock);
        }
    }
}
