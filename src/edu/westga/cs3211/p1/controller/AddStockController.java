package edu.westga.cs3211.p1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.io.IOException;

public class AddStockController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField quantityField;

    @FXML
    private ChoiceBox<String> typeChoice;

    @FXML
    private Label addStockStatus;

    @FXML
    private Button addButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button HomeButton;

    private String occupation;

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @FXML
    private void onAddStock(ActionEvent event) {}

    @FXML
    private void onClearForm(ActionEvent event) {
        nameField.clear();
        quantityField.clear();
        typeChoice.getSelectionModel().clearSelection();
        addStockStatus.setText("");
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

    @FXML
    private void onHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
            Scene homeScene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setOccupation(this.occupation);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(homeScene);
            currentStage.setTitle("Pirate Ship Inventory System - Home");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
