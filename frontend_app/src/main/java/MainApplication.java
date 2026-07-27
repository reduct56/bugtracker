import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.pet.Main;
import edu.pet.dto.BugResponse;
import edu.pet.networking.NetworkHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");

    @Override
    public void start(Stage stage) {
        Button btn = new Button("Click Me!");

        Button btn2 = new Button("fake");
        Button btn3 = new Button("fake2");

        btn.setOnAction(event -> new Thread( () -> {
            try {
                System.out.println(networkHandler.markClose(2L));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start());

        btn2.setOnAction(actionEvent -> new Thread( () -> {
            try {
                System.out.println(networkHandler.getAll());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start());
        btn3.setOnAction(actionEvent -> new Thread( () -> {
            try {
                System.out.println(networkHandler.markOpen(2L));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start());



        FlowPane root = new FlowPane(10, 10, btn, btn2, btn3);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("My First JavaFX App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
