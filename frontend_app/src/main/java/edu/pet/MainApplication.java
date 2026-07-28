package edu.pet;

import edu.pet.networking.NetworkHandler;
import edu.pet.view.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");

    @Override
    public void start(Stage stage) {
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
