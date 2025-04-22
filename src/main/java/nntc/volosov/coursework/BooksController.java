package nntc.volosov.coursework;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.math.BigDecimal;
import java.util.Optional;

public class BooksController {

    @FXML
    private TableView<Books> tableView;
    @FXML
    private TableColumn<Books, Integer> idColumn;
    @FXML
    private TableColumn<Books, String> titleColumn;
    @FXML
    private TableColumn<Books, String> authorColumn;
    @FXML
    private TableColumn<Books, Integer> availableCopiesColumn;
    @FXML
    private TableColumn<Books, Integer> genreIdColumn;
    @FXML
    private TableColumn<Books, BigDecimal> priceColumn;

    @FXML
    private TextField fieldID;
    @FXML
    private TextField fieldTitle;
    @FXML
    private TextField fieldAuthor;
    @FXML
    private TextField fieldAvailableCopies;
    @FXML
    private TextField fieldGenreId;
    @FXML
    private TextField fieldPrice;

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

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
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldTitle.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldAvailableCopies.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldGenreId.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });
    }

    @FXML
    public void updateTable() {
        if (primaryDatabaseManager != null) {
            ObservableList<Books> data = primaryDatabaseManager.fetchBooks();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
            genreIdColumn.setCellValueFactory(new PropertyValueFactory<>("genreId"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            tableView.setItems(data);
        } else {
            System.out.println("primaryDatabaseManager is null");
        }
    }

    @FXML
    public void addRow() {
        primaryDatabaseManager.insertBook(
                fieldTitle.getText(),
                fieldAuthor.getText(),
                Integer.parseInt(fieldAvailableCopies.getText()),
                Integer.parseInt(fieldGenreId.getText()),
                new BigDecimal(fieldPrice.getText())
        );
        updateTable();
        clearFields();
    }

    @FXML
    public void editRow() {
        primaryDatabaseManager.updateBook(
                Integer.parseInt(fieldID.getText()),
                fieldTitle.getText(),
                fieldAuthor.getText(),
                Integer.parseInt(fieldAvailableCopies.getText()),
                Integer.parseInt(fieldGenreId.getText()),
                new BigDecimal(fieldPrice.getText())
        );
        updateTable();
        clearFields();
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Действительно удалить запись с ID=%s?", fieldID.getText()))) {
            primaryDatabaseManager.deleteBook(Integer.parseInt(fieldID.getText()));
            updateTable();
            clearFields();
        }
    }

    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Books selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                fieldID.setText(String.valueOf(selectedBook.getId()));
                fieldTitle.setText(selectedBook.getTitle());
                fieldAuthor.setText(selectedBook.getAuthor());
                fieldAvailableCopies.setText(String.valueOf(selectedBook.getAvailableCopies()));
                fieldGenreId.setText(String.valueOf(selectedBook.getGenreId()));
                fieldPrice.setText(selectedBook.getPrice().toString());
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
        fieldTitle.clear();
        fieldAuthor.clear();
        fieldAvailableCopies.clear();
        fieldGenreId.clear();
        fieldPrice.clear();
    }
}