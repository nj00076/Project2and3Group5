package edu.westga.cs3211.p1.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Represents a change in the stock inventory, including the user who added it,
 * the stock item, the compartment it was added to, remaining space, and the timestamp.
 * @author nj00076
 * @version cs3211
 */
public class StockChange {

    private String username;
    private Stock stock;
    private String compartmentName;
    private int remaining;
    private LocalDateTime timestamp;

    /**
     * Constructs a new StockChange record.
     *
     * @param username        the name of the user adding the stock
     * @param stock           the stock item added
     * @param compartmentName the compartment name where the stock was stored
     * @param remaining       the remaining space in the compartment after adding
     */
    public StockChange(String username, Stock stock, String compartmentName, int remaining) {
        this.username = username;
        this.stock = stock;
        this.compartmentName = compartmentName;
        this.remaining = remaining;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns the username associated with this stock change.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the stock item associated with this change.
     *
     * @return the stock
     */
    public Stock getStock() {
        return this.stock;
    }

    /**
     * Returns the name of the compartment where the stock was added.
     *
     * @return the compartment name
     */
    public String getCompartmentName() {
        return this.compartmentName;
    }

    /**
     * Returns the remaining space in the compartment after this change.
     *
     * @return the remaining space
     */
    public int getRemaining() {
        return this.remaining;
    }

    /**
     * Returns the timestamp of when this stock change occurred.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns a formatted string representation of the stock change, including username,
     * stock name, quantity, compartment, remaining space, date added, special qualities,
     * and expiration date if applicable.
     *
     * @return the string representation of this stock change
     */
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        StringBuilder sb = new StringBuilder();
        sb.append("CrewMate: ").append(this.username)
          .append(", Stock: ").append(this.stock.getName())
          .append(", Quantity: ").append(this.stock.getSize())
          .append(", Compartment: ").append(this.compartmentName)
          .append(", Remaining Space: ").append(this.remaining)
          .append(", Date Added: ").append(this.timestamp.format(dateFormatter));

        List<Stock.SpecialQuality> qualities = this.stock.getQualities();
        if (qualities != null && !qualities.isEmpty()) {
            sb.append(", Special Qualities: ");
            sb.append(String.join(", ", qualities.stream().map(Enum::name).toList()));
        }

        if (this.stock.getExpirationDate() != null) {
            sb.append(", Expiration Date: ").append(this.stock.getExpirationDate().format(dateFormatter));
        }

        return sb.toString();
    }
}
