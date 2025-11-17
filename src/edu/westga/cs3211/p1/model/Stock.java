package edu.westga.cs3211.p1.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a stock item in the inventory, including its name, quantity, condition,
 * special qualities, expiration date (if applicable), and size.
 * @author nj00076
 * @version cs3211
 */
public class Stock {

    /**
     * Represents the condition of the stock.
     */
    public enum Condition {
        PERFECT, USABLE, UNUSABLE
    }

    /**
     * Represents special qualities a stock item may have.
     */
    public enum SpecialQuality {
        FLAMMABLE, LIQUID, PERISHABLE
    }

    private String name;
    private int quantity;
    private Condition condition;
    private List<SpecialQuality> qualities;
    private LocalDate expirationDate;
    private int size;

    /**
     * Constructs a new Stock item.
     *
     * @param name          the name of the stock
     * @param quantity      the quantity of the stock
     * @param condition     the condition of the stock
     * @param qualities     the list of special qualities
     * @param expirationDate the expiration date if applicable, otherwise null
     * @param size          the size or space occupied by this stock
     */
    public Stock(String name, int quantity, Condition condition,
                 List<SpecialQuality> qualities, LocalDate expirationDate, int size) {
        this.name = name;
        this.quantity = quantity;
        this.condition = condition;
        this.qualities = qualities;
        this.expirationDate = expirationDate;
        this.size = size;
    }

    /**
     * Returns the name of the stock item.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the quantity of the stock item.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Returns the condition of the stock item.
     *
     * @return the condition
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * Returns the list of special qualities of the stock.
     *
     * @return the list of qualities
     */
    public List<SpecialQuality> getQualities() {
        return this.qualities;
    }

    /**
     * Returns the expiration date of the stock if applicable.
     *
     * @return the expiration date, or null if not applicable
     */
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    /**
     * Returns the size of the stock item.
     *
     * @return the size
     */
    public int getSize() {
        return this.size;
    }
}
