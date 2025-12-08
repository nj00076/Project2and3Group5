package edu.westga.cs3211.p1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Pirate Ship Inventory Management System.
 * Launches the JavaFX application and opens the login screen.
 * @author nj00076
 * @version cs3211
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the Login.fxml scene and displays it on the primary stage.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Pirate Ship Inventory Management System - Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method, launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
