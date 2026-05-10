package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PuzzleGrid {
    private final Tile[][] grid;
    private final double[][] traversalCosts;
    private final int width;
    private final int height;
    private final int[] startPosition;
    private final int[] exitPosition;
    private final List<int[]> goalPositions;

    public PuzzleGrid(Tile[][] grid) {
        this(grid, null);
    }

    public PuzzleGrid(Tile[][] grid, double[][] traversalCosts) {
        validateBasicGrid(grid);

        this.height = grid.length;
        this.width = grid[0].length;
        this.grid = copyGrid(grid);

        if(traversalCosts == null){
            this.traversalCosts = createDefaultCosts(height, width);
        } else {
            this.traversalCosts = copyCosts(traversalCosts, height, width);
        }

        int[] foundStart = null;
        int[] foundExit = null;
        List<int[]> foundGoals = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = this.grid[y][x];

                if (tile == Tile.START) {
                    foundStart = new int[]{x, y};
                } else if (tile == Tile.EXIT) {
                    foundExit = new int[]{x, y};
                } else if (tile.isGoal()) {
                    foundGoals.add(new int[]{x, y, tile.getGoalIndex()});
                }
            }
        }

        this.startPosition = foundStart;
        this.exitPosition = foundExit;

        foundGoals.sort(Comparator.comparingInt(a -> a[2]));

        this.goalPositions = new ArrayList<>();
        for (int[] goal : foundGoals) {
            this.goalPositions.add(new int[]{goal[0], goal[1]});
        }
    }

    private void validateBasicGrid(Tile[][] grid) {
        Objects.requireNonNull(grid, "Grid tidak boleh null.");

        if (grid.length == 0) {
            throw new IllegalArgumentException("Grid tidak boleh kosong.");
        }

        if (grid[0] == null || grid[0].length == 0) {
            throw new IllegalArgumentException("Baris grid tidak boleh kosong.");
        }

        int expectedWidth = grid[0].length;

        for (int y = 0; y < grid.length; y++) {
            if (grid[y] == null || grid[y].length != expectedWidth) {
                throw new IllegalArgumentException("Semua baris grid harus memiliki panjang yang sama.");
            }

            for (int x = 0; x < expectedWidth; x++) {
                if (grid[y][x] == null) {
                    throw new IllegalArgumentException("Tile tidak boleh null pada posisi (" + x + ", " + y + ").");
                }
            }
        }
    }

    private Tile[][] copyGrid(Tile[][] source) {
        Tile[][] copied = new Tile[source.length][source[0].length];

        for (int y = 0; y < source.length; y++) {
            System.arraycopy(source[y], 0, copied[y], 0, source[y].length);
        }

        return copied;
    }

    private double[][] createDefaultCosts(int height, int width) {
        double[][] costs = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                costs[y][x] = 1.0;
            }
        }

        return costs;
    }

    private double[][] copyCosts(double[][] source, int expectedHeight, int expectedWidth) {
        if (source.length != expectedHeight) {
            throw new IllegalArgumentException("Jumlah baris cost tidak sesuai dengan tinggi grid.");
        }

        double[][] copied = new double[expectedHeight][expectedWidth];

        for (int y = 0; y < expectedHeight; y++) {
            if (source[y] == null || source[y].length != expectedWidth) {
                throw new IllegalArgumentException("Jumlah kolom cost tidak sesuai dengan lebar grid.");
            }

            for (int x = 0; x < expectedWidth; x++) {
                if (source[y][x] < 0) {
                    throw new IllegalArgumentException("Cost tile tidak boleh negatif.");
                }

                copied[y][x] = source[y][x];
            }
        }

        return copied;
    }

    public Tile getTile(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new IndexOutOfBoundsException("Koordinat di luar grid: (" + x + ", " + y + ")");
        }

        return grid[y][x];
    }

    public double getCost(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new IndexOutOfBoundsException("Koordinat cost di luar grid: (" + x + ", " + y + ")");
        }

        return traversalCosts[y][x];
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int[] getStartPosition() {
        return startPosition == null ? null : new int[]{startPosition[0], startPosition[1]};
    }

    public int[] getExitPosition() {
        return exitPosition == null ? null : new int[]{exitPosition[0], exitPosition[1]};
    }

    public List<int[]> getGoalPositions() {
        List<int[]> copied = new ArrayList<>();

        for (int[] goal : goalPositions) {
            copied.add(new int[]{goal[0], goal[1]});
        }

        return Collections.unmodifiableList(copied);
    }

    public int[] getGoalPosition(int index) {
        if (index < 0 || index >= goalPositions.size()) {
            throw new IndexOutOfBoundsException("Index goal tidak valid: " + index);
        }

        int[] goal = goalPositions.get(index);
        return new int[]{goal[0], goal[1]};
    }

    public int getTotalGoals() {
        return goalPositions.size();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}