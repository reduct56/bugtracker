package edu.pet.view;

import edu.pet.dto.BugResponse;
import edu.pet.enums.Priority;
import edu.pet.enums.State;
import edu.pet.networking.NetworkHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class MainWindow {
    private final NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");
    private final Stage stage;

    private BugResponse selectedBug;

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        ObservableList<BugResponse> bugList = FXCollections.observableArrayList(
                new BugResponse(666L, "test666", "testInfo666", Priority.HIGH, State.OPEN),
                new BugResponse(777L, "test777", "testInfo777", Priority.MEDIUM, State.CLOSED),
                new BugResponse(888L, "test888", "testInfo888", Priority.LOW, State.CLOSED),
                new BugResponse(999L, "test999", "testInfo999", Priority.MEDIUM, State.OPEN)
        );

        Button btn = new Button("Close bug");
        AnchorPane.setTopAnchor(btn, 10.0);
        AnchorPane.setLeftAnchor(btn, 10.0);

        Button btn2 = new Button("Re-Open bug");
        AnchorPane.setTopAnchor(btn2, 10.0);
        AnchorPane.setLeftAnchor(btn2, 100.0);

        Button btnUpdate = new Button("Update");
        AnchorPane.setTopAnchor(btnUpdate, 10.0);
        AnchorPane.setLeftAnchor(btnUpdate, 200.0);

        TableView<BugResponse> tableView = new TableView<>(bugList);
        prepareTable(tableView);
        AnchorPane.setTopAnchor(tableView, 80.0);
        AnchorPane.setLeftAnchor(tableView, 10.0);

        // Close bug
        btn.setOnAction(event -> {
            selectedBug = tableView.getSelectionModel().getSelectedItem();
            if (selectedBug == null) {
                System.out.println("Error");
                return;
            }
            new Thread( () -> {
                try {
                    System.out.println(networkHandler.markClose(selectedBug.id()));
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        btn2.setOnAction(actionEvent -> new Thread( () -> {
            try {
                System.out.println(networkHandler.getAll());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start());

        btnUpdate.setOnAction(actionEvent -> new Thread( () -> {
            try {
                List<BugResponse> list = networkHandler.getAll();
                Platform.runLater(() -> bugList.setAll(list));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start());

        // FlowPane root = new FlowPane(10, 10, btn, btn2, btnUpdate, tableView);

        AnchorPane root = new AnchorPane(btn, btn2, btnUpdate, tableView);

        Scene scene = new Scene(root, 750, 450);
        stage.setTitle("My First JavaFX App");
        stage.setScene(scene);
        stage.show();
    }

    private void prepareTable(TableView<BugResponse> tableView) {
        tableView.setPrefHeight(250);
        tableView.setPrefWidth(350);

        // ID
        TableColumn<BugResponse, Long> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().id()).asObject());
        tableView.getColumns().add(columnId);

        // Title
        TableColumn<BugResponse, String> columnTitle = new TableColumn<>("Title");
        columnTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().title()));
        tableView.getColumns().add(columnTitle);

        // Priority
        TableColumn<BugResponse, Priority> columnPriority = new TableColumn<>("Priority");
        columnPriority.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().priority()));
        tableView.getColumns().add(columnPriority);

        // State
        TableColumn<BugResponse, State> columnState = new TableColumn<>("State");
        columnState.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().state()));
        tableView.getColumns().add(columnState);
    }
}
