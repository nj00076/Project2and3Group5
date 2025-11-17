package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.StockChange;

/**
 * Controller class for the "View Stock Changes" screen.
 * Allows filtering of stock changes by username, special quality, and date range.
 * @author nj00076
 * @version cs3211
 */
public class ViewChangesController {

    @FXML private ListView<String> changeList;
    @FXML private DatePicker lowerBoundDatePicker;
    @FXML private DatePicker upperBoundDatePicker;
    @FXML private TextField usernameTextField;
    @FXML private ListView<String> sqListView;

    // Both are used but say they are unused for some reason
    @SuppressWarnings("unused")
	private Inventory inventory;
    @SuppressWarnings("unused")
	private String username;

    /**
     * Sets the currently logged-in username.
     * 
     * @param username the username of the current user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     * Loads all stock changes and sets up the special quality filter.
     */
    @FXML
    public void initialize() {
        this.loadChangeList();
        this.sqListView.getItems().clear();
        this.sqListView.getItems().addAll("Perishable", "Flammable", "Liquid");
        this.sqListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        this.sqListView.getSelectionModel().selectAll();
    }

    /**
     * Filters the change list by username input.
     * 
     * @param event the ActionEvent triggered by the username filter
     */
    @FXML
    private void onUsernameFilter(ActionEvent event) {
        String user = this.usernameTextField.getText();
        if (user == null || user.isBlank()) {
            this.loadChangeList();
            return;
        }
        this.changeList.getItems().clear();
        InventoryStore.getChangeLog().stream()
            .filter(change -> change.getUsername().equalsIgnoreCase(user))
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .forEach(change -> this.changeList.getItems().add(this.formatChange(change)));
    }

    /**
     * Navigates back to the Home Page.
     * 
     * @param event the ActionEvent triggered by the Home button
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private void onHomeButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pirate Ship Inventory Management System - Home");
    }

    /**
     * Logs the user out and returns to the Login screen.
     * 
     * @param event the ActionEvent triggered by the Logout button
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pirate Ship Inventory Management System - Login");
    }

    /**
     * Filters the change list by selected special qualities (SQs).
     * 
     * @param event the ActionEvent triggered by the SQ filter
     */
    @FXML
    private void onSQFilter(ActionEvent event) {
        List<String> selectedSQs = this.sqListView.getSelectionModel().getSelectedItems();
        if (selectedSQs.isEmpty()) {
            this.loadChangeList();
            return;
        }
        this.changeList.getItems().clear();
        InventoryStore.getChangeLog().stream()
            .filter(change -> change.getStock().getQualities().stream()
                .anyMatch(q -> selectedSQs.stream()
                    .anyMatch(sq -> sq.equalsIgnoreCase(q.name()))))
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .forEach(change -> this.changeList.getItems().add(this.formatChange(change)));
    }

    /**
     * Filters the change list by a date range.
     * 
     * @param event the ActionEvent triggered by the date filter
     */
    @FXML
    private void onFilterDate(ActionEvent event) {
        var start = this.lowerBoundDatePicker.getValue();
        var end = this.upperBoundDatePicker.getValue();
        if (start == null && end == null) {
            this.loadChangeList();
            return;
        }
        if (start != null && end != null && end.isBefore(start)) {
            this.changeList.getItems().clear();
            this.changeList.getItems().add("Invalid time range: end must be after start.");
            return;
        }
        this.changeList.getItems().clear();
        InventoryStore.getChangeLog().stream()
            .filter(change -> {
            	boolean tOrF = false;
            	if (change.getStock().getExpirationDate() != null) {
            		var date = change.getStock().getExpirationDate();
            		if (start != null && date.isAfter(start)) {
            			tOrF = true;
            		}
            		if (end != null && date.isBefore(end)) {
            			tOrF = true;
            		}
            		if (start != null && end != null && date.isEqual(end) && date.isEqual(start)) {
            			tOrF = true;
            		}
            	}
            	return tOrF;
            })
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .forEach(change -> this.changeList.getItems().add(this.formatChange(change)));
    }

    /**
     * Sets the inventory instance for the controller.
     * 
     * @param inventory the Inventory instance
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.loadChangeList();
    }

    /**
     * Formats a StockChange object into a readable string for display in the ListView.
     * 
     * @param change the StockChange object
     * @return formatted string representation
     */
    private String formatChange(StockChange change) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String sq;
        if (change.getStock().getQualities().isEmpty()) {
            sq = "N/A";
        } else {
            sq = change.getStock().getQualities().stream()
                    .map(Enum::name)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("N/A");
        }
        String expDate;
        if (change.getStock().getExpirationDate() == null) {
            expDate = "N/A";
        } else {
            expDate = change.getStock().getExpirationDate().format(formatter);
        }
        String time = change.getTimestamp().format(formatter);
        return String.format(
                "User: %s | Item: %s | Qty: %d | Compartment: %s | Remaining: %d | Date Added: %s | Special Qualities: %s | Expiration: %s",
                change.getUsername(),
                change.getStock().getName(),
                change.getStock().getQuantity(),
                change.getCompartmentName(),
                change.getRemaining(),
                time,
                sq,
                expDate
        );
    }

    /**
     * Loads the full change list into the ListView sorted by timestamp descending.
     */
    private void loadChangeList() {
        this.changeList.getItems().clear();
        InventoryStore.getChangeLog().stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .forEach(change -> this.changeList.getItems().add(this.formatChange(change)));
    }
}
