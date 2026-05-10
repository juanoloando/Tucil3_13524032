package view;

import java.net.URL;
import java.util.ResourceBundle;

import controller.PlaybackController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

// UI untuk menampilkan playback solusi per step
public class PlaybackView implements Initializable {

    @FXML private Label lblStep;
    @FXML private Label lblStepCost;
    @FXML private Label lblCostSoFar;
    @FXML private Label lblDirection;
    @FXML private Slider sliderSpeed;
    @FXML private Button btnPlayPause;
    @FXML private HBox controlBar;

    private PlaybackController playbackController;

    private boolean isPlaying = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sliderSpeed.setMin(1);
        sliderSpeed.setMax(10);
        sliderSpeed.setValue(5);
        sliderSpeed.setShowTickLabels(true);
        sliderSpeed.setShowTickMarks(true);
        sliderSpeed.setMajorTickUnit(3);
        sliderSpeed.setSnapToTicks(false);

        sliderSpeed.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (playbackController != null) {
                double ms = 1600 - (newVal.doubleValue() * 150);
                playbackController.setSpeed(Math.max(100, ms));
            }
        });

        resetLabels();
    }

    public void setPlaybackController(PlaybackController controller) {
        this.playbackController = controller;
        double ms = 1600 - (sliderSpeed.getValue() * 150);
        this.playbackController.setSpeed(Math.max(100, ms));
    }

    public void setDisabled(boolean disabled) {
        controlBar.setDisable(disabled);
        if (disabled) {
            lblStep.setText("Solusi tidak ditemukan.");
            lblStepCost.setText("");
            lblCostSoFar.setText("");
            lblDirection.setText("");
        }
    }

    @FXML
    private void onFirstClicked() {
        if (playbackController == null) return;
        playbackController.goToFirst();
        pauseIfPlaying();
    }

    @FXML
    private void onPreviousClicked() {
        if (playbackController == null) return;
        playbackController.stepBackward();
        pauseIfPlaying();
    }

    @FXML
    private void onPlayPauseClicked() {
        togglePlayPause();
    }

    @FXML
    private void onNextClicked() {
        if (playbackController == null) return;
        playbackController.stepForward();
        pauseIfPlaying();
    }

    @FXML
    private void onLastClicked() {
        if (playbackController == null) return;
        playbackController.goToLast();
        pauseIfPlaying();
    }

    public void togglePlayPause() {
        if (playbackController == null) return;
        if (isPlaying) {
            playbackController.pause();
            btnPlayPause.setText("▶  Play");
            isPlaying = false;
        } else {
            playbackController.play();
            btnPlayPause.setText("⏸  Pause");
            isPlaying = true;
        }
    }

    public void updateLabels(int currentStep, int totalSteps,
                             double stepCost, double cumulativeCost,
                             String direction) {
        lblStep.setText(String.format("Step: %d / %d", currentStep, totalSteps));
        lblStepCost.setText(String.format("Step Cost: %.0f", stepCost));
        lblCostSoFar.setText(String.format("Step Cost Sejauh Ini: %.0f", cumulativeCost));
        lblDirection.setText("Direction: " + direction);
    }

    public void onPlaybackFinished() {
        isPlaying = false;
        btnPlayPause.setText("Play");
    }

    private void pauseIfPlaying() {
        if (isPlaying) {
            playbackController.pause();
            btnPlayPause.setText("Play");
            isPlaying = false;
        }
    }

    private void resetLabels() {
        lblStep.setText("Step: 0 / 0");
        lblStepCost.setText("Step Cost: —");
        lblCostSoFar.setText("Step Cost Sejauh Ini: —");
        lblDirection.setText("Direction: -");
        btnPlayPause.setText("Play");
    }
}
