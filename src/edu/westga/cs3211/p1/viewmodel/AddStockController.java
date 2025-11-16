package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Stock;
import edu.westga.cs3211.p1.model.StockChange;
import edu.westga.cs3211.p1.model.InventoryStore;

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

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.typeChoice.getItems().addAll("Food", "Munitions", "Other");
        this.conditionChoiceBox.getItems().addAll("New", "Used", "Damaged");
        this.sqChoiceBox.getItems().addAll("Perishable", "N/A");
    }

    @FXML
    private void onAddStock(ActionEvent event) {
        try {
            String name = this.nameField.getText();
            int quantity = Integer.parseInt(this.quantityField.getText());
            String compartmentName = this.typeChoice.getValue();
            String condition = this.conditionChoiceBox.getValue();
            String expDate = this.datePicker.getValue().toString();
            String sq = this.sqChoiceBox.getValue();

            Compartment selectedComp = this.inventory.getCompartments().stream()
                .filter(c -> c.getName().equalsIgnoreCase(compartmentName))
                .findFirst()
                .orElse(null);

            if (selectedComp != null && selectedComp.hasFreeSpace()) {
                Stock newStock = new Stock(name, quantity, condition, expDate, sq);
                selectedComp.addStock(newStock);
                String username = this.username;
                InventoryStore.addChangeLogEntry(new StockChange(username, name, quantity, compartmentName));

                this.addStockStatus.setText("Stock added!");
            } else {
                this.addStockStatus.setText("No free space in selected compartment.");
            }

        } catch (Exception exception) {
            this.addStockStatus.setText("Error: " + exception.getMessage());
        }
    }

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

    @FXML
    private void onHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml")
            );

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

    @FXML
    private void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml")
            );

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
