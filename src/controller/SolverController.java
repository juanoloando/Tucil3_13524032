package controller;

import java.io.IOException;

import algorithm.AStar;
import algorithm.GBFS;
import algorithm.Solver;
import algorithm.UCS;
import model.GameConfig;
import model.PuzzleGrid;
import model.SearchResult;
import util.FileParser;
import util.GridValidator;


public class SolverController {

    private PuzzleGrid lastGrid;
    private SearchResult lastResult;
    private GameConfig lastConfig;

    public SearchResult solve(GameConfig config) throws IOException {
        PuzzleGrid grid = FileParser.parse(config.getInputFilePath());
        GridValidator.validate(grid);
        this.lastGrid   = grid;
        this.lastConfig = config;
        Solver solver   = createSolver(config.getAlgorithmName());
        SearchResult result = solver.solve(grid, config);
        this.lastResult = result;
        return result;
    }

    private Solver createSolver(String algorithmName) {
        return switch (algorithmName.toUpperCase()) {
            case "UCS"        -> new UCS();
            case "GBFS"       -> new GBFS();
            case "ASTAR", "A*" -> new AStar();
            default -> throw new IllegalArgumentException(
                "Algoritma tidak dikenal: " + algorithmName);
        };
    }

    public PuzzleGrid    getLastGrid()   { return lastGrid; }
    public SearchResult  getLastResult() { return lastResult; }
    public GameConfig    getLastConfig() { return lastConfig; }
}