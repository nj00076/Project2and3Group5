package edu.westga.cs3211.p1.view;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.List;
import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.Stock;

/**
 * Controller for the Report Damage screen.
 * 
 * @author vfilpo :)
 * @version cs3211
 */
public class ReportDamageController {

    @FXML private Label addStockStatus;
    @FXML private Button addButton;
    @FXML private Button logoutButton;
    @FXML private Button homeButton;
    @FXML private ListView<String> stockList;
    @FXML private ChoiceBox<String> conditionChoiceBox;
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
        if (inventory != null) {
    		for (Compartment comp : this.inventory.getCompartments()) {
    			for (Stock stock : comp.getStockList()) {
    				String qualitiesText = stock.getQualities().toString();
    				String item = String.format("%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s", comp.getName(),
    						stock.getName(), stock.getCondition(), stock.getSize(), stock.getExpirationDate(),
    						qualitiesText);
    				this.stockList.getItems().add(item);
    			}
    		}
            this.conditionChoiceBox.getItems().setAll("PERFECT", "USABLE", "UNUSABLE");
        }
    }
    
    /**
     * Handles finalizing damage report.
     * 
     * @param event the action event
     */
    @FXML
    private void onFinishReport(ActionEvent event) {

        String selectedString = this.stockList.getSelectionModel().getSelectedItem();
        int selectedIndex = this.stockList.getSelectionModel().getSelectedIndex();

        if (selectedString == null || selectedIndex == -1) {
            return; 
        }

        String compartmentName = selectedString.split(" ")[0].trim();

        Compartment selectedCompartment = null;
        for (Compartment compartment : InventoryStore.getInventory().getCompartments()) {
            if (compartment.getName().equalsIgnoreCase(compartmentName)) {
                selectedCompartment = compartment;
                break;
            }
        }

        if (selectedCompartment == null) {
            return; 
        }

        List<Stock> stockList = selectedCompartment.getStockList();

        if (selectedIndex < 0 || selectedIndex >= stockList.size()) {
            return;
        }

        Stock targetStock = stockList.get(selectedIndex);
        
      String conditionStr = this.conditionChoiceBox.getSelectionModel().getSelectedItem();
      Stock.Condition conditionEnum = this.conditionSwitch(conditionStr);
        targetStock.setCondition(conditionEnum);
        this.stockList.refresh();
        
		}

    private Stock.Condition conditionSwitch(String conditionStr) {
        switch (conditionStr.toLowerCase()) {
            case "new":     return Stock.Condition.PERFECT;
            case "used":    return Stock.Condition.USABLE;
            case "damaged": return Stock.Condition.UNUSABLE;
            default:        return Stock.Condition.USABLE;
        }
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
