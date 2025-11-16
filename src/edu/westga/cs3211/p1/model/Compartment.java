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

    public String getName() { 
    	return this.name; 
    }

    public List<Stock> getStockList() { 
    	return this.stockList; 
    }

    public boolean hasFreeSpace() {
        return this.getFreeSpace() > 0;
    }

    public int getFreeSpace() {
        int usedSpace = this.stockList.stream().mapToInt(Stock::getSize).sum();
        return this.capacity - usedSpace;
    }

    public void addStock(Stock stock) {
        if (this.getFreeSpace() >= stock.getSize()) {
        	this.stockList.add(stock);
        }
    }
}
