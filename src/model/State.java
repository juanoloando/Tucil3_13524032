package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class State {
    // posisi karakter
    private final int x;
    private final int y;
    //indeks goal berikutnya 
    private final int nextGoalIndex;
    // cost dari state awal ke current state
    private final double cost;
    // arah dari parent state ke current state 
    private final State parent;
    // arah gerakan terakhir untuk masuk ke state ini 
    private final Direction lastMove;
    // cost langkah terakhir 
    private final double stepCost;
    // titik-titik yang dilalui selama slide
    private final List<int[]> slideWaypoints;

    public State(int x, int y) {
        this(x, y, 0, 0.0, null, null, 0.0, new ArrayList<>());
    }

    public State(int x, int y, int nextGoalIndex, double cost, State parent,
                 Direction lastMove, double stepCost, List<int[]> slideWayPoints) {
        this.x = x;
        this.y = y;
        this.nextGoalIndex = nextGoalIndex;
        this.cost = cost;
        this.parent = parent;
        this.lastMove = lastMove;
        this.stepCost = stepCost;
        this.slideWaypoints = copyWaypoints(slideWayPoints);
    }

    private List<int[]> copyWaypoints(List<int[]> waypoints) {
        List<int[]> copied = new ArrayList<>();
        if (waypoints == null) {
            return copied;
        }
        for (int[] point : waypoints) {
            if (point != null && point.length >= 2) {
                copied.add(new int[]{point[0], point[1]});
            }
        }
        return copied;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getNextGoalIndex() { return nextGoalIndex; }
    public double getCost() { return cost; }
    public State getParent() { return parent; }
    public Direction getLastMove() { return lastMove; }
    public double getStepCost(){ return stepCost; }

    public List<int[]> getSlideWaypoints() {
        return copyWaypoints(slideWaypoints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State state)) return false;
        return x == state.x && y == state.y && nextGoalIndex == state.nextGoalIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, nextGoalIndex);
    }

    @Override
    public String toString() {
        return "State{x=" + x + ", y=" + y + ", nextGoalIndex=" + nextGoalIndex + ", cost=" + cost + ", lastMove=" + lastMove + '}';
    }
}
