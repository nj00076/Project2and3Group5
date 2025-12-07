package edu.westga.cs3211.p1.viewmodel;

import java.io.IOException;

import edu.westga.cs3211.p1.model.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RemoveStockOfficerController {
	
    private String occupation;
    private String username;
    private Inventory inventory;

    
    /**
     * Sets the occupation of the current user.
     *
     * @param occupation the occupation string
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    
    /**
     * Sets the inventory
     *
     * @param inventory the Inventory object
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
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
     * Logs out the current user and navigates to the Login page.
     *
     * @param event the ActionEvent triggered by the logout button
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
