package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.SearchResult;

public class ResultView {
    @FXML private TextArea txtSolutionMoves;
    @FXML private Label lblTotalCost;
    @FXML private Label lblIterations;
    @FXML private Label lblExecutionTime;
    @FXML private VBox resultContainer;
    @FXML private Label lblNoSolution;


    public void displayResult(SearchResult result) {
        if (result.isSolutionFound()) {
        
            lblNoSolution.setVisible(false);
            lblNoSolution.setManaged(false);
            resultContainer.setVisible(true);
            resultContainer.setManaged(true);

            String moveString = buildMoveString(result);
            txtSolutionMoves.setText(moveString);
            txtSolutionMoves.setEditable(false);
            txtSolutionMoves.setWrapText(true);

            lblTotalCost.setText(String.format("Total Cost: %.0f", result.getTotalCost()));
            lblIterations.setText(String.format("Banyak Iterasi: %,d", result.getNodesExplored()));
            lblExecutionTime.setText(String.format("Waktu Eksekusi: %.3f ms", result.getExecutionTimeMs()));

        } else {
            resultContainer.setVisible(false);
            resultContainer.setManaged(false);
            lblNoSolution.setVisible(true);
            lblNoSolution.setManaged(true);
            lblNoSolution.setText("Solusi tidak ditemukan.");
            lblIterations.setText(String.format("Banyak Iterasi: %,d", result.getNodesExplored()));
            lblExecutionTime.setText(String.format("Waktu Eksekusi: %.3f ms", result.getExecutionTimeMs()));
        }
    }

    public String getSolutionMovesText() {
        return txtSolutionMoves.getText();
    }
    private String buildMoveString(SearchResult result) {
        if (result.getSolutionPath() == null) return "-";

        StringBuilder sb = new StringBuilder();
        for (var dir : result.getSolutionPath().getMoveSequence()) {
            sb.append(switch (dir) {
                case UP    -> "U";
                case DOWN  -> "D";
                case LEFT  -> "L";
                case RIGHT -> "R";
            });
        }
        return sb.toString();
    }
}
