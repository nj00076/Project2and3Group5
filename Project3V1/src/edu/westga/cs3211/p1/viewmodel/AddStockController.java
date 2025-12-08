package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.Stock;
import edu.westga.cs3211.p1.model.StockChange;

/**
 * Controller for the Add Stock UI screen.
 * Handles form input validation, stock creation, adding to inventory, and updating the change log.
 * @author nj00076
 * @version cs3211
 */
public class AddStockController {

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<String> typeChoice;
    @FXML private ChoiceBox<String> conditionChoiceBox;
    @FXML private ChoiceBox<String> sqChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private Label addStockStatus;
    @FXML private Button addButton;
    @FXML private Button logoutButton;
    @FXML private Button homeButton;

    private String occupation;
    private Inventory inventory;
    private String username;

    /**
     * Sets the occupation of the current user.
     *
     * @param occupation the occupation string
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * Sets the username of the current user.
     *
     * @param username the username string
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the inventory for this controller and populates UI choice boxes.
     *
     * @param inventory the Inventory object
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.typeChoice.getItems().clear();
        if (inventory != null) {
            for (Compartment compartment : inventory.getCompartments()) {
                this.typeChoice.getItems().add(compartment.getName());
            }
        }
        this.conditionChoiceBox.getItems().setAll("PERFECT", "USABLE", "UNUSABLE");
        this.sqChoiceBox.getItems().setAll("N/A", "Perishable", "Flammable", "Liquid");
        this.datePicker.setValue(null);
    }

    /**
     * Handles the Add Stock button action.
     * Validates input, creates a Stock object, adds it to the selected compartment, and logs the change.
     *
     * @param event the action event
     */
    @FXML
    private void onAddStock(ActionEvent event) {
        this.addStockStatus.setText("");
        try {
            Stock newStock = this.createStockFromForm();
            if (newStock == null) {
            	return;
            }

            Compartment selectedComp = this.compartmentCheck(this.newStockCompartmentName());
            if (selectedComp == null) {
                this.addStockStatus.setText("Selected compartment not found.");
                return;
            }

            if (!this.checkCompartmentSpace(selectedComp, newStock)) {
            	return;
            }

            selectedComp.addStock(newStock);
            int remaining = selectedComp.getFreeSpace();
            String userToUse = this.resolveUsername();
            StockChange change = new StockChange(userToUse, newStock, selectedComp.getName(), remaining);
            InventoryStore.addChangeLogEntry(change);

            this.addStockStatus.setText("Stock added to " + selectedComp.getName()
             + ". Remaining capacity: " + remaining);
        } catch (Exception ex) {
            this.addStockStatus.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Resolves username for logging. Returns "unknown" if username is null.
     *
     * @return the username string
     */
    private String resolveUsername() {
        if (this.username == null) {
        	return "unknown";
        }
        return this.username;
    }

    /**
     * Creates a Stock object from the form fields, performing validation.
     *
     * @return the created Stock object or null if validation fails
     */
    private Stock createStockFromForm() {
        String name = this.nameField.getText();
        if (name == null || name.isBlank()) {
            this.addStockStatus.setText("Name is required.");
            return null;
        }

        String qtyText = this.quantityField.getText();
        if (qtyText == null || qtyText.isBlank()) {
            this.addStockStatus.setText("Quantity is required.");
            return null;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                this.addStockStatus.setText("Quantity must be a positive integer.");
                return null;
            }
        } catch (NumberFormatException nfe) {
            this.addStockStatus.setText("Quantity must be an integer.");
            return null;
        }

        String compartmentName = this.typeChoice.getValue();
        if (compartmentName == null) {
            this.addStockStatus.setText("Select a compartment.");
            return null;
        }

        String conditionStr = this.conditionChoiceBox.getValue();
        if (conditionStr == null) {
            this.addStockStatus.setText("Select a condition.");
            return null;
        }

        Stock.Condition conditionEnum = this.conditionSwitch(conditionStr);
        List<Stock.SpecialQuality> qualities = this.sqSelectionSetup();
        LocalDate expiration = null;
        if (qualities.contains(Stock.SpecialQuality.PERISHABLE)) {
            expiration = this.datePicker.getValue();
            if (expiration == null) {
                this.addStockStatus.setText("Expiration date required for perishable stock.");
                return null;
            }
        }

        return new Stock(name, quantity, conditionEnum, qualities, expiration, quantity);
    }

    /**
     * Checks whether the selected compartment has sufficient space for the new stock.
     *
     * @param selectedComp the selected Compartment
     * @param newStock     the Stock to add
     * @return true if there is sufficient space; false otherwise
     */
    private boolean checkCompartmentSpace(Compartment selectedComp, Stock newStock) {
        int free = selectedComp.getFreeSpace();
        if (free >= newStock.getSize()) {
        	return true;
        }

        List<String> candidates = new ArrayList<>();
        if (this.inventory != null) {
            candidates = this.inventory.getCompartments().stream()
                    .filter(c -> c.getFreeSpace() >= newStock.getSize())
                    .map(Compartment::getName)
                    .collect(Collectors.toList());
        }

        if (candidates.isEmpty()) {
            this.addStockStatus.setText("No storage compartment has sufficient space for this quantity.");
        } else {
            this.addStockStatus.setText("Selected compartment lacks space. Other compartments with capacity: "
                    + String.join(", ", candidates));
        }
        return false;
    }

    private String newStockCompartmentName() {
        return this.typeChoice.getValue();
    }

    private Stock.Condition conditionSwitch(String conditionStr) {
        switch (conditionStr.toLowerCase()) {
            case "new":     return Stock.Condition.PERFECT;
            case "used":    return Stock.Condition.USABLE;
            case "damaged": return Stock.Condition.UNUSABLE;
            default:        return Stock.Condition.USABLE;
        }
    }

    private Compartment compartmentCheck(String compartmentName) {
        if (this.inventory != null) {
            return this.inventory.getCompartments().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(compartmentName))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private List<Stock.SpecialQuality> sqSelectionSetup() {
        String sqSelection = this.sqChoiceBox.getValue();
        List<Stock.SpecialQuality> qualities = new ArrayList<>();
        if (sqSelection != null) {
            switch (sqSelection.toLowerCase()) {
                case "perishable": qualities.add(Stock.SpecialQuality.PERISHABLE); 
                break;
                case "flammable":  qualities.add(Stock.SpecialQuality.FLAMMABLE);  
                break;
                case "liquid":     qualities.add(Stock.SpecialQuality.LIQUID);     
                break;
                default: break;
            }
        }
        return qualities;
    }

    /**
     * Clears all fields in the Add Stock form.
     *
     * @param event the action event
     */
    @FXML
    private void onClearForm(ActionEvent event) {
        this.nameField.clear();
        this.quantityField.clear();
        this.typeChoice.getSelectionModel().clearSelection();
        this.conditionChoiceBox.getSelectionModel().clearSelection();
        this.sqChoiceBox.getSelectionModel().clearSelection();
        this.datePicker.setValue(null);
        this.addStockStatus.setText("");
    }

    /**
     * Handles the Home button action, navigating to the Home Page scene.
     *
     * @param event the action event
     */
    @FXML
    private void onHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
            Scene scene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setOccupation(this.occupation);
            controller.setInventory(this.inventory);
            controller.setUsername(this.username);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pirate Ship Inventory Management System -  Home");
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the Logout button action, navigating to the Login scene.
     *
     * @param event the action event
     */
    @FXML
    private void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Pirate Ship Inventory Management System - Login");
            currentStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
