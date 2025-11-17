package edu.westga.cs3211.p1.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StockChange {

    private String username;
    private Stock stock;
    private String compartmentName;
    private int remaining;
    private LocalDateTime timestamp;

    public StockChange(String username, Stock stock, String compartmentName, int remaining) {
        this.username = username;
        this.stock = stock;
        this.compartmentName = compartmentName;
        this.remaining = remaining;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() {
        return this.username;
    }

    public Stock getStock() {
        return this.stock;
    }

    public String getCompartmentName() {
        return this.compartmentName;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

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
