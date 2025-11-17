package edu.westga.cs3211.p1.model;

import java.time.LocalDate;
import java.util.List;

public class Stock {

    public enum Condition {
        PERFECT, USABLE, UNUSABLE
    }

    public enum SpecialQuality {
        FLAMMABLE, LIQUID, PERISHABLE
    }

    private String name;
    private int quantity;
    private Condition condition;
    private List<SpecialQuality> qualities;
    private LocalDate expirationDate;
    private int size;

    public Stock(String name, int quantity, Condition condition, 
                 List<SpecialQuality> qualities, LocalDate expirationDate, int size) {

        this.name = name;
        this.quantity = quantity;
        this.condition = condition;
        this.qualities = qualities;
        this.expirationDate = expirationDate;
        this.size = size;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public List<SpecialQuality> getQualities() {
        return this.qualities;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public int getSize() {
        return this.size;
    }
}
