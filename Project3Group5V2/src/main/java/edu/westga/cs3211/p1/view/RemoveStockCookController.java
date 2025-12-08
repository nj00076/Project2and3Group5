package edu.westga.cs3211.p1.view;

import java.io.IOException;
import java.util.Optional;

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

/**
 * Controller for the Cook Remove Stock UI screen. Handles removal of food stock
 * items from the Food compartment. Cooks can only remove food items and must
 * provide a reason.
 * 
 * @author jo00087
 * @version cs3211
 */
public class RemoveStockCookController {
	private String occupation;
	private String username;
	private Inventory inventory;

	@FXML
	private ListView<String> foodListView;

	@FXML
	private TextField reasonTextField;

	@FXML
	private Label errorLabel;

	private ObservableList<String> foodList;

	/**
	 * Sets the occupation of the current user.
	 *
	 * @param occupation the occupation
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * Sets the username of the current user.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Sets the inventory to display and loads the food list.
	 *
	 * @param inventory the inventory
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
		this.loadFoodList();
	}

	/**
	 * Loads the food compartment and populates the list view.
	 */
	private void loadFoodList() {
		Compartment food = this.getFoodCompartment();
		this.foodList = FXCollections.observableArrayList();

		if (food != null) {
			for (Stock stock : food.getStockList()) {
				String qualitiesText = stock.getQualities().toString();
				String formatted = String.format("%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s", food.getName(),
						stock.getName(), stock.getCondition(), stock.getSize(), stock.getExpirationDate(),
						qualitiesText);
				this.foodList.add(formatted);
			}
		}

		this.foodListView.setItems(this.foodList);

		if (this.errorLabel != null) {
			this.errorLabel.setText("");
		}
	}

	/**
	 * Handles removing a selected food stock item from the food compartment.
	 *
	 * @param event the ActionEvent triggered by the remove button
	 */
	@FXML
	private void onRemoveStock(ActionEvent event) {
		if (this.errorLabel != null) {
			this.errorLabel.setText("");
		}

		Compartment food = this.getFoodCompartment();
		if (food == null) {
			if (this.errorLabel != null) {
				this.errorLabel.setText("No food compartment found.");
			}
			return;
		}

		String selectedString = this.foodListView.getSelectionModel().getSelectedItem();
		if (selectedString == null) {
			if (this.errorLabel != null) {
				this.errorLabel.setText("Please select a food item to remove.");
			}
			return;
		}

		String reason = this.reasonTextField.getText();
		if (reason == null || reason.isBlank()) {
			if (this.errorLabel != null) {
				this.errorLabel.setText("You must enter a reason to remove this item.");
			}
			return;
		}

		Stock selectedStock = this.findSelectedStock(food, selectedString);
		if (selectedStock == null) {
			if (this.errorLabel != null) {
				this.errorLabel.setText("Selected food item not found in inventory.");
			}
			return;
		}

		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setTitle("Confirm Food Removal");
		confirm.setHeaderText("Are you sure you want to remove this food item?");
		confirm.setContentText("Food Item: " + selectedStock.getName() + "\nQuantity: " + selectedStock.getSize()
				+ "\nReason: " + reason);

		Optional<ButtonType> userChoice = confirm.showAndWait();
		if (userChoice.isEmpty() || userChoice.get() != ButtonType.OK) {
			return;
		}

		food.getStockList().remove(selectedStock);
		this.foodList.remove(selectedString);

		this.logStockChange(selectedStock, food, reason);

		this.reasonTextField.clear();
		this.errorLabel.setText("Food item successfully removed from inventory.");
	}

	/**
	 * Retrieves the food compartment from the inventory.
	 *
	 * @return the food compartment, or null if not found
	 */
	private Compartment getFoodCompartment() {
		for (Compartment compartment : this.inventory.getCompartments()) {
			if (compartment.getName().equalsIgnoreCase("Food")) {
				return compartment;
			}
		}
		return null;
	}

	/**
	 * Finds the stock object corresponding to the selected list item string.
	 *
	 * @param food           the food compartment
	 * @param selectedString the string selected in the list view
	 * @return the corresponding Stock object, or null if not found
	 */
	private Stock findSelectedStock(Compartment food, String selectedString) {
		for (Stock stock : food.getStockList()) {
			String formatted = String.format("%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s", food.getName(),
					stock.getName(), stock.getCondition(), stock.getSize(), stock.getExpirationDate(),
					stock.getQualities().toString());
			if (formatted.equals(selectedString)) {
				return stock;
			}
		}
		return null;
	}

	/**
	 * Logs the removal of a food stock item to the inventory change log.
	 *
	 * @param stock  the removed stock
	 * @param food   the food compartment
	 * @param reason the reason for removal
	 */
	private void logStockChange(Stock stock, Compartment food, String reason) {
		StockChange change = new StockChange(this.username, stock, "Food", food.getFreeSpace(),
				"Removed Food Stock: " + reason);
		InventoryStore.addChangeLogEntry(change);
	}

	/**
	 * Navigates back to the home page.
	 *
	 * @param event the ActionEvent triggered by the home button
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
			stage.setTitle("Pirate Ship Inventory Management System - Home");
			stage.show();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Logs out the current user and navigates to the login page.
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