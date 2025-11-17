package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Stock;

public class HomePageController {

    @FXML private Label usernameLabel;
    @FXML private Button addStockButton;
    @FXML private Button viewStockChangesButton;
    @FXML private Button logoutButton;
    @FXML private ListView<String> stockList;

    private String occupation;
    private Inventory inventory;
    private String username;

    @FXML
    public void initialize() {
        this.setInventory(InventoryStore.getInventory());
    }

    public void setUsername(String enteredUsername) {
        if (this.usernameLabel != null) {
            this.usernameLabel.setText(enteredUsername);
        }
        this.username = enteredUsername;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
        this.updateButtonState();
    }

    private void updateButtonState() {
        if (this.viewStockChangesButton != null && this.occupation != null) {
            this.viewStockChangesButton.setDisable(!"Quartermaster".equalsIgnoreCase(this.occupation));
        }
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.updateStockList();
    }

    private void updateStockList() {
        this.stockList.getItems().clear();
        if (this.inventory == null) {
            return;
        }

        for (Compartment comp : this.inventory.getCompartments()) {
            for (Stock stock : comp.getStockList()) {

                String qualitiesText = stock.getQualities().toString();

                String item = String.format(
                    "%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s",
                    comp.getName(),
                    stock.getName(),
                    stock.getCondition(),
                    stock.getSize(),
                    stock.getExpirationDate(),
                    qualitiesText
                );

                this.stockList.getItems().add(item);
            }
        }
    }

    @FXML
    private void onAddStock() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/edu/westga/cs3211/p1/view/AddStock.fxml")
        );

        Parent root = loader.load();
        AddStockController controller = loader.getController();

        controller.setInventory(InventoryStore.getInventory());
        controller.setOccupation(this.occupation);
        controller.setUsername(this.username);

        Stage stage = (Stage) this.addStockButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Pirate Ship Inventory Management System - Add Stock");
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

    @FXML
    private void onViewStockChanges(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/edu/westga/cs3211/p1/view/ViewChanges.fxml")
            );
            Parent root = loader.load();
            ViewChangesController controller = loader.getController();
            controller.setInventory(InventoryStore.getInventory());
            controller.setUsername(this.username);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pirate Ship Inventory Management System - View Stock Changes");
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
