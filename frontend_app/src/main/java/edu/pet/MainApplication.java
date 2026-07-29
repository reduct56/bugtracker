package edu.pet;

import edu.pet.networking.NetworkHandler;
import edu.pet.view.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");

    @Override
    public void start(Stage stage) {
        logger.info("Application started");
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
