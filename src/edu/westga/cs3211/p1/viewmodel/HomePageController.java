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

/**
 * Controller for the Home Page of the Pirate Ship Inventory Management System.
 * Handles displaying stock, navigating to Add Stock or View Changes screens,
 * and logout functionality.
 * 
 * @author nj00076
 * @version cs3211
 */
public class HomePageController {
	@FXML
	private Label usernameLabel;
	@FXML
	private Button addStockButton;
	@FXML
	private Button removeStockButton;
	@FXML
	private Button viewStockChangesButton;
	@FXML
	private Button logoutButton;
	@FXML
	private ListView<String> stockList;
	private String occupation;
	private Inventory inventory;
	private String username;

	/**
	 * Initializes the Home Page controller. Sets the inventory from InventoryStore.
	 */
	@FXML
	public void initialize() {
		this.setInventory(InventoryStore.getInventory());
	}

	/**
	 * Sets the current username and updates the username label.
	 *
	 * @param enteredUsername the username of the current user
	 */
	public void setUsername(String enteredUsername) {
		if (this.usernameLabel != null) {
			this.usernameLabel.setText(enteredUsername);
		}
		this.username = enteredUsername;
	}

	/**
	 * Sets the current user's occupation and updates button accessibility.
	 *
	 * @param occupation the occupation string
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
		this.updateButtonState();
	}

	/**
	 * Sets the inventory and updates the stock list displayed.
	 *
	 * @param inventory the Inventory object
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
		this.updateStockList();
	}

	/**
	 * Navigates to the Add Stock page.
	 *
	 * @throws IOException if the FXML file cannot be loaded
	 */
	@FXML
	private void onAddStock() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/AddStock.fxml"));
		Parent root = loader.load();
		AddStockController controller = loader.getController();
		controller.setInventory(InventoryStore.getInventory());
		controller.setOccupation(this.occupation);
		controller.setUsername(this.username);
		Stage stage = (Stage) this.addStockButton.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Pirate Ship Inventory Management System - Add Stock");
	}

	/**
	 * Navigates to the Remove Stock page based on occupation. Officers go to
	 * RemoveStockOfficer, Cooks go to RemoveStockCook.
	 *
	 * @throws IOException if the FXML file cannot be loaded
	 */
	@FXML
	private void onRemoveStock() throws IOException {
		String fxmlFile;
		String titleSuffix;

		if ("Officer".equalsIgnoreCase(this.occupation)) {
			fxmlFile = "/edu/westga/cs3211/p1/view/RemoveStockOfficer.fxml";
			titleSuffix = "Remove Munitions Stock";
		} else if ("Cook".equalsIgnoreCase(this.occupation)) {
			fxmlFile = "/edu/westga/cs3211/p1/view/RemoveStockCook.fxml";
			titleSuffix = "Remove Food Stock";
		} else {
			return;
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
		Parent root = loader.load();

		if ("Officer".equalsIgnoreCase(this.occupation)) {
			RemoveStockOfficerController controller = loader.getController();
			controller.setInventory(InventoryStore.getInventory());
			controller.setOccupation(this.occupation);
			controller.setUsername(this.username);
		} else if ("Cook".equalsIgnoreCase(this.occupation)) {
			RemoveStockCookController controller = loader.getController();
			controller.setInventory(InventoryStore.getInventory());
			controller.setOccupation(this.occupation);
			controller.setUsername(this.username);
		}

		Stage stage = (Stage) this.removeStockButton.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Pirate Ship Inventory Management System - " + titleSuffix);
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

	/**
	 * Navigates to the View Stock Changes page.
	 *
	 * @param event the ActionEvent triggered by the view stock changes button
	 */
	@FXML
	private void onViewStockChanges(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/ViewChanges.fxml"));
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

	/**
	 * Navigates to the View Expiration page for Cooks.
	 *
	 * @param event the ActionEvent triggered by the view button
	 * @throws IOException if the FXML file cannot be loaded
	 */
	@FXML
	private void onViewExpiration(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/ViewExpiration.fxml"));
		Parent root = loader.load();

		ViewExpirationController controller = loader.getController();
		controller.setInventory(InventoryStore.getInventory());
		controller.setOccupation(this.occupation);
		controller.setUsername(this.username);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Pirate Ship Inventory Management System - View Food Expiration");
	}

	/**
	 * Updates the stock list view with the current inventory.
	 */
	private void updateStockList() {
		this.stockList.getItems().clear();
		if (this.inventory == null) {
			return;
		}
		for (Compartment comp : this.inventory.getCompartments()) {
			for (Stock stock : comp.getStockList()) {
				String qualitiesText = stock.getQualities().toString();
				String item = String.format("%s - %s (%s) - Qty: %d - Exp: %s - SQ: %s", comp.getName(),
						stock.getName(), stock.getCondition(), stock.getSize(), stock.getExpirationDate(),
						qualitiesText);
				this.stockList.getItems().add(item);
			}
		}
	}

	/**
	 * Updates the state of the View Stock Changes button based on occupation. Only
	 * a Quartermaster can access stock change history. Cooks see "View Expiration
	 * Dates" instead. Only Officers and Cooks can remove stock (from different
	 * compartments).
	 */
	private void updateButtonState() {
		if (this.viewStockChangesButton != null && this.occupation != null) {
			if ("Quartermaster".equalsIgnoreCase(this.occupation)) {
				this.viewStockChangesButton.setText("View Stock Changes");
				this.viewStockChangesButton.setOnAction(this::onViewStockChanges);
				this.viewStockChangesButton.setDisable(false);
			} else if ("Cook".equalsIgnoreCase(this.occupation)) {
				this.viewStockChangesButton.setText("View Expiration Dates");
				this.viewStockChangesButton.setOnAction(e -> {
					try {
						this.onViewExpiration(e);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
				this.viewStockChangesButton.setDisable(false);
			} else {
				this.viewStockChangesButton.setDisable(true);
			}
		}

		if (this.removeStockButton != null && this.occupation != null) {
			boolean canRemove = "Officer".equalsIgnoreCase(this.occupation) || "Cook".equalsIgnoreCase(this.occupation);
			this.removeStockButton.setDisable(!canRemove);

			if ("Officer".equalsIgnoreCase(this.occupation)) {
				this.removeStockButton.setText("Remove Munitions");
			} else if ("Cook".equalsIgnoreCase(this.occupation)) {
				this.removeStockButton.setText("Remove Food");
			}
		}
	}
}