package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.PuzzleGrid;
import model.Tile;


public class FileParser {

    private FileParser() {}

    public static PuzzleGrid parse(String filePath) throws IOException {
        List<String> lines = readNonEmptyLines(filePath);

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("File kosong.");
        }

        int[] dims = parseDimensions(lines.get(0));
        int N = dims[0]; 
        int M = dims[1];  
        int totalLines = lines.size();

        if (N != M) {
            System.err.println("[PERINGATAN] Peta tidak persegi: N=" + N + ", M=" + M
                + ". Melanjutkan dengan ukuran " + N + "x" + M + ".");
        }

        if (totalLines < 2 * N + 1) {
            throw new IllegalArgumentException(
                "File kurang lengkap. Diharapkan " + (2 * N + 1) + " baris "
                + "(1 header + " + N + " peta + " + N + " cost), "
                + "Jumlah saat ini " + totalLines + " baris.");
        }

        Tile[][] grid = parseTileGrid(lines, 1, N, M);

        double[][] costs = parseCostGrid(lines, N + 1, N, M);

        return new PuzzleGrid(grid, costs);
    }

    private static Tile[][] parseTileGrid(List<String> lines, int startIdx, int N, int M) {
        Tile[][] grid = new Tile[N][M];

        for (int y = 0; y < N; y++) {
            String row = lines.get(startIdx + y);
            String cleaned = row.replaceAll("\\s+", "");

            if (cleaned.length() != M) {
                throw new IllegalArgumentException(
                    "Baris peta ke-" + (y + 1) + " panjangnya tidak sesuai. "
                    + "Diharapkan " + M + " karakter, ditemukan " + cleaned.length()
                    + ". Baris: \"" + row + "\"");
            }

            for (int x = 0; x < M; x++) {
                char c = cleaned.charAt(x);
                try {
                    grid[y][x] = Tile.fromChar(c);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                        "Karakter tidak valid '" + c + "' pada baris " + (y + 1)
                        + ", kolom " + (x + 1) + ".");
                }
            }
        }
        return grid;
    }


    private static double[][] parseCostGrid(List<String> lines, int startIdx, int N, int M) {
        double[][] costs = new double[N][M];

        for (int y = 0; y < N; y++) {
            String row      = lines.get(startIdx + y);
            String[] tokens = row.trim().split("\\s+");

            if (tokens.length != M) {
                throw new IllegalArgumentException(
                    "Baris cost ke-" + (y + 1) + " jumlah nilainya tidak sesuai. "
                    + "Diharapkan " + M + " nilai, ditemukan " + tokens.length
                    + ". Baris: \"" + row + "\"");
            }

            for (int x = 0; x < M; x++) {
                try {
                    double val = Double.parseDouble(tokens[x]);
                    if (val < 0) {
                        throw new IllegalArgumentException(
                            "Cost tidak boleh negatif pada baris " + (y + 1)
                            + ", kolom " + (x + 1) + ": " + val);
                    }
                    costs[y][x] = val;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "Nilai cost tidak valid '" + tokens[x] + "' pada baris "
                        + (y + 1) + ", kolom " + (x + 1) + ".");
                }
            }
        }
        return costs;
    }

    private static int[] parseDimensions(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException(
                "Baris pertama harus berisi dua angka N dan M. Kenyataan: \"" + line + "\"");
        }
        try {
            int N = Integer.parseInt(parts[0]);
            int M = Integer.parseInt(parts[1]);
            if (N <= 0 || M <= 0) {
                throw new IllegalArgumentException(
                    "N dan M harus > 0. Kenyataan: N=" + N + ", M=" + M);
            }
            return new int[]{N, M};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "N dan M harus berupa angka integer. Kenyataan: \"" + line + "\"");
        }
    }

    private static List<String> readNonEmptyLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }
}
