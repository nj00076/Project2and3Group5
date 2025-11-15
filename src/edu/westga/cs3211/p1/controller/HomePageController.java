package edu.westga.cs3211.p1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;

public class HomePageController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button addStockButton;

    @FXML
    private Button viewStockChangesButton;

    @FXML
    private Button logoutButton;
    
    private String occupation;

    @FXML
    private void onAddStock(ActionEvent event) {
    	 try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/AddStock.fxml"));
    	        Scene addStockScene = new Scene(loader.load());
    	        AddStockController controller = loader.getController();
    	        controller.setOccupation(this.occupation);
    	        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	        currentStage.setScene(addStockScene);
    	        currentStage.setTitle("Pirate Ship Inventory System - Add Stock");
    	        currentStage.show();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    }

    @FXML
    private void onViewStockChanges(ActionEvent event) {}

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

    public void setUsername(String enteredUsername) {
        usernameLabel.setText(enteredUsername);
    }

    public void setOccupation(String occupation) {
    	this.occupation = occupation;
        if (!"Quartermaster".equalsIgnoreCase(occupation)) {
            viewStockChangesButton.setDisable(true);
        } else {
            viewStockChangesButton.setDisable(false);
        }
    }
}
