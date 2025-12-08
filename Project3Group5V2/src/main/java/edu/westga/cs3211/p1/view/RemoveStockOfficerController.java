package edu.westga.cs3211.p1.view;

import java.io.IOException;
import java.util.Optional;

import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.Stock;
import edu.westga.cs3211.p1.model.StockChange;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Remove Stock UI screen.
 * Handles form input validation, stock removal, removing from inventory, and updating the change log.
 * @author nj00076
 * @version cs3211
 */
public class RemoveStockOfficerController {

    private String occupation;
    private String username;
    private Inventory inventory;

    @FXML
    private ListView<String> munitionsListView;

    @FXML
    private TextField reasonTextField;

    @FXML
    private Label errorLabel;

    private ObservableList<String> munitionsList;

    /**
     * Sets the occupation of the current user.
     *
     * @param occupation the occupation
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * Sets the username of the current user.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the inventory to display and loads the munitions list.
     *
     * @param inventory the inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.loadMunitions();
    }

    /**
     * Loads the munitions compartment and populates the list view.
     */
    private void loadMunitions() {
        Compartment munitions = null;
        for (Compartment com : this.inventory.getCompartments()) {
            if (com.getName().equalsIgnoreCase("Munitions")) {
                munitions = com;
                break;
            }
        }

        this.munitionsList = FXCollections.observableArrayList();

        if (munitions != null) {
            for (Stock stock : munitions.getStockList()) {
                String qualitiesText = stock.getQualities().toString();
                String formatted = String.format(
                    "%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s",
                    munitions.getName(),
                    stock.getName(),
                    stock.getCondition(),
                    stock.getSize(),
                    stock.getExpirationDate(),
                    qualitiesText
                );
                this.munitionsList.add(formatted);
            }
        }

        this.munitionsListView.setItems(this.munitionsList);

        if (this.errorLabel != null) {
            this.errorLabel.setText("");
        }
    }

    /**
     * Handles removing a selected stock item from the munitions compartment.
     *
     * @param event the ActionEvent triggered by the remove button
     */
    @FXML
    private void onRemoveStock(ActionEvent event) {
        if (this.errorLabel != null) {
            this.errorLabel.setText("");
        }

        Compartment munitions = this.getMunitionsCompartment();
        if (munitions == null) {
            if (this.errorLabel != null) {
                this.errorLabel.setText("No munitions compartment found.");
            }
            return;
        }

        String selectedString = this.munitionsListView.getSelectionModel().getSelectedItem();
        if (selectedString == null) {
            if (this.errorLabel != null) {
                this.errorLabel.setText("Please select a stock item to remove.");
            }
            return;
        }

        String reason = this.reasonTextField.getText();
        if (reason == null || reason.isBlank()) {
            if (this.errorLabel != null) {
                this.errorLabel.setText("You must enter a reason to remove this item.");
            }
            return;
        }

        Stock selectedStock = this.findSelectedStock(munitions, selectedString);
        if (selectedStock == null) {
            if (this.errorLabel != null) {
                this.errorLabel.setText("Selected stock item not found in inventory.");
            }
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Are you sure you want to remove this stock?");
        confirm.setContentText("Stock: " + selectedStock.getName()
                + "\nQuantity: " + selectedStock.getSize()
                + "\nReason: " + reason);

        Optional<ButtonType> userChoice = confirm.showAndWait();
        if (userChoice.isEmpty() || userChoice.get() != ButtonType.OK) {
            return;
        }

        munitions.getStockList().remove(selectedStock);
        this.munitionsList.remove(selectedString);
        this.logStockChange(selectedStock, munitions, reason);
        this.reasonTextField.clear();
    }

    /**
     * Retrieves the munitions compartment from the inventory.
     *
     * @return the munitions compartment, or null if not found
     */
    private Compartment getMunitionsCompartment() {
        for (Compartment com : this.inventory.getCompartments()) {
            if (com.getName().equalsIgnoreCase("Munitions")) {
                return com;
            }
        }
        return null;
    }

    /**
     * Finds the stock object corresponding to the selected list item string.
     *
     * @param munitions the munitions compartment
     * @param selectedString the string selected in the list view
     * @return the corresponding Stock object, or null if not found
     */
    private Stock findSelectedStock(Compartment munitions, String selectedString) {
        for (Stock stock : munitions.getStockList()) {
            String formatted = String.format(
                "%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s",
                munitions.getName(),
                stock.getName(),
                stock.getCondition(),
                stock.getSize(),
                stock.getExpirationDate(),
                stock.getQualities().toString()
            );
            if (formatted.equals(selectedString)) {
                return stock;
            }
        }
        return null;
    }

    /**
     * Logs the removal of a stock item to the inventory change log.
     *
     * @param stock the removed stock
     * @param munitions the munitions compartment
     * @param reason the reason for removal
     */
    private void logStockChange(Stock stock, Compartment munitions, String reason) {
        StockChange change = new StockChange(this.username, stock, "Munitions",
                munitions.getFreeSpace(), "Removed Stock: " + reason);
        InventoryStore.addChangeLogEntry(change);
    }

    /**
     * Navigates back to the home page.
     *
     * @param event the ActionEvent triggered by the home button
     */
    @FXML
    private void onHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/edu/westga/cs3211/p1/view/HomePage.fxml"
            ));
            Scene scene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setOccupation(this.occupation);
            controller.setInventory(this.inventory);
            controller.setUsername(this.username);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pirate Ship Inventory Management System - Home");
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Logs out the current user and navigates to the login page.
     *
     * @param event the ActionEvent triggered by the logout button
     */
    @FXML
    private void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/edu/westga/cs3211/p1/view/Login.fxml"
            ));
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
