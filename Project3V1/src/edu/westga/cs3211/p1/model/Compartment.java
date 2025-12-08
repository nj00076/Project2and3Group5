package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a compartment in the inventory that can hold stock items.
 * Each compartment has a name, capacity, allowed special qualities, 
 * and a list of stocks currently stored.
 * @author nj00076
 * @version cs3211
 */
public class Compartment {

    private String name;
    private int capacity;
    private List<Stock> stockList;
    private List<Stock.SpecialQuality> allowedQualities;

    /**
     * Constructs a Compartment with the given name, capacity, and allowed qualities.
     *
     * @param name the name of the compartment
     * @param capacity the maximum total size of stock that can be stored
     * @param allowedQualities the special qualities of stock allowed in this compartment
     */
    public Compartment(String name, int capacity, List<Stock.SpecialQuality> allowedQualities) {
        this.name = name;
        this.capacity = capacity;
        this.stockList = new ArrayList<>();
        this.allowedQualities = allowedQualities;
    }

    /**
     * Returns the name of the compartment.
     *
     * @return the compartment name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the remaining free space in the compartment.
     * Calculated as capacity minus the sum of sizes of all stored stock.
     *
     * @return the available free space
     */
    public int getFreeSpace() {
        int usedSpace = this.stockList.stream().mapToInt(Stock::getSize).sum();
        return this.capacity - usedSpace;
    }

    /**
     * Determines if the given stock can be stored in this compartment.
     * Stock can be stored if there is enough free space and all its
     * special qualities are allowed in this compartment.
     *
     * @param stock the stock to check
     * @return true if the stock can be stored, false otherwise
     */
    public boolean canStore(Stock stock) {
        if (this.getFreeSpace() < stock.getSize()) {
            return false;
        }
        return this.allowedQualities.containsAll(stock.getQualities());
    }

    /**
     * Adds the given stock to the compartment if it can be stored.
     *
     * @param stock the stock to add
     */
    public void addStock(Stock stock) {
        if (this.canStore(stock)) {
            this.stockList.add(stock);
        }
    }

    /**
     * Returns the list of stock items currently stored in the compartment.
     * Modifications to the returned list will affect the compartment's contents.
     *
     * @return the list of stored stock
     */
    public List<Stock> getStockList() {
        return this.stockList;
    }
}
