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

public class AStar implements Solver {

    @Override
    public SearchResult solve(PuzzleGrid grid, GameConfig config) {
        long startTime = System.nanoTime();

        Heuristic heuristic = Heuristic.fromName(config.getHeuristicName());

        PriorityQueue<PriorityQueueNode> pq = new PriorityQueue<>();
        Set<State> visited = new HashSet<>();

        int[] start = grid.getStartPosition();
        State initialState = new State(start[0], start[1]);

        double initialPriority = initialState.getCost() + heuristic.estimate(initialState, grid);
        pq.add(new PriorityQueueNode(initialState, initialPriority));

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

                return SearchResult.success(path, nodesExplored, (endTime - startTime) / 1_000_000.0,"A*", config.getHeuristicName());
            }

            for (State neighbor : SlideMechanics.getNeighbors(current, grid)) {
                if (!visited.contains(neighbor)) {
                    double priority = neighbor.getCost() + heuristic.estimate(neighbor, grid);
                    pq.add(new PriorityQueueNode(neighbor, priority));
                }
            }
        }

        long endTime = System.nanoTime();

        return SearchResult.failure(nodesExplored, (endTime - startTime) / 1_000_000.0,"A*",config.getHeuristicName());
    }

    private boolean isGoalState(State state, PuzzleGrid grid) {
        Tile tile = grid.getTile(state.getX(), state.getY());

        return tile == Tile.EXIT && state.getNextGoalIndex() == grid.getTotalGoals();
    }
}
