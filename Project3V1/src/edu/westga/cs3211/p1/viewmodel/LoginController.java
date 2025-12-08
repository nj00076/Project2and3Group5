package edu.westga.cs3211.p1.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;

/**
 * Controller for the login screen of the Pirate Ship Inventory System.
 * Handles verifying user credentials and navigating to the home page.
 * @author nj00076
 * @version cs3211
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    /**
     * Handles the login button click.
     * Reads user credentials from Logins.txt and verifies them.
     * If valid, navigates to the Home Page.
     *
     * @param event the ActionEvent triggered by the login button
     */
    @FXML
    private void onLogin(ActionEvent event) {
        String enteredUsername = this.usernameField.getText();
        String enteredPassword = this.passwordField.getText();
        boolean found = false;
        String occupation = "";

        InputStream inputStream = getClass().getResourceAsStream("/edu/westga/cs3211/p1/resources/Logins.txt");
        if (inputStream == null) {
            this.errorLabel.setText("Login file not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                	continue;
                }

                String username = parts[0].trim();
                String password = parts[1].trim();

                if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
                    found = true;
                    occupation = parts[2].trim();
                    break;
                }
            }

            if (found) {
                this.openHomePage(event, enteredUsername, occupation);
            } else {
                this.errorLabel.setText("Invalid username or password.");
            }
        } catch (IOException exception) {
            this.errorLabel.setText("Error reading login file.");
            exception.printStackTrace();
        }
    }

    /**
     * Opens the Home Page scene and passes the username and occupation to the controller.
     *
     * @param event the ActionEvent that triggered the navigation
     * @param username the verified username
     * @param occupation the verified user's occupation
     */
    private void openHomePage(ActionEvent event, String username, String occupation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/HomePage.fxml"));
            Scene homeScene = new Scene(loader.load());
            HomePageController controller = loader.getController();
            controller.setUsername(username);
            controller.setOccupation(occupation);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(homeScene);
            currentStage.setTitle("Pirate Ship Inventory System - Home");
            currentStage.show();
        } catch (IOException exception) {
            this.errorLabel.setText("Failed to open HomePage.fxml.");
            exception.printStackTrace();
        }
    }
}
