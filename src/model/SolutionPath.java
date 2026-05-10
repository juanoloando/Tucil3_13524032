package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionPath {
    private final List<State> stateSequence;
    private final List<Direction> moveSequence;
    private final List<Double> stepCosts;
    private final List<List<int[]>> slideWaypoints;

    public SolutionPath(
            List<State> stateSequence,
            List<Direction> moveSequence,
            List<Double> stepCosts,
            List<List<int[]>> slideWaypoints
    ) {
        this.stateSequence = new ArrayList<>(stateSequence);
        this.moveSequence = new ArrayList<>(moveSequence);
        this.stepCosts = new ArrayList<>(stepCosts);
        this.slideWaypoints = copyWaypointList(slideWaypoints);
    }

    private List<List<int[]>> copyWaypointList(List<List<int[]>> source) {
        List<List<int[]>> copied = new ArrayList<>();

        if (source == null) {
            return copied;
        }

        for (List<int[]> stepWaypoints : source) {
            List<int[]> copiedStep = new ArrayList<>();

            if (stepWaypoints != null) {
                for (int[] point : stepWaypoints) {
                    if (point != null && point.length >= 2) {
                        copiedStep.add(new int[]{point[0], point[1]});
                    }
                }
            }

            copied.add(copiedStep);
        }

        return copied;
    }

    public int getTotalSteps() {
        return moveSequence.size();
    }

    public State getStateAt(int stepIndex) {
        if (stepIndex < 0 || stepIndex >= stateSequence.size()) {
            throw new IndexOutOfBoundsException("Step state tidak valid: " + stepIndex);
        }

        return stateSequence.get(stepIndex);
    }

    public Direction getMoveAt(int stepIndex) {
        if (stepIndex < 0 || stepIndex >= moveSequence.size()) {
            throw new IndexOutOfBoundsException("Step move tidak valid: " + stepIndex);
        }

        return moveSequence.get(stepIndex);
    }

    public double getCostAt(int stepIndex) {
        if (stepIndex < 0 || stepIndex >= stepCosts.size()) {
            throw new IndexOutOfBoundsException("Step cost tidak valid: " + stepIndex);
        }

        return stepCosts.get(stepIndex);
    }

    public List<int[]> getWaypointsAt(int stepIndex) {
        if (stepIndex < 0 || stepIndex >= slideWaypoints.size()) {
            throw new IndexOutOfBoundsException("Step waypoint tidak valid: " + stepIndex);
        }

        List<int[]> copied = new ArrayList<>();

        for (int[] point : slideWaypoints.get(stepIndex)) {
            copied.add(new int[]{point[0], point[1]});
        }

        return copied;
    }

    public List<State> getStateSequence() {
        return Collections.unmodifiableList(stateSequence);
    }

    public List<Direction> getMoveSequence() {
        return Collections.unmodifiableList(moveSequence);
    }

    public List<Double> getStepCosts() {
        return Collections.unmodifiableList(stepCosts);
    }

    public double getTotalCost() {
        if (stateSequence.isEmpty()) {
            return 0.0;
        }

        return stateSequence.get(stateSequence.size() - 1).getCost();
    }

    public String getMoveString() {
        StringBuilder sb = new StringBuilder();

        for (Direction direction : moveSequence) {
            sb.append(direction.getSymbol());
        }

        return sb.toString();
    }

    public static SolutionPath reconstruct(State finalState) {
        List<State> reversedStates = new ArrayList<>();

        State current = finalState;
        while (current != null) {
            reversedStates.add(current);
            current = current.getParent();
        }

        Collections.reverse(reversedStates);

        List<Direction> moves = new ArrayList<>();
        List<Double> costs = new ArrayList<>();
        List<List<int[]>> waypoints = new ArrayList<>();

        for (int i = 1; i < reversedStates.size(); i++) {
            State state = reversedStates.get(i);
            moves.add(state.getLastMove());
            costs.add(state.getStepCost());
            waypoints.add(state.getSlideWaypoints());
        }

        return new SolutionPath(reversedStates, moves, costs, waypoints);
    }
}