package model;

import java.util.Locale;

public class GameConfig {
    private final String algorithmName;
    private final String heuristicName;
    private final String inputFilePath;

    public GameConfig(String algorithmName, String heuristicName, String inputFilePath) {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException("Nama algoritma tidak boleh kosong.");
        }

        if (inputFilePath == null || inputFilePath.isBlank()) {
            throw new IllegalArgumentException("Path file input tidak boleh kosong.");
        }

        this.algorithmName = normalizeAlgorithmName(algorithmName);
        this.heuristicName = normalizeHeuristicName(heuristicName);
        this.inputFilePath = inputFilePath;
    }

    private String normalizeAlgorithmName(String name) {
        String normalized = name.trim().toUpperCase(Locale.ROOT);

        return switch (normalized) {
            case "UCS" -> "UCS";
            case "GBFS" -> "GBFS";
            case "A*", "ASTAR", "A_STAR" -> "ASTAR";
            default -> throw new IllegalArgumentException("Algoritma tidak dikenal: " + name);
        };
    }

    private String normalizeHeuristicName(String name) {
        if (name == null || name.isBlank()) {
            return "NONE";
        }

        // Normalisasi: trim lalu uppercase untuk perbandingan
        String upper = name.trim().toUpperCase(Locale.ROOT);
 
        return switch (upper) {
            case "H1", "H1 MANHATTAN 2 TITIK" -> "H1";
            case "H2", "H2 MANHATTAN BERANTAI" -> "H2";
            case "H3", "H3 MINIMUM SLIDE"      -> "H3";
            case "N/A", "NA", "NONE"           -> "NONE";
            default -> throw new IllegalArgumentException("Heuristik tidak dikenal: " + name);
        };
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getHeuristicName() {
        return heuristicName;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public boolean requiresHeuristic() {
        return !algorithmName.equals("UCS");
    }
}