package edu.pet.view;

import edu.pet.dto.BugResponse;
import edu.pet.enums.Priority;
import edu.pet.enums.State;
import edu.pet.logic.AppService;
import edu.pet.networking.NetworkHandler;
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

public class MainWindow {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    private final NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");
    private final Stage stage;
    private final ObservableList<BugResponse> bugList = FXCollections.observableArrayList();
    private final TableView<BugResponse> tableView = new TableView<>(bugList);
    private final AppService appService = new AppService(networkHandler, tableView, bugList);

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        Button btn = new Button("Close bug");
        AnchorPane.setTopAnchor(btn, 10.0);
        AnchorPane.setLeftAnchor(btn, 10.0);

        Button btn2 = new Button("Re-Open bug");
        AnchorPane.setTopAnchor(btn2, 10.0);
        AnchorPane.setLeftAnchor(btn2, 100.0);

        Button btnUpdate = new Button("Update");
        AnchorPane.setTopAnchor(btnUpdate, 10.0);
        AnchorPane.setLeftAnchor(btnUpdate, 200.0);

        prepareTable(tableView);
        AnchorPane.setTopAnchor(tableView, 80.0);
        AnchorPane.setLeftAnchor(tableView, 10.0);

        // Close bug
        btn.setOnAction(event -> appService.closeBug());

        // Re-open bug
        btn2.setOnAction(actionEvent -> appService.reopenBug());

        // Update all
        btnUpdate.setOnAction(actionEvent -> appService.updateAll());

        AnchorPane root = new AnchorPane(btn, btn2, btnUpdate, tableView);

        Scene scene = new Scene(root, 750, 450);
        stage.setTitle("Bug Tracker");
        stage.setScene(scene);
        stage.show();
        logger.info("Main window ready to use");
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
