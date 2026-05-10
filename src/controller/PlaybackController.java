package controller;

import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.SolutionPath;
import model.State;
import view.GridView;
import view.PlaybackView;

public class PlaybackController {

    private SolutionPath solutionPath;
    private GridView gridView;
    private PlaybackView playbackView;

    private int currentStepIndex = 0;  
    private double speedMs = 850.0;
    private Timeline playbackTimeline;

    public void initialize(SolutionPath path, GridView gv, PlaybackView pv) {
        this.solutionPath   = path;
        this.gridView       = gv;
        this.playbackView   = pv;
        this.currentStepIndex = 0;
        pause();
        playbackTimeline = null;
    }


    public void goToStep(int stepIndex) {
        if (solutionPath == null) return;

        stepIndex = Math.max(0, Math.min(stepIndex, solutionPath.getTotalSteps()));
        currentStepIndex = stepIndex;

        State state = solutionPath.getStateAt(stepIndex);

        List<int[]> waypoints = (stepIndex > 0)
            ? solutionPath.getWaypointsAt(stepIndex - 1)
            : List.of();

        gridView.renderStep(state, waypoints);

        double stepCost       = (stepIndex > 0) ? solutionPath.getCostAt(stepIndex - 1) : 0.0;
        double cumulativeCost = state.getCost();
        String dirStr         = (stepIndex > 0)
            ? String.valueOf(solutionPath.getMoveAt(stepIndex - 1).getSymbol())
            : "-";

        playbackView.updateLabels(
            stepIndex,
            solutionPath.getTotalSteps(),
            stepCost,
            cumulativeCost,
            dirStr
        );
    }

    public void stepForward()  { goToStep(currentStepIndex + 1); }
    public void stepBackward() { goToStep(currentStepIndex - 1); }
    public void goToFirst()    { goToStep(0); }
    public void goToLast()     { goToStep(solutionPath != null ? solutionPath.getTotalSteps() : 0); }

    public void play() {
        if (solutionPath == null || solutionPath.getTotalSteps() == 0) {
            return;
        }

        if (isAtEnd()) {
            goToFirst();
        }

        ensureTimeline();
        playbackTimeline.play();
    }

    public void pause() {
        if (playbackTimeline != null) {
            playbackTimeline.stop();
        }
    }

    public void setSpeed(double speedMs) {
        this.speedMs = Math.max(100.0, speedMs);

        if (playbackTimeline != null) {
            boolean wasRunning = playbackTimeline.getStatus() == Animation.Status.RUNNING;
            playbackTimeline.stop();
            playbackTimeline = null;

            if (wasRunning) {
                ensureTimeline();
                playbackTimeline.play();
            }
        }
    }

    public int getCurrentStep() { return currentStepIndex; }
    public int getTotalSteps()  { return solutionPath != null ? solutionPath.getTotalSteps() : 0; }
    public boolean isAtEnd()    { return solutionPath != null && currentStepIndex >= solutionPath.getTotalSteps(); }

    private void ensureTimeline() {
        if (playbackTimeline != null) {
            return;
        }

        playbackTimeline = new Timeline(
            new KeyFrame(Duration.millis(speedMs), event -> advancePlayback())
        );
        playbackTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void advancePlayback() {
        if (solutionPath == null) {
            pause();
            return;
        }

        if (isAtEnd()) {
            pause();
            playbackView.onPlaybackFinished();
            return;
        }

        stepForward();

        if (isAtEnd()) {
            pause();
            playbackView.onPlaybackFinished();
        }
    }
}
