package edu.westga.cs3211.p1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/westga/cs3211/p1/view/Login.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Pirate Ship Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
