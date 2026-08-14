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

    private final Stage stage;
    private final NetworkHandler networkHandler = new NetworkHandler("http://localhost:8080");

    private final ObservableList<BugResponse> bugList = FXCollections.observableArrayList();
    private final TableView<BugResponse> tableView = new TableView<>(bugList);

    private final AppService appService = new AppService(networkHandler, tableView, bugList);
    private final DetailPanel detailPanel = new DetailPanel(appService);

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        Button btnUpdate = new Button("Update");
        AnchorPane.setTopAnchor(btnUpdate, 0.0);
        AnchorPane.setLeftAnchor(btnUpdate, 10.0);

        prepareTable(tableView);
        AnchorPane.setTopAnchor(tableView, 30.0);
        AnchorPane.setLeftAnchor(tableView, 10.0);

        // Update all
        btnUpdate.setOnAction(actionEvent -> appService.updateAll());

        AnchorPane.setTopAnchor(detailPanel.getLayout(), 0.0);
        AnchorPane.setBottomAnchor(detailPanel.getLayout(), 10.0);
        AnchorPane.setRightAnchor(detailPanel.getLayout(), 10.0);
        AnchorPane.setLeftAnchor(detailPanel.getLayout(), null);

        AnchorPane root = new AnchorPane(btnUpdate, tableView, detailPanel.getLayout());

        Scene scene = new Scene(root, 750, 450);
        stage.setTitle("Bug Tracker");
        stage.setScene(scene);
        stage.show();
        logger.info("Main window is ready");
    }

    private void prepareTable(TableView<BugResponse> tableView) {
        tableView.setPrefHeight(250);
        tableView.setPrefWidth(350);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // ID
        TableColumn<BugResponse, Long> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().id()).asObject());
        columnId.setSortable(false);
        columnId.setPrefWidth(25);
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

        // sync with server
        appService.updateAll();


        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldSelection, newSelection)
                        -> {
                            if (newSelection != null) {
                                detailPanel.setBug(newSelection);
                            } else {
                                detailPanel.clear();
                            }
                        });

        logger.info("Table prepared");
    }
}
