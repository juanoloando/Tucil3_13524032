package algorithm;

import model.PuzzleGrid;
import model.State;

public interface Heuristic {
    double estimate(State state, PuzzleGrid grid);

    static Heuristic fromName(String name) {
        if (name == null) return new DefaultHeuristic();
        return switch (name.toUpperCase()) {
            case "H1" -> new H1Manhattan2Titik();
            case "H2" -> new H2ManhattanBerantai();
            case "H3" -> new H3MinimumSlide();
            default -> new DefaultHeuristic();
        };
    }
}

// default heuristic buat algoritma uninform cost search karena sebenarnya ga perlu heuristic 
class DefaultHeuristic implements Heuristic {
    @Override
    public double estimate(State state, PuzzleGrid grid) {
        return 0.0;
    }
}

// Heuristic H1: menggunakan jarak Manhattan ke next goal 
class H1Manhattan2Titik implements Heuristic {
    @Override
    public double estimate(State state, PuzzleGrid grid) {
        int[] target;
        if (state.getNextGoalIndex() >= grid.getTotalGoals()) {
            target = grid.getExitPosition();
        } else {
            target = grid.getGoalPosition(state.getNextGoalIndex());
        }
        if (target == null) return 0.0;
        return manhattan(state.getX(), state.getY(), target[0], target[1]);
    }

    private double manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}

// Heuristic H2: menggunakan jarak Manhattan berantai ke semua goal yang tersedia lalu ke exit 
class H2ManhattanBerantai implements Heuristic {
    @Override
    public double estimate(State state, PuzzleGrid grid) {
        double total = 0.0;
        int currentX = state.getX();
        int currentY = state.getY();

        for (int i = state.getNextGoalIndex(); i < grid.getTotalGoals(); i++) {
            int[] goal = grid.getGoalPosition(i);
            total += manhattan(currentX, currentY, goal[0], goal[1]);
            currentX = goal[0];
            currentY = goal[1];
        }

        int[] exit = grid.getExitPosition();
        if (exit != null) {
            total += manhattan(currentX, currentY, exit[0], exit[1]);
        }
        return total;
    }

    private double manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
// Heuristic H3: minimal slide agar sampai ke next goal/exit
class H3MinimumSlide implements Heuristic {
    @Override
    public double estimate(State state, PuzzleGrid grid) {
        double total = 0.0;
        int cx = state.getX(), cy = state.getY();

        for (int i = state.getNextGoalIndex(); i < grid.getTotalGoals(); i++) {
            int[] goal = grid.getGoalPosition(i);
            int dx = Math.abs(cx - goal[0]);
            int dy = Math.abs(cy - goal[1]);
            if (dx == 0 || dy == 0){
                total += 1.0;
            }else{
                total += 2.0;
            }
            cx = goal[0]; cy = goal[1];
        } 

        int[] exit = grid.getExitPosition();
        if (exit != null) {
            int dx = Math.abs(cx - exit[0]);
            int dy = Math.abs(cy - exit[1]);
            if (dx == 0 || dy == 0){
                total += 1.0;
            }else{
                total += 2.0;
            }
        }
        return total;
    }
}
