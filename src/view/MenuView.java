package view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import controller.AppController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.GameConfig;

// controller buat halaman menu, hanya ui untuk pilih file, algoritma, heuristic dan generate
public class MenuView implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;

    @FXML private Label lblInputText;
    @FXML private Label lblAlgorithmText;
    @FXML private Label lblHeuristicText;
    @FXML private Label lblError;

    @FXML private StackPane popupOverlay;
    @FXML private ImageView popupImage;
    @FXML private Label popupOption1;
    @FXML private Label popupOption2;
    @FXML private Label popupOption3;

    private enum PopupMode { ALGORITHM, HEURISTIC }

    private final AppController appController = new AppController();

    private File selectedFile;
    private String selectedAlgorithm;
    private String selectedHeuristic;
    private PopupMode activePopupMode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        lblInputText.setText("Input File Puzzle (.txt)");
        lblAlgorithmText.setText("Pilih Algoritma");
        lblHeuristicText.setText("Pilih Heuristic");

        clearError();
        hidePopup();
    }

    @FXML
    private void onBrowseClicked() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih File Puzzle (.txt)");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
        );

        File file = chooser.showOpenDialog(MainApp.getPrimaryStage());
        if (file != null) {
            selectedFile = file;
            lblInputText.setText(shortenFileName(file.getName()));
            clearError();
        }
    }

    @FXML
    private void onAlgorithmClicked() {
        showPopup(PopupMode.ALGORITHM);
    }

    @FXML
    private void onHeuristicClicked() {
        if ("UCS".equals(selectedAlgorithm)) {
            showError("UCS tidak menggunakan heuristic.");
            return;
        }
        showPopup(PopupMode.HEURISTIC);
    }

    @FXML
    private void onGenerateClicked() {
        if (selectedFile == null) {
            showError("Harap pilih file puzzle (.txt) terlebih dahulu.");
            return;
        }
        if (selectedAlgorithm == null) {
            showError("Harap pilih algoritma terlebih dahulu.");
            return;
        }
        if (!"UCS".equals(selectedAlgorithm) && selectedHeuristic == null) {
            showError("Harap pilih heuristic terlebih dahulu.");
            return;
        }

        clearError();
        GameConfig config = new GameConfig(
            selectedAlgorithm,
            "UCS".equals(selectedAlgorithm) ? "NONE" : selectedHeuristic,
            selectedFile.getAbsolutePath()
        );
        appController.generateSolusi(config);
    }

    @FXML
    private void onPopupOption1Clicked() {
        selectPopupOption(popupOption1.getText());
    }

    @FXML
    private void onPopupOption2Clicked() {
        selectPopupOption(popupOption2.getText());
    }

    @FXML
    private void onPopupOption3Clicked() {
        selectPopupOption(popupOption3.getText());
    }

    @FXML
    private void onPopupBackdropClicked() {
        hidePopup();
    }

    private void showPopup(PopupMode mode) {
        activePopupMode = mode;
        clearError();

        if (mode == PopupMode.ALGORITHM) {
            popupImage.setImage(loadAsset("popup_algoritma.png"));
            popupOption1.setText("A*");
            popupOption2.setText("GBFS");
            popupOption3.setText("UCS");
        } else {
            popupImage.setImage(loadAsset("popup_heuristic.png"));
            popupOption1.setText("H1 Manhattan 2 Titik");
            popupOption2.setText("H2 Manhattan Berantai");
            popupOption3.setText("H3 Minimum Slide");
        }

        popupOverlay.setVisible(true);
        popupOverlay.setManaged(true);
        popupOverlay.toFront();
    }

    private void hidePopup() {
        popupOverlay.setVisible(false);
        popupOverlay.setManaged(false);
        activePopupMode = null;
    }

    private void selectPopupOption(String value) {
        if (activePopupMode == PopupMode.ALGORITHM) {
            selectedAlgorithm = value;
            lblAlgorithmText.setText("Algoritma: " + value);

            if ("UCS".equals(value)) {
                selectedHeuristic = null;
                lblHeuristicText.setText("Heuristic: N/A");
                lblHeuristicText.getStyleClass().add("menu-placeholder-disabled");
            } else {
                lblHeuristicText.getStyleClass().remove("menu-placeholder-disabled");
                if (selectedHeuristic == null) {
                    lblHeuristicText.setText("Pilih Heuristic");
                }
            }
        } else if (activePopupMode == PopupMode.HEURISTIC) {
            selectedHeuristic = value;
            lblHeuristicText.getStyleClass().remove("menu-placeholder-disabled");
            lblHeuristicText.setText("Heuristic: " + value);
        }
        hidePopup();
    }

    private Image loadAsset(String fileName) {
        URL resource = getClass().getResource("/resources/assets/" + fileName);
        if (resource == null) {
            throw new IllegalStateException("Asset tidak ditemukan: /resources/assets/" + fileName);
        }
        return new Image(resource.toExternalForm());
    }

    private String shortenFileName(String name) {
        if (name.length() <= 26) return name;
        return name.substring(0, 10) + "..." + name.substring(name.length() - 12);
    }

    private void showError(String message) {
        lblError.setText("ERROR:" + message);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void clearError() {
        lblError.setText("");
        lblError.setVisible(false);
        lblError.setManaged(false);
    }
}
