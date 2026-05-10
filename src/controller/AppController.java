package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Direction;
import model.GameConfig;
import model.PuzzleGrid;
import model.SearchResult;
import model.SolutionPath;
import model.State;
import model.Tile;
import view.MainApp;
import view.PuzzleView;


public class AppController {

    private final SolverController solverController = new SolverController();
    private SearchResult lastResult;
    private PuzzleGrid   lastGrid;

    public void generateSolusi(GameConfig config) {
        Stage stage = MainApp.getPrimaryStage();

        javafx.concurrent.Task<SearchResult> task = new javafx.concurrent.Task<>() {
            @Override
            protected SearchResult call() throws Exception {
                return solverController.solve(config);
            }
        };

        task.setOnSucceeded(e -> {
            lastResult = task.getValue();
            lastGrid   = solverController.getLastGrid();
            javafx.application.Platform.runLater(() ->
                navigateToPuzzleView(lastResult, lastGrid));
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            javafx.application.Platform.runLater(() -> showError(stage,
                ex != null ? ex.getMessage() : "Terjadi kesalahan yang tidak diketahui."));
        });

        new Thread(task, "solver-thread").start();
    }

    // navigasi ke halaman puzzle (board) — inject diri sendiri ke PuzzleView
    public void navigateToPuzzleView(SearchResult result, PuzzleGrid grid) {
        try {
            Stage stage = MainApp.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/resources/puzzle.fxml"));
            Parent root = loader.load();

            PuzzleView puzzleView = loader.getController();
            // Inject instance AppController ini (yang punya lastResult/lastGrid)
            puzzleView.setAppController(this);
            puzzleView.initWithResult(result, grid);

            Scene scene = new Scene(root,
                stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException ex) {
            showError(MainApp.getPrimaryStage(),
                "Gagal membuka halaman hasil: " + ex.getMessage());
        }
    }

    // navigasi ke halaman menu utama
    public void navigateToMenu() {
        try {
            Stage stage = MainApp.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/resources/menu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root,
                stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException ex) {
            showError(MainApp.getPrimaryStage(),
                "Gagal kembali ke menu: " + ex.getMessage());
        }
    }


    public void saveSolutionToFile(Stage stage) {
        if (lastResult == null || !lastResult.isSolutionFound()) {
            showError(stage, "Tidak ada solusi untuk disimpan.");
            return;
        }

        File outputFile = resolveOutputFile("solusi");
        if (outputFile == null) {
            showError(stage, "Gagal menentukan lokasi file output.");
            return;
        }

        try (BufferedWriter w = new BufferedWriter(new FileWriter(outputFile))) {
            SolutionPath path = lastResult.getSolutionPath();

            w.write("Ice Sliding Puzzle - Solusi \n");
            w.write("Algoritma  : " + lastResult.getAlgorithmUsed() + "\n");
            w.write("Heuristik  : " + lastResult.getHeuristicUsed() + "\n");
            w.write("Gerakan    : " + path.getMoveString() + "\n");
            w.write("Total Cost : " + (int) lastResult.getTotalCost() + "\n");
            w.write("\n");

            State initialState = path.getStateAt(0);
            w.write("Initial\n");
            w.write(renderGrid(lastGrid, initialState) + "\n");
            w.write("\n");

            for (int i = 0; i < path.getTotalSteps(); i++) {
                Direction dir   = path.getMoveAt(i);
                State     after = path.getStateAt(i + 1);

                w.write("Step " + (i + 1) + " : " + dir.getSymbol() + "\n");
                w.write(renderGrid(lastGrid, after) + "\n");
                w.write("\n");
            }

            w.write("Statistik \n");
            w.write("Total Cost     : " + (int) lastResult.getTotalCost() + "\n");
            w.write("Banyak Iterasi : " + lastResult.getNodesExplored() + "\n");
            w.write(String.format("Waktu Eksekusi : %.3f ms%n", lastResult.getExecutionTimeMs()));
            w.write("Jumlah Step    : " + path.getTotalSteps() + "\n");

            showInfo(stage, "Solusi disimpan ke:\n" + outputFile.getAbsolutePath());

        } catch (IOException ex) {
            showError(stage, "Gagal menyimpan file: " + ex.getMessage());
        }
    }


    public void saveIterationsToFile(Stage stage) {
        if (lastResult == null) {
            showError(stage, "Belum ada hasil solver.");
            return;
        }

        File outputFile = resolveOutputFile("statistik");
        if (outputFile == null) {
            showError(stage, "Gagal menentukan lokasi file output.");
            return;
        }

        try (BufferedWriter w = new BufferedWriter(new FileWriter(outputFile))) {
            w.write("Ice Sliding Puzzle Statistik \n");
            w.write("Algoritma      : " + lastResult.getAlgorithmUsed() + "\n");
            w.write("Heuristik      : " + lastResult.getHeuristicUsed() + "\n");
            w.write("Solusi         : " + (lastResult.isSolutionFound()
                ? "Ditemukan" : "Tidak ditemukan") + "\n");
            w.write("Total Cost     : " + (int) lastResult.getTotalCost() + "\n");
            w.write("Banyak Iterasi : " + lastResult.getNodesExplored() + "\n");
            w.write(String.format("Waktu Eksekusi : %.3f ms%n", lastResult.getExecutionTimeMs()));

            if (lastResult.isSolutionFound()) {
                SolutionPath path = lastResult.getSolutionPath();
                w.write("Jumlah Step    : " + path.getTotalSteps() + "\n");
                w.write("Urutan Gerakan : " + path.getMoveString() + "\n");
            }

            showInfo(stage, "Statistik disimpan ke:\n" + outputFile.getAbsolutePath());

        } catch (IOException ex) {
            showError(stage, "Gagal menyimpan file: " + ex.getMessage());
        }
    }


    private File resolveOutputFile(String suffix) {
        GameConfig config = solverController.getLastConfig();
        if (config == null) return null;

        File inputFile = new File(config.getInputFilePath());
        String baseName = inputFile.getName();
        if (baseName.contains(".")) {
            baseName = baseName.substring(0, baseName.lastIndexOf('.'));
        }

        File testDir = determineTestDir(inputFile);
        if (!testDir.exists()) {
            testDir.mkdirs();
        }

        int index = 1;
        File candidate;
        do {
            candidate = new File(testDir, baseName + "_" + suffix + "_" + index + ".txt");
            index++;
        } while (candidate.exists());

        return candidate;
    }


    private File determineTestDir(File inputFile) {
        File inputDir = inputFile.getParentFile();
        if (inputDir == null) inputDir = new File(".");

        if (inputDir.getName().equalsIgnoreCase("test")) {
            return inputDir;
        }

        File siblingTest = new File(inputDir, "test");
        if (siblingTest.exists() && siblingTest.isDirectory()) {
            return siblingTest;
        }

        File grandParent = inputDir.getParentFile();
        if (grandParent != null) {
            File rootTest = new File(grandParent, "test");
            if (rootTest.exists() && rootTest.isDirectory()) {
                return rootTest;
            }
        }

        return new File(inputDir, "test");
    }

    private String renderGrid(PuzzleGrid grid, State state) {
        int height = grid.getHeight();
        int width  = grid.getWidth();
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (x == state.getX() && y == state.getY()) {
                    sb.append('Z');
                    continue;
                }

                Tile tile = grid.getTile(x, y);
                switch (tile) {
                    case OBSTACLE -> sb.append('X');
                    case LAVA     -> sb.append('L');
                    case EXIT     -> sb.append('O');
                    case ICE      -> sb.append('*');
                    case START    -> sb.append('*');  
                    default -> {
                        if (tile.isGoal()) {
                            if (tile.getGoalIndex() < state.getNextGoalIndex()) {
                                sb.append('*');
                            } else {
                                sb.append(tile.toChar());
                            }
                        } else {
                            sb.append(tile.toChar());
                        }
                    }
                }
            }
            if (y < height - 1) sb.append('\n');
        }

        return sb.toString();
    }

    private void showInfo(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tersimpan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Terjadi kesalahan");
        alert.setContentText(message != null ? message : "Unknown error");
        alert.showAndWait();
    }
}
