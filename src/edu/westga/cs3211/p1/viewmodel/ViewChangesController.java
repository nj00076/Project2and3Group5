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

public class ViewChangesController {

    @FXML
    private ListView<String> changeList;

    @FXML
    private DatePicker lowerBoundDatePicker;

    @FXML
    private DatePicker upperBoundDatePicker;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ListView<String> sqListView;

    private Inventory inventory;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        this.loadChangeList();
        this.sqListView.getItems().clear();
        this.sqListView.getItems().addAll("Perishable", "Flammable", "Liquid");
        this.sqListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        this.sqListView.getSelectionModel().selectAll();
    }

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

    @FXML
    private void onHomeButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pirate Ship Inventory Management System - Home");
    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pirate Ship Inventory Management System - Login");
    }

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
                var date = change.getTimestamp().toLocalDate();
                if (start != null && date.isBefore(start)) {
                	return false;
                }
                if (end != null && date.isAfter(end)) {
                	return false;
                }
                return true;
            })
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .forEach(change -> this.changeList.getItems().add(this.formatChange(change)));
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.loadChangeList();
    }

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

    private void loadChangeList() {
        this.changeList.getItems().clear();
        InventoryStore.getChangeLog().stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .forEach(change -> this.changeList.getItems().add(this.formatChange(change)));
    }
}
