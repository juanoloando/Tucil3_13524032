package view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SidebarView {
    @FXML private Label lblAlgorithm;
    @FXML private Label lblHeuristic;
    @FXML private VBox legendContainer;

    public void displayInfo(String algorithm, String heuristic) {
        lblAlgorithm.setText("Algoritma yang digunakan : " + algorithm);

        if ("NONE".equals(heuristic) || heuristic == null) {
            lblHeuristic.setText("Heuristic  : N/A");
        } else {
            lblHeuristic.setText("Heuristic  : " + heuristic);
        }

        buildLegend();
    }

    private void buildLegend() {
        legendContainer.getChildren().clear();

        addColorLegendItem(
                    "ICE",
                    "#BFEFFF",
                    "Lantai es / tile bisa dilewati"
            );

            addLegendItem(
                    "OBSTACLE",
                    "iceblock.png",
                    "Batu es"
            );

            addLegendItem(
                    "LAVA",
                    "lava.png",
                    "Lava"
            );

            addLegendItem(
                    "EXIT",
                    "exit.png",
                    "Exit / tujuan akhir"
            );

            addLegendItem(
                    "GOAL",
                    "0.png",
                    "Tile Berangka 0-9"
            );

            addLegendItem(
                    "PLAYER",
                    "player.png",
                    "Posisi karakter saat ini"
            );
        }

    private void addLegendItem(
                String label,
                String assetName,
                String description
        ) {

            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            var stream = getClass()
                    .getResourceAsStream(
                            "/resources/assets/" + assetName
                    );

            ImageView icon;

            if (stream != null) {

                icon = new ImageView(new Image(stream));

            } else {

                icon = new ImageView();
            }

            icon.setFitWidth(22);
            icon.setFitHeight(22);
            icon.setPreserveRatio(true);

            Label name = new Label(label);
            name.getStyleClass().add("legend-name");

            Label desc = new Label(description);
            desc.getStyleClass().add("legend-description");

            VBox textBox = new VBox(2, name, desc);

            row.getChildren().addAll(icon, textBox);

            legendContainer.getChildren().add(row);
        }
    private void addColorLegendItem(
                String label,
                String colorHex,
                String description
        ) {

            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            Rectangle rect = new Rectangle(22, 22);

            rect.setFill(Color.web(colorHex));
            rect.setArcWidth(6);
            rect.setArcHeight(6);

            rect.setStroke(Color.web("#D8F8FF"));
            rect.setStrokeWidth(1.2);

            Label name = new Label(label);
            name.getStyleClass().add("legend-name");

            Label desc = new Label(description);
            desc.getStyleClass().add("legend-description");

            VBox textBox = new VBox(2, name, desc);

            row.getChildren().addAll(rect, textBox);

            legendContainer.getChildren().add(row);
        }
}
