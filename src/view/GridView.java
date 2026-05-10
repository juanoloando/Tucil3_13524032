package view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import model.PuzzleGrid;
import model.State;
import model.Tile;

public class GridView extends Pane {

    private static final double MIN_TILE = 34.0;
    private static final double MAX_TILE = 112.0;
    private static final double PADDING  = 18.0;
    private static final double GAP      = 4.0;
    private static final double ARC      = 8.0;

    private static final Color BOARD_BG        = Color.web("#071523");
    private static final Color ICE_BASE_TOP    = Color.web("#D9F3FF");
    private static final Color ICE_BASE_BOTTOM = Color.web("#8ECFFF");
    private static final Color ICE_STROKE      = Color.web("#4AA3D8");
    private static final Color ICE_CRACK       = Color.web("#FFFFFF", 0.25);
    private static final Color ICE_BUBBLE      = Color.web("#FFFFFF", 0.35);

    private static final Color TRAIL_FILL      = Color.web("#BFEFFF", 0.92);
    private static final Color TRAIL_GLOW      = Color.web("#D8F8FF", 0.92);

    private final Canvas canvas = new Canvas();
    private final Map<String, Image> images = new HashMap<>();

    private PuzzleGrid grid;
    private double tileSize;
    private double offsetX;
    private double offsetY;

    private int playerX = -1;
    private int playerY = -1;

    private int currentGoalIdx = 0;

    private List<int[]> trailTiles = List.of();

    public GridView() {
        getChildren().add(canvas);
        setStyle("-fx-background-color: transparent;");
        loadAssets();
    }

    public void initialize(PuzzleGrid grid) {
        this.grid = grid;

        this.currentGoalIdx = 0;
        this.trailTiles = List.of();

        int[] start = grid.getStartPosition();

        playerX = (start != null) ? start[0] : -1;
        playerY = (start != null) ? start[1] : -1;

        recalculateTileSize();
        redrawAll(trailTiles);
    }

    public void renderStep(State state, List<int[]> highlightTiles) {
        this.playerX = state.getX();
        this.playerY = state.getY();

        this.currentGoalIdx = state.getNextGoalIndex();

        this.trailTiles = highlightTiles != null
                ? highlightTiles
                : List.of();

        redrawAll(this.trailTiles);
    }

    @Override
    protected void layoutChildren() {
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());

        recalculateTileSize();
        redrawAll(trailTiles);
    }

    private void loadAssets() {

        load("player", "player.png");

        load("obstacle", "iceblock.png");
        load("lava", "lava.png");
        load("exit", "exit.png");

        for (int i = 0; i <= 9; i++) {
            load("goal" + i, i + ".png");
        }
    }

    private void load(String key, String filename) {
        try {
            var url = getClass().getResource("/resources/assets/" + filename);

            if (url != null) {
                images.put(key, new Image(url.toExternalForm(), false));
            }

        } catch (Exception ignored) {
        }
    }

    private void recalculateTileSize() {

        if (grid == null ||
            canvas.getWidth() <= 0 ||
            canvas.getHeight() <= 0) {
            return;
        }

        double availW =
                canvas.getWidth()
                        - 2 * PADDING
                        - (grid.getWidth() - 1) * GAP;

        double availH =
                canvas.getHeight()
                        - 2 * PADDING
                        - (grid.getHeight() - 1) * GAP;

        tileSize = Math.max(
                MIN_TILE,
                Math.min(
                        MAX_TILE,
                        Math.min(
                                availW / grid.getWidth(),
                                availH / grid.getHeight()
                        )
                )
        );

        double totalW =
                grid.getWidth() * tileSize
                        + (grid.getWidth() - 1) * GAP;

        double totalH =
                grid.getHeight() * tileSize
                        + (grid.getHeight() - 1) * GAP;

        offsetX = (canvas.getWidth() - totalW) / 2.0;
        offsetY = (canvas.getHeight() - totalH) / 2.0;
    }

    private void redrawAll(List<int[]> trail) {

        if (grid == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawBoardBackground(gc);

        for (int y = 0; y < grid.getHeight(); y++) {

            for (int x = 0; x < grid.getWidth(); x++) {

                double px =
                        offsetX + x * (tileSize + GAP);

                double py =
                        offsetY + y * (tileSize + GAP);

                drawTile(gc, grid.getTile(x, y), px, py);
            }
        }

        for (int[] pos : trail) {

            double px =
                    offsetX + pos[0] * (tileSize + GAP);

            double py =
                    offsetY + pos[1] * (tileSize + GAP);

            drawTrail(gc, px, py);
        }

        if (playerX >= 0 && playerY >= 0) {

            double px =
                    offsetX + playerX * (tileSize + GAP);

            double py =
                    offsetY + playerY * (tileSize + GAP);

            drawPlayer(gc, px, py);
        }
    }

    private void drawBoardBackground(GraphicsContext gc) {

        gc.setFill(Color.TRANSPARENT);

        gc.fillRoundRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight(),
                6,
                6
        );
    }

    private void drawTile(
            GraphicsContext gc,
            Tile tile,
            double px,
            double py
    ) {

        switch (tile) {

            case ICE ->
                    drawIceTile(gc, px, py);

            case OBSTACLE ->
                    drawImageTile(
                            gc,
                            "obstacle",
                            px,
                            py,
                            () -> drawObstacleFallback(gc, px, py)
                    );

            case LAVA ->
                    drawImageTile(
                            gc,
                            "lava",
                            px,
                            py,
                            () -> drawLavaFallback(gc, px, py)
                    );

            case EXIT ->
                    drawImageTile(
                            gc,
                            "exit",
                            px,
                            py,
                            () -> drawExitFallback(gc, px, py)
                    );

            case START ->
                    drawIceTile(gc, px, py);

            default -> {

                if (tile.isGoal()) {
                    drawGoalTile(gc, tile, px, py);
                } else {
                    drawIceTile(gc, px, py);
                }
            }
        }
    }

    private void drawImageTile(
            GraphicsContext gc,
            String key,
            double px,
            double py,
            Runnable fallback
    ) {

        Image img = images.get(key);

        if (img != null && !img.isError()) {

            gc.drawImage(
                    img,
                    px,
                    py,
                    tileSize,
                    tileSize
            );

        } else {

            fallback.run();
        }
    }

    private void drawGoalTile(
            GraphicsContext gc,
            Tile tile,
            double px,
            double py
    ) {

        int idx = tile.getGoalIndex();

        if (idx < currentGoalIdx) {

            drawIceTile(gc, px, py);
            drawCollectedMark(gc, px, py);

            return;
        }

        drawImageTile(
                gc,
                "goal" + idx,
                px,
                py,
                () -> {

                    drawIceTile(gc, px, py);

                    drawLabel(
                            gc,
                            String.valueOf(idx),
                            px,
                            py,
                            Color.web("#004EAA")
                    );
                }
        );
    }

    private void drawIceTile(
            GraphicsContext gc,
            double px,
            double py
    ) {

        double t = tileSize;

        LinearGradient base = new LinearGradient(
                px,
                py,
                px + t,
                py + t,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#DDF6FF")),
                new Stop(0.45, ICE_BASE_TOP),
                new Stop(1.0, ICE_BASE_BOTTOM)
        );

        gc.setFill(base);

        gc.fillRoundRect(
                px,
                py,
                t,
                t,
                ARC,
                ARC
        );

        RadialGradient highlight = new RadialGradient(
                0,
                0,
                px + t * 0.22,
                py + t * 0.18,
                t * 0.36,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#FFFFFF", 0.60)),
                new Stop(0.60, Color.web("#FFFFFF", 0.12)),
                new Stop(1.0, Color.web("#FFFFFF", 0.00))
        );

        gc.setFill(highlight);

        gc.fillRoundRect(
                px,
                py,
                t,
                t,
                ARC,
                ARC
        );

        gc.save();

        gc.beginPath();

        gc.moveTo(px + ARC, py);

        gc.arcTo(px + t, py, px + t, py + t, ARC);
        gc.arcTo(px + t, py + t, px, py + t, ARC);
        gc.arcTo(px, py + t, px, py, ARC);
        gc.arcTo(px, py, px + t, py, ARC);

        gc.closePath();

        gc.clip();

        gc.setStroke(ICE_CRACK);

        gc.setLineWidth(Math.max(0.8, t * 0.014));

        gc.strokeLine(
                px + t * 0.32,
                py + t * 0.22,
                px + t * 0.45,
                py + t * 0.48
        );

        gc.strokeLine(
                px + t * 0.45,
                py + t * 0.48,
                px + t * 0.35,
                py + t * 0.70
        );

        gc.strokeLine(
                px + t * 0.60,
                py + t * 0.20,
                px + t * 0.50,
                py + t * 0.43
        );

        gc.strokeLine(
                px + t * 0.50,
                py + t * 0.43,
                px + t * 0.66,
                py + t * 0.66
        );

        gc.restore();

        gc.setStroke(ICE_STROKE);

        gc.setLineWidth(Math.max(1.4, t * 0.026));

        gc.strokeRoundRect(
                px,
                py,
                t,
                t,
                ARC,
                ARC
        );
    }

    private void drawTrail(
            GraphicsContext gc,
            double px,
            double py
    ) {

        double t = tileSize;

        gc.setFill(TRAIL_FILL);

        gc.fillRoundRect(
                px,
                py,
                t,
                t,
                ARC,
                ARC
        );

        RadialGradient glow = new RadialGradient(
                0,
                0,
                px + t * 0.5,
                py + t * 0.5,
                t * 0.52,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#FFFFFF", 0.55)),
                new Stop(0.70, Color.web("#BFEFFF", 0.20)),
                new Stop(1.0, Color.web("#BFEFFF", 0.00))
        );

        gc.setFill(glow);

        gc.fillRoundRect(
                px,
                py,
                t,
                t,
                ARC,
                ARC
        );

        gc.setStroke(TRAIL_GLOW);

        gc.setLineWidth(Math.max(1.4, t * 0.020));

        gc.strokeRoundRect(
                px + 1,
                py + 1,
                t - 2,
                t - 2,
                ARC,
                ARC
        );
    }

    private void drawPlayer(
            GraphicsContext gc,
            double px,
            double py
    ) {

        Image player = images.get("player");

        if (player != null && !player.isError()) {

            gc.drawImage(
                    player,
                    px + tileSize * 0.05,
                    py + tileSize * 0.05,
                    tileSize * 0.90,
                    tileSize * 0.90
            );

            return;
        }

        drawLabel(gc, "P", px, py, Color.WHITE);
    }

    private void drawCollectedMark(
            GraphicsContext gc,
            double px,
            double py
    ) {

        double fs = Math.max(13, tileSize * 0.36);

        gc.setFont(Font.font(
                "System",
                FontWeight.BOLD,
                fs
        ));

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFill(Color.web("#7DE8FF", 0.72));

        gc.fillText(
                "✓",
                px + tileSize / 2.0,
                py + tileSize / 2.0 + fs * 0.34
        );
    }

    private void drawLabel(
            GraphicsContext gc,
            String text,
            double px,
            double py,
            Color color
    ) {

        double fs = Math.max(11, tileSize * 0.38);

        gc.setFont(Font.font(
                "System",
                FontWeight.BOLD,
                fs
        ));

        gc.setTextAlign(TextAlignment.CENTER);

        double tx = px + tileSize / 2.0;
        double ty = py + tileSize / 2.0 + fs * 0.36;

        gc.setFill(Color.web("#002A58", 0.55));
        gc.fillText(text, tx + 1.2, ty + 1.2);

        gc.setFill(color);
        gc.fillText(text, tx, ty);
    }

    private void drawObstacleFallback(
            GraphicsContext gc,
            double px,
            double py
    ) {

        gc.setFill(BOARD_BG);

        gc.fillRoundRect(
                px,
                py,
                tileSize,
                tileSize,
                ARC,
                ARC
        );

        drawLabel(
                gc,
                "X",
                px,
                py,
                Color.web("#7FE8FF")
        );
    }

    private void drawLavaFallback(
            GraphicsContext gc,
            double px,
            double py
    ) {

        gc.setFill(Color.web("#5A1200"));

        gc.fillRoundRect(
                px,
                py,
                tileSize,
                tileSize,
                ARC,
                ARC
        );

        gc.setFill(Color.web("#FF7A00"));

        gc.fillOval(
                px + tileSize * 0.12,
                py + tileSize * 0.18,
                tileSize * 0.76,
                tileSize * 0.68
        );
    }

    private void drawExitFallback(
            GraphicsContext gc,
            double px,
            double py
    ) {

        gc.setFill(Color.web("#0B314D"));

        gc.fillRoundRect(
                px,
                py,
                tileSize,
                tileSize,
                ARC,
                ARC
        );

        drawLabel(
                gc,
                "O",
                px,
                py,
                Color.web("#B8F7FF")
        );
    }
}