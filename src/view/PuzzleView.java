package view;

import java.net.URL;
import java.util.ResourceBundle;

import controller.AppController;
import controller.PlaybackController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import model.PuzzleGrid;
import model.SearchResult;

public class PuzzleView implements Initializable {
    @FXML private VBox rootPane;
    @FXML private GridView gridCanvas;
    @FXML private ResultView   resultPanelController;
    @FXML private SidebarView  sidebarPanelController;
    @FXML private PlaybackView playbackPanelController;

    private PlaybackController playbackController;

    private AppController appController;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        javafx.application.Platform.runLater(() -> {
            if (rootPane.getScene() != null) {
                rootPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyShortcut);
                rootPane.requestFocus();
            }
        });
    }

    public void initWithResult(SearchResult result, PuzzleGrid grid) {
        gridCanvas.initialize(grid);

        resultPanelController.displayResult(result);

        sidebarPanelController.displayInfo(result.getAlgorithmUsed(), result.getHeuristicUsed());

        if (result.isSolutionFound()) {
            playbackController = new PlaybackController();
            playbackController.initialize(
                result.getSolutionPath(),
                gridCanvas,
                playbackPanelController
            );
            playbackPanelController.setPlaybackController(playbackController);
            playbackController.goToStep(0);
        } else {
            playbackPanelController.setDisabled(true);
        }
    }

    private void handleKeyShortcut(KeyEvent event) {
        if (playbackController == null) return;

        switch (event.getCode()) {
            case RIGHT  -> { playbackController.stepForward();  event.consume(); }
            case LEFT   -> { playbackController.stepBackward(); event.consume(); }
            case ESCAPE -> { openJumpToStepDialog();            event.consume(); }
            case SPACE  -> { playbackPanelController.togglePlayPause(); event.consume(); }
        }
    }

    private void openJumpToStepDialog() {
        if (playbackController == null) return;

        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Jump to Step");
        dialog.setHeaderText("Lompat ke step tertentu");
        dialog.setContentText(String.format(
            "Masukkan nomor step (0 – %d):", playbackController.getTotalSteps()
        ));

        dialog.showAndWait().ifPresent(input -> {
            try {
                int step = Integer.parseInt(input.trim());
                playbackController.goToStep(step);
            } catch (NumberFormatException ignored) {}
        });
    }

    @FXML
    private void onSaveSolutionClicked() {
        if (appController == null) return;
        appController.saveSolutionToFile(MainApp.getPrimaryStage());
    }

    @FXML
    private void onSaveIterationsClicked() {
        if (appController == null) return;
        appController.saveIterationsToFile(MainApp.getPrimaryStage());
    }

    @FXML
    private void onBackToMenuClicked() {
        if (playbackController != null) {
            playbackController.pause();
        }
        if (appController != null) {
            appController.navigateToMenu();
        }
    }
}