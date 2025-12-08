package edu.westga.cs3211.p1.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import edu.westga.cs3211.p1.model.Compartment;
import edu.westga.cs3211.p1.model.Inventory;
import edu.westga.cs3211.p1.model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Controller for the View Food Expiration UI screen. Allows cooks to view food
 * items that expire by a certain date.
 * 
 * @author jo00087
 * @version cs3211
 */
public class ViewExpirationController {
	private String occupation;
	private String username;
	private Inventory inventory;

	@FXML
	private DatePicker expirationDatePicker;

	@FXML
	private ListView<String> foodListView;

	@FXML
	private Label statusLabel;

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
	 * Sets the inventory to display.
	 *
	 * @param inventory the inventory
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
		this.loadAllFoodItems();
	}

	/**
	 * Initializes the controller. Sets up the date picker with today's date as
	 * default.
	 */
	@FXML
	public void initialize() {
		this.expirationDatePicker.setValue(LocalDate.now());
		this.loadAllFoodItems();
	}

	/**
	 * Filters food items by expiration date. Shows only food items that expire on
	 * or before the selected date.
	 *
	 * @param event the ActionEvent triggered by the filter button
	 */
	@FXML
	private void onFilterByDate(ActionEvent event) {
		LocalDate filterDate = this.expirationDatePicker.getValue();

		if (filterDate == null) {
			this.statusLabel.setText("Please select a date to filter by.");
			return;
		}

		Compartment food = this.getFoodCompartment();
		if (food == null) {
			this.statusLabel.setText("No food compartment found.");
			return;
		}

		ObservableList<String> filteredList = FXCollections.observableArrayList();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		int itemCount = 0;

		for (Stock stock : food.getStockList()) {
			LocalDate expirationDate = stock.getExpirationDate();

			if (expirationDate != null && stock.getQualities().contains(Stock.SpecialQuality.PERISHABLE)) {

				if (!expirationDate.isAfter(filterDate)) {
					String formattedItem = this.formatFoodItem(stock, filterDate);
					filteredList.add(formattedItem);
					itemCount++;
				}
			}
		}

		this.foodListView.setItems(filteredList);

		if (itemCount == 0) {
			this.statusLabel.setText("No food items expire on or before " + filterDate.format(formatter) + ".");
		} else {
			this.statusLabel.setText(
					"Found " + itemCount + " food item(s) expiring on or before " + filterDate.format(formatter) + ".");
		}
	}

	/**
	 * Loads all food items from the food compartment.
	 * @param event show food
	 */
	@FXML
	private void onShowAllFood(ActionEvent event) {
		this.loadAllFoodItems();
	}

	/**
	 * Loads all food items regardless of expiration date.
	 */
	private void loadAllFoodItems() {
		Compartment food = this.getFoodCompartment();
		if (food == null) {
			this.statusLabel.setText("No food compartment found.");
			this.foodListView.getItems().clear();
			return;
		}

		ObservableList<String> allFoodList = FXCollections.observableArrayList();

		for (Stock stock : food.getStockList()) {
			String formattedItem = this.formatFoodItem(stock, LocalDate.now());
			allFoodList.add(formattedItem);
		}

		this.foodListView.setItems(allFoodList);
		this.statusLabel.setText("Showing all food items (" + allFoodList.size() + " items).");
	}

	/**
	 * Formats a food item for display with color coding.
	 *
	 * @param stock       the stock item
	 * @param currentDate the current date for comparison
	 * @return formatted string with color coding
	 */
	private String formatFoodItem(Stock stock, LocalDate currentDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate expirationDate = stock.getExpirationDate();

		String expirationText;
		String colorIndicator = "";

		if (expirationDate != null) {
			expirationText = expirationDate.format(formatter);

			long daysUntilExpiry = ChronoUnit.DAYS.between(currentDate, expirationDate);

			if (daysUntilExpiry <= 0) {
				colorIndicator = "[EXPIRED] ";
			} else if (daysUntilExpiry <= 3) {
				colorIndicator = "[URGENT] ";
			} else if (daysUntilExpiry <= 7) {
				colorIndicator = "[SOON] ";
			} else {
				colorIndicator = "[OK] ";
			}
		} else {
			expirationText = "No expiration";
			colorIndicator = "[NON-PERISHABLE] ";
		}

		return String.format("%s%s - %s - Expires: %s - Qty: %d - Condition: %s", colorIndicator, stock.getName(),
				this.getStockType(stock), expirationText, stock.getSize(), stock.getCondition());
	}

	/**
	 * Gets a readable type description for the stock item.
	 *
	 * @param stock the stock item
	 * @return type description string
	 */
	private String getStockType(Stock stock) {
		if (stock.getQualities().contains(Stock.SpecialQuality.LIQUID)) {
			return "Liquid Food";
		} else if (stock.getQualities().contains(Stock.SpecialQuality.PERISHABLE)) {
			return "Perishable Food";
		} else {
			return "Dry Food";
		}
	}

	/**
	 * Retrieves the food compartment from the inventory.
	 *
	 * @return the food compartment, or null if not found
	 */
	private Compartment getFoodCompartment() {
		if (this.inventory == null) {
			return null;
		}

		for (Compartment compartment : this.inventory.getCompartments()) {
			if (compartment.getName().equalsIgnoreCase("Food")) {
				return compartment;
			}
		}
		return null;
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