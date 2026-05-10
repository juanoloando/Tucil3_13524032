package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Direction;
import model.PuzzleGrid;
import model.State;
import model.Tile;

public class SlideMechanics {

    private SlideMechanics() {}

    public static List<State> getNeighbors(State current, PuzzleGrid grid) {
        List<State> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            State nextState = slide(current, grid, direction);
            if (nextState != null) {
                neighbors.add(nextState);
            }
        }
        return neighbors;
    }

    private static State slide(State current, PuzzleGrid grid, Direction direction) {
        int cx = current.getX();
        int cy = current.getY();

        int nextGoalIndex = current.getNextGoalIndex();
        double slideCost  = 0.0;
        boolean hasMoved  = false;

        List<int[]> waypoints = new ArrayList<>();

        while (true) {
            int nx = cx + direction.getDx();
            int ny = cy + direction.getDy();

            if (!grid.isInBounds(nx, ny)) {
                return null;
            }

            Tile nextTile = grid.getTile(nx, ny);

            if (nextTile == Tile.OBSTACLE) {
                break;
            }

            if (nextTile == Tile.LAVA) {
                return null;
            }

            cx = nx;
            cy = ny;
            hasMoved = true;
            slideCost += grid.getCost(cx, cy);
            waypoints.add(new int[]{cx, cy});

            if (nextTile.isGoal()) {
                int goalIdx = nextTile.getGoalIndex();

                if (goalIdx == nextGoalIndex) {
                    nextGoalIndex++;
                } else if (goalIdx < nextGoalIndex) {
                    // aman
                } else {
                    return null;
                }
            }

            if (nextTile == Tile.EXIT && nextGoalIndex == grid.getTotalGoals()) {
                return new State(
                        cx,
                        cy,
                        nextGoalIndex,
                        current.getCost() + slideCost,
                        current,
                        direction,
                        slideCost,
                        waypoints
                );
            }
        }

        // Valid hanya jika bergerak minimal 1 tile
        if (!hasMoved) {
            return null;
        }

        return new State(cx, cy, nextGoalIndex,
                current.getCost() + slideCost,
                current, direction, slideCost, waypoints);
    }
}