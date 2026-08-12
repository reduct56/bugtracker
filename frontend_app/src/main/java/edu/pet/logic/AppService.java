package edu.pet.logic;

import edu.pet.dto.BugResponse;
import edu.pet.networking.NetworkHandler;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class AppService {
    private static final Logger logger = LoggerFactory.getLogger(AppService.class);

    private final NetworkHandler networkHandler;
    private final TableView<BugResponse> tableView;
    private final ObservableList<BugResponse> bugList;

    private BugResponse selectedBug;

    public AppService(NetworkHandler networkHandler, TableView<BugResponse> tableView, ObservableList<BugResponse> bugList) {
        this.networkHandler = networkHandler;
        this.tableView = tableView;
        this.bugList = bugList;
    }

    public void closeBug() {
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

    public void reopenBug() {
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

    public void updateAll() {
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

    public void updateSingleEntry(Long id) {
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
}
