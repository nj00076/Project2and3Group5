package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

import edu.westga.cs3211.p1.model.ChangeLogStore;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.InventoryStore;
import edu.westga.cs3211.p1.model.StockChange;

public class ViewChangesController {

    @FXML
    private ListView<String> changeList;
    
	private Inventory inventory;
	private String username;
	
	public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        this.changeList.getItems().clear();

        for (StockChange change : ChangeLogStore.getChanges()) {
        	this.changeList.getItems().add(change.toString());
        }
    }

    @FXML
    private void onHomeButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pirate Ship Inventory Management System -  Home");
    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pirate Ship Inventory Management System - Login");

    }

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	    this.loadChangeList();		
	}

	private void loadChangeList() {
		this.changeList.getItems().clear();

	    for (StockChange change : InventoryStore.getChangeLog()) {
	        String record = String.format(
	            "User: %s | Item: %s | Qty: %d | Compartment: %s | Time: %s",
	            change.getUsername().toString(),
	            change.getItemName().toString(),
	            change.getQuantity(),
	            change.getCompartment().toString(),
	            change.getTimestamp().toString()
	        );
	        this.changeList.getItems().add(record);
	    }		
	}
}
