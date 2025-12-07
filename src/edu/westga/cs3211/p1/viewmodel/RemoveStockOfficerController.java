package edu.westga.cs3211.p1.viewmodel;

import java.io.IOException;
import java.util.Optional;

import edu.westga.cs3211.p1.model.ChangeLogStore;
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

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        loadMunitions();
    }

    private void loadMunitions() {
        Compartment munitions = null;
        for (Compartment c : inventory.getCompartments()) {
            if (c.getName().equalsIgnoreCase("Munitions")) {
                munitions = c;
                break;
            }
        }

        munitionsList = FXCollections.observableArrayList();
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
                munitionsList.add(formatted);
            }
        }

        munitionsListView.setItems(munitionsList);
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    @FXML
    private void onRemoveStock(ActionEvent event) {
        if (errorLabel != null) {
            errorLabel.setText("");
        }

        Compartment munitions = null;
        for (Compartment c : inventory.getCompartments()) {
            if (c.getName().equalsIgnoreCase("Munitions")) {
                munitions = c;
                break;
            }
        }
        if (munitions == null) {
            if (errorLabel != null) {
                errorLabel.setText("No munitions compartment found.");
            }
            return;
        }

        String selectedString = munitionsListView.getSelectionModel().getSelectedItem();
        if (selectedString == null) {
            if (errorLabel != null) {
                errorLabel.setText("Please select a stock item to remove.");
            }
            return;
        }

        String reason = reasonTextField.getText();
        if (reason == null || reason.isBlank()) {
            if (errorLabel != null) {
                errorLabel.setText("You must enter a reason to remove this item.");
            }
            return;
        }

        Stock selectedStock = null;
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
            if (formatted.equals(selectedString)) {
                selectedStock = stock;
                break;
            }
        }

        if (selectedStock == null) {
            if (errorLabel != null) {
                errorLabel.setText("Selected stock item not found in inventory.");
            }
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Are you sure you want to remove this stock?");
        confirm.setContentText(
            "Stock: " + selectedStock.getName()
            + "\nQuantity: " + selectedStock.getSize()
            + "\nReason: " + reason
        );

        Optional<ButtonType> userChoice = confirm.showAndWait();
        if (userChoice.isEmpty() || userChoice.get() != ButtonType.OK) {
            return;
        }

        munitions.getStockList().remove(selectedStock);
        munitionsList.remove(selectedString);
        StockChange change = new StockChange(username, selectedStock, "Munitions", munitions.getFreeSpace(), "Removed Stock: " + reason);
        InventoryStore.addChangeLogEntry(change);
        reasonTextField.clear();
    }

    @FXML
    private void onHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/edu/westga/cs3211/p1/view/HomePage.fxml"
            ));
            Scene scene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setOccupation(occupation);
            controller.setInventory(inventory);
            controller.setUsername(username);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pirate Ship Inventory Management System - Home");
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

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
