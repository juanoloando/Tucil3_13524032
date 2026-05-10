package algorithm;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import model.GameConfig;
import model.PuzzleGrid;
import model.SearchResult;
import model.SolutionPath;
import model.State;
import model.Tile;

public class UCS implements Solver {

    @Override
    public SearchResult solve(PuzzleGrid grid, GameConfig config) {
        long startTime = System.nanoTime();

        PriorityQueue<PriorityQueueNode> pq = new PriorityQueue<>();
        Set<State> visited = new HashSet<>();

        int[] start = grid.getStartPosition();
        State initialState = new State(start[0], start[1]);

        pq.add(new PriorityQueueNode(initialState, initialState.getCost()));

        int nodesExplored = 0;

        while (!pq.isEmpty()) {
            PriorityQueueNode node = pq.poll();
            State current = node.getState();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            nodesExplored++;

            if (isGoalState(current, grid)) {
                long endTime = System.nanoTime();
                SolutionPath path = SolutionPath.reconstruct(current);

                return SearchResult.success(path, nodesExplored, (endTime - startTime) / 1_000_000.0,"UCS","None"
                );
            }

            for (State neighbor : SlideMechanics.getNeighbors(current, grid)) {
                if (!visited.contains(neighbor)) {
                    pq.add(new PriorityQueueNode(neighbor, neighbor.getCost()));
                }
            }
        }

        long endTime = System.nanoTime();

        return SearchResult.failure(nodesExplored, (endTime - startTime) / 1_000_000.0,"UCS","None"
        );
    }

    private boolean isGoalState(State state, PuzzleGrid grid) {
        Tile tile = grid.getTile(state.getX(), state.getY());

        return tile == Tile.EXIT && state.getNextGoalIndex() == grid.getTotalGoals();
    }
}
