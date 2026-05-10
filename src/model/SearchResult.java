package model;

public class SearchResult {
    private final boolean solutionFound;
    private final SolutionPath solutionPath;
    private final double totalCost;
    private final int nodesExplored;
    private final double executionTimeMs;
    private final String algorithmUsed;
    private final String heuristicUsed;

    private SearchResult(
            boolean solutionFound,
            SolutionPath solutionPath,
            double totalCost,
            int nodesExplored,
            double executionTimeMs,
            String algorithmUsed,
            String heuristicUsed
    ) {
        this.solutionFound = solutionFound;
        this.solutionPath = solutionPath;
        this.totalCost = totalCost;
        this.nodesExplored = nodesExplored;
        this.executionTimeMs = executionTimeMs;
        this.algorithmUsed = algorithmUsed;
        this.heuristicUsed = heuristicUsed;
    }

    public static SearchResult success(
            SolutionPath path,
            int nodesExplored,
            double timeMs,
            String algorithm,
            String heuristic
    ) {
        double cost = path == null ? 0.0 : path.getTotalCost();

        return new SearchResult(
                true,
                path,
                cost,
                nodesExplored,
                timeMs,
                algorithm,
                heuristic
        );
    }

    public static SearchResult failure(
            int nodesExplored,
            double timeMs,
            String algorithm,
            String heuristic
    ) {
        return new SearchResult(false, null,0.0, nodesExplored, timeMs, algorithm,heuristic);
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public SolutionPath getSolutionPath() {
        return solutionPath;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getNodesExplored() {
        return nodesExplored;
    }

    public double getexecutionTime() {
        return executionTimeMs;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public String getHeuristicUsed() {
        return heuristicUsed;
    }
}
