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

    @FXML
    public void initialize() {
        setInventory(InventoryStore.getInventory());
    }

    public void setUsername(String enteredUsername) {
        usernameLabel.setText(enteredUsername);
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
        viewStockChangesButton.setDisable(!"Quartermaster".equalsIgnoreCase(occupation));
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateStockList();
    }

    private void updateStockList() {
        stockList.getItems().clear();

        if (inventory == null) {
            return;
        }

        for (Compartment comp : inventory.getCompartments()) {
            for (Stock stock : comp.getStockList()) {

                String item = String.format(
                        "%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s",
                        comp.getName(),
                        stock.getName(),
                        stock.getCondition(),
                        stock.getSize(),
                        stock.getExpirationDate(),
                        stock.getSpecialQuals()
                );

                stockList.getItems().add(item);
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

        Stage stage = (Stage) this.addStockButton.getScene().getWindow();
        stage.setScene(new Scene(root));
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
            currentStage.setTitle("Pirate Ship Inventory Management System");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewStockChanges(ActionEvent event) {
    }
}
