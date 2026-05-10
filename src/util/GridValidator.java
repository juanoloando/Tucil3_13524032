package util;

import java.util.ArrayList;
import java.util.List;

import model.PuzzleGrid;
import model.Tile;


public class GridValidator {

    private GridValidator() {}

    public static void validate(PuzzleGrid grid) {
        List<String> errors = new ArrayList<>();

        if (grid.getStartPosition() == null) {
            errors.add("Tidak ada tile START (Z) di peta.");
        }

        if (grid.getExitPosition() == null) {
            errors.add("Tidak ada tile EXIT (O) di peta.");
        }

        int totalGoals = grid.getTotalGoals();
        if (totalGoals > 0) {
            for (int i = 0; i < totalGoals; i++) {
                try {
                    grid.getGoalPosition(i);
                } catch (IndexOutOfBoundsException e) {
                    errors.add("Goal ke-" + i + " tidak ditemukan di peta. "
                        + "Goal harus berurutan mulai dari 0.");
                }
            }

            validateGoalSequence(grid, totalGoals, errors);
        }

        if (grid.getWidth() == 1 && grid.getHeight() == 1) {
            errors.add("Grid terlalu kecil (1x1).");
        }
        
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                "Puzzle tidak valid:\n" + String.join("\n", errors));
        }
    }

    private static void validateGoalSequence(PuzzleGrid grid, int totalGoals, List<String> errors) {
        boolean[] found = new boolean[10]; 
        int foundCount = 0;

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Tile tile = grid.getTile(x, y);
                if (tile.isGoal()) {
                    int idx = tile.getGoalIndex();
                    if (found[idx]) {
                        errors.add("Goal angka " + idx + " muncul lebih dari satu kali di peta.");
                    }
                    found[idx] = true;
                    foundCount++;
                }
            }
        }

        for (int i = 0; i < foundCount; i++) {
            if (!found[i]) {
                errors.add("Goal angka " + i + " tidak ditemukan, "
                    + "tapi ada goal dengan angka lebih besar. "
                    + "Goal harus berurutan tanpa gap mulai dari 0.");
            }
        }
    }
}   