package nntc.volosov.coursework;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.math.BigDecimal;
import java.util.Optional;

public class OrderItemsController {

    @FXML
    private TableView<OrderItems> tableView;
    @FXML
    private TableColumn<OrderItems, Integer> idColumn;
    @FXML
    private TableColumn<OrderItems, Integer> loanIdColumn;
    @FXML
    private TableColumn<OrderItems, Integer> bookIdColumn;
    @FXML
    private TableColumn<OrderItems, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItems, BigDecimal> priceColumn;

    @FXML
    private TextField fieldID;
    @FXML
    private TextField fieldLoanId;
    @FXML
    private TextField fieldBookId;
    @FXML
    private TextField fieldQuantity;
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
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldLoanId.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldLoanId.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldBookId.textProperty().addListener((observable, oldValue, newValue) -> {
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
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
            ObservableList<OrderItems> data = primaryDatabaseManager.fetchLoanItems();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            loanIdColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));
            bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            tableView.setItems(data);
        } else {
            System.out.println("primaryDatabaseManager is null");
        }
    }

    @FXML
    public void addRow() {
        primaryDatabaseManager.insertLoanItem(
                Integer.parseInt(fieldLoanId.getText()),
                Integer.parseInt(fieldBookId.getText()),
                Integer.parseInt(fieldQuantity.getText()),
                new BigDecimal(fieldPrice.getText())
        );
        updateTable();
        clearFields();
    }

    @FXML
    public void editRow() {
        if (fieldQuantity.getText().isEmpty() || fieldLoanId.getText().isEmpty()) {
            showWarning("Ошибка", "Поля 'Количество' и 'ID заказа' не могут быть пустыми.");
            return;
        }

        try {
            int quantity = Integer.parseInt(fieldQuantity.getText());
            int loanId = Integer.parseInt(fieldLoanId.getText());
            int bookId = Integer.parseInt(fieldBookId.getText());
            BigDecimal price = new BigDecimal(fieldPrice.getText());

            primaryDatabaseManager.updateLoanItem(
                    Integer.parseInt(fieldID.getText()), // ID заказа
                    loanId, // ID клиента
                    bookId, // ID книги
                    quantity, // Количество
                    price // Цена
            );

            updateTable();
            clearFields();
        } catch (NumberFormatException e) {
            showWarning("Ошибка", "Поля должны содержать корректные числовые значения.");
        }
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Действительно удалить запись с ID=%s?", fieldID.getText()))) {
            primaryDatabaseManager.deleteLoanItem(Integer.parseInt(fieldID.getText()));
            updateTable();
            clearFields();
        }
    }

    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            OrderItems selectedLoanItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedLoanItem != null) {
                fieldID.setText(String.valueOf(selectedLoanItem.getId()));
                fieldLoanId.setText(String.valueOf(selectedLoanItem.getLoanId()));
                fieldBookId.setText(String.valueOf(selectedLoanItem.getBookId()));
                fieldQuantity.setText(String.valueOf(selectedLoanItem.getQuantity()));
                fieldPrice.setText(String.valueOf(selectedLoanItem.getPrice()));
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
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
        fieldLoanId.clear();
        fieldBookId.clear();
        fieldQuantity.clear();
        fieldPrice.clear();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}