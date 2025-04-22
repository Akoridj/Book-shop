package nntc.volosov.coursework;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.Optional;

public class GenresController {

    @FXML
    private TableView<Genres> tableView;
    @FXML
    private TableColumn<Genres, Integer> idColumn;
    @FXML
    private TableColumn<Genres, String> nameColumn;

    @FXML
    public TextField fieldID;
    @FXML
    public TextField fieldName;

    @FXML
    public Button btnEdit;
    @FXML
    public Button btnDelete;

    private Boolean disableEditOrDeleteBtnsFlag = true;

    private DatabaseManager primaryDatabaseManager;

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
            updateTable();
        });

        fieldID.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldName.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });
    }

    @FXML
    public void updateTable() {
        if (primaryDatabaseManager != null) {
            ObservableList<Genres> data = primaryDatabaseManager.fetchGenres();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            tableView.setItems(data);
        } else {
            System.out.println("primaryDatabaseManager is null");
        }
    }

    @FXML
    public void addRow() {
        primaryDatabaseManager.insertGenres(fieldName.getText());
        updateTable();
        clearFields();
    }

    @FXML
    public void editRow() {
        primaryDatabaseManager.updateGenres(
                Integer.parseInt(fieldID.getText()),
                fieldName.getText()
        );
        updateTable();
        clearFields();
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Действительно удалить жанр %s с ID=%s?", fieldName.getText(), fieldID.getText()))) {
            primaryDatabaseManager.deleteGenres(Integer.parseInt(fieldID.getText()));
            updateTable();
            clearFields();
        }
    }

    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Genres selectedGenre = tableView.getSelectionModel().getSelectedItem();
            if (selectedGenre != null) {
                fieldID.setText(String.valueOf(selectedGenre.getId()));
                fieldName.setText(selectedGenre.getGenre());
            }
        }
    }

    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void clearFields() {
        fieldID.clear();
        fieldName.clear();
    }
}