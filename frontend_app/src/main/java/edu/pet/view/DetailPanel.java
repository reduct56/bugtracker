package edu.pet.view;

import edu.pet.dto.BugResponse;
import edu.pet.enums.Priority;
import edu.pet.enums.State;
import edu.pet.logic.AppService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailPanel {
    public static final Logger logger = LoggerFactory.getLogger(DetailPanel.class);

    private final AppService appService;

    // Headers
    private final Label detailsHeader = new Label("Bug Details");
    private final Label titleHeader = new Label("Title");
    private final Label infoHeader = new Label("Info");
    private final Label priorityHeader = new Label("Priority");
    private final Label stateHeader = new Label("State");

    // Labels, Combos and TextArea
    private final Label idLabel = new Label();
    private final Label titlelabel = new Label();
    private final ComboBox<Priority> priorityComboBox = new ComboBox<>();
    private final ComboBox<State> stateComboBox = new ComboBox<>();
    private final TextArea infoArea = new TextArea();

    // Buttons
    private final Button markCloseButton = new Button("Close");
    private final Button markOpenButton = new Button("Re-open");

    @Getter
    private VBox layout;

    public DetailPanel(AppService appService) {
        this.appService = appService;

        detailsHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        titleHeader.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: bold;");
        infoHeader.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: bold;");
        priorityHeader.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: bold;");
        stateHeader.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: bold;");

        idLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black; -fx-border-width: 1 1 1 1; -fx-padding: 0px 8px 0px 8px; ");
        titlelabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");

        priorityComboBox.getItems().setAll(Priority.values());
        stateComboBox.getItems().setAll(State.values());
        priorityComboBox.setPrefWidth(120);
        stateComboBox.setPrefWidth(120);

        infoArea.setPrefRowCount(8);
        infoArea.setWrapText(true);
        infoArea.setPrefWidth(350);
        infoArea.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

        // Mark Close
        markCloseButton.setOnAction(actionEvent -> appService.closeBug());

        // Mark open
        markOpenButton.setOnAction(actionEvent -> appService.reopenBug());

        layout = new VBox(3);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 0 1;");

        layout.getChildren().addAll(
                detailsHeader,
                idLabel,
                titleHeader, titlelabel,
                new HBox(10,
                        new VBox(5,
                                priorityHeader,
                                priorityComboBox),
                        new VBox(5,
                                stateHeader,
                                stateComboBox)),
                infoHeader, infoArea,
                new HBox(5, markCloseButton, markOpenButton));
    }

    public void setBug(BugResponse bug) {
        idLabel.setText(bug.id().toString());

        titlelabel.setText(bug.title());

        priorityComboBox.setItems(FXCollections.observableArrayList(bug.priority()));
        priorityComboBox.getSelectionModel().select(0);
        priorityComboBox.setMouseTransparent(true);

        stateComboBox.setItems(FXCollections.observableArrayList(bug.state()));
        stateComboBox.getSelectionModel().select(0);
        stateComboBox.setMouseTransparent(true);

        infoArea.setText(bug.info());
        infoArea.setEditable(false);
    }

    public void clear() {
        idLabel.setText("");
        titlelabel.setText("");
        priorityComboBox.setItems(null);
        stateComboBox.setItems(null);
        infoArea.clear();
    }
}
