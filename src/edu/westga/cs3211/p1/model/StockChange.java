package edu.westga.cs3211.p1.model;

import java.time.LocalDateTime;

public class StockChange {

    private final String username;
    private final String itemName;
    private final int quantity;
    private final String compartment;
    private final LocalDateTime timestamp;

    public StockChange(String username, String itemName, int quantity, String compartment) {
        this.username = username;
        this.itemName = itemName;
        this.quantity = quantity;
        this.compartment = compartment;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() {
        return this.username;
    }

    public String getItemName() {
        return this.itemName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getCompartment() {
        return this.compartment;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s | User: %s | Item: %s | Qty: %d | Compartment: %s",
        		this.timestamp, this.username, this.itemName, this.quantity, this.compartment);
    }
}
