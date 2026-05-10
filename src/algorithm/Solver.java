package algorithm;
import model.GameConfig;
import model.PuzzleGrid;
import model.SearchResult;

public interface Solver {
    SearchResult solve(PuzzleGrid grid, GameConfig config);
}