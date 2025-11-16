package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    @FXML private Button HomeButton;

    private String occupation;
    private Inventory inventory;

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        typeChoice.getItems().addAll("Food", "Munitions", "Other");
        conditionChoiceBox.getItems().addAll("New", "Used", "Damaged");
        sqChoiceBox.getItems().addAll("Perishable", "N/A");
    }

    @FXML
    private void onAddStock(ActionEvent event) {
        //try {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String compartmentName = typeChoice.getValue();
            String condition = conditionChoiceBox.getValue();
            String expDate = datePicker.getValue().toString();
            String sq = sqChoiceBox.getValue();

            Compartment selectedComp = inventory.getCompartments().stream()
                .filter(c -> c.getName().equalsIgnoreCase(compartmentName))
                .findFirst()
                .orElse(null);

            if (selectedComp != null && selectedComp.hasFreeSpace()) {
                Stock newStock = new Stock(name, quantity, condition, expDate, sq);
                selectedComp.addStock(newStock);
                addStockStatus.setText("Stock added!");
            } else {
                addStockStatus.setText("No free space in selected compartment.");
            }
        //} catch (Exception e) {
           //addStockStatus.setText("Error: " + e.getMessage());
        //}
    }

    @FXML
    private void onClearForm(ActionEvent event) {
        nameField.clear();
        quantityField.clear();
        typeChoice.getSelectionModel().clearSelection();
        conditionChoiceBox.getSelectionModel().clearSelection();
        sqChoiceBox.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        addStockStatus.setText("");
    }

    @FXML
    private void onHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
            Scene scene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setOccupation(this.occupation);
            controller.setInventory(this.inventory);

            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogout(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Pirate Ship Inventory Management System");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
