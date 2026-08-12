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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MainWindow {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    private final NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");
    private final Stage stage;

    private BugResponse selectedBug;

    ObservableList<BugResponse> bugList;
    TableView<BugResponse> tableView;

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        bugList = FXCollections.observableArrayList(
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

        tableView = new TableView<>(bugList);
        prepareTable(tableView);
        AnchorPane.setTopAnchor(tableView, 80.0);
        AnchorPane.setLeftAnchor(tableView, 10.0);

        // Close bug
        btn.setOnAction(event -> this.closeBug());

        // Re-open bug
        btn2.setOnAction(actionEvent -> this.reopenBug());

        // Update all
        btnUpdate.setOnAction(actionEvent -> this.updateAll());

        AnchorPane root = new AnchorPane(btn, btn2, btnUpdate, tableView);

        Scene scene = new Scene(root, 750, 450);
        stage.setTitle("Bug Tracker");
        stage.setScene(scene);
        stage.show();
        logger.info("Main window ready to use");
    }

    private void closeBug() {
        selectedBug = tableView.getSelectionModel().getSelectedItem();
        if (selectedBug == null) {
            logger.error("Bug is null while trying to close bug");
            return;
        }
        new Thread( () -> {
            try {
                networkHandler.markClose(selectedBug.id());
                updateSingleEntry(selectedBug.id());
                logger.info("Bug id={} closed", selectedBug.id());
            } catch (IOException | InterruptedException e) {
                logger.error("Can't get server response on Close Bug action");
            }
        }).start();
    }

    private void reopenBug() {
        selectedBug = tableView.getSelectionModel().getSelectedItem();
        if (selectedBug == null) {
            logger.error("Bug is null while trying to re-open bug");
            return;
        }
        new Thread( () -> {
            try {
                networkHandler.markOpen(selectedBug.id());
                updateSingleEntry(selectedBug.id());
                logger.info("Bug id={} re-opened", selectedBug.id());
            } catch (IOException | InterruptedException e) {
                logger.error("Can't get server response on Re-open Bug action");
            }
        }).start();
    }

    private void updateAll() {
        new Thread( () -> {
            try {
                List<BugResponse> list = networkHandler.getAll();
                Platform.runLater(() -> bugList.setAll(list));
                logger.info("Synced all with server");
            } catch (IOException | InterruptedException e) {
                logger.error("Can't get server response on Update All action");
            }
        }).start();
    }

    private void updateSingleEntry(Long id) {
        try {
            BugResponse item = networkHandler.getById(id);
            Platform.runLater(() -> {
                for (int i = 0; i < bugList.size(); i++ ){
                    if (bugList.get(i).id().equals(id)) {
                        bugList.set(i, item);
                        return;
                    }
                }
                bugList.add(item);
                logger.warn("failed to find existing id in table - append");
            });
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareTable(TableView<BugResponse> tableView) {
        tableView.setPrefHeight(250);
        tableView.setPrefWidth(350);

        // ID
        TableColumn<BugResponse, Long> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().id()).asObject());
        columnId.setSortable(false);
        tableView.getColumns().add(columnId);

        // Title
        TableColumn<BugResponse, String> columnTitle = new TableColumn<>("Title");
        columnTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().title()));
        columnTitle.setSortable(false);
        tableView.getColumns().add(columnTitle);

        // Priority
        TableColumn<BugResponse, Priority> columnPriority = new TableColumn<>("Priority");
        columnPriority.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().priority()));
        columnPriority.setSortable(false);
        tableView.getColumns().add(columnPriority);

        // State
        TableColumn<BugResponse, State> columnState = new TableColumn<>("State");
        columnState.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().state()));
        columnState.setSortable(false);
        tableView.getColumns().add(columnState);

        logger.info("Table prepared");
    }
}
