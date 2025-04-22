package nntc.volosov.coursework;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class DesktopController {

    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    public TextField fieldID;
    @FXML
    public TextField fieldUsername;
    @FXML
    public TextField fieldEmail;
    @FXML
    public TextField fieldRole;
    @FXML
    private TextField totalPriceField;

    @FXML
    private TableView<Orders> ordersTableView;
    @FXML
    private TableColumn<Orders, Integer> orderIdColumn;
    @FXML
    private TableColumn<Orders, Integer> orderUserIdColumn;
    @FXML
    private TableColumn<Orders, LocalDateTime> orderDateColumn;
    @FXML
    private TableColumn<Orders, BigDecimal> orderTotalPriceColumn;
    @FXML
    private TableColumn<Orders, String> orderStatusColumn;

    @FXML
    private TextField orderIdField;
    @FXML
    private TextField orderUserIdField;
    @FXML
    private TextField orderStatusField;
    @FXML
    public Button btnEditOrder;
    @FXML
    public Button btnDeleteOrder;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private Stage primaryStage;
    private DatabaseManager primaryDatabaseManager;

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void showProductsWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("books-view.fxml"));
        VBox productsContent = fxmlLoader.load();
        BooksController booksController = fxmlLoader.getController();
        booksController.setPrimaryDatabaseManager(primaryDatabaseManager);
        Stage productsStage = new Stage();
        productsStage.setTitle("Товары");
        productsStage.setScene(new Scene(productsContent));
        productsStage.setResizable(false); // Отключить изменение размера окна
        booksController.updateTable(); // Обновить таблицу сразу
        productsStage.show();
    }

    @FXML
    private void returnToConnectionScreen(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        DesktopApplication app = new DesktopApplication();
        app.showLoginView(stage);
    }

    public void showCategoriesWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("genres-view.fxml"));
        VBox categoriesContent = fxmlLoader.load();
        GenresController genresController = fxmlLoader.getController();
        genresController.setPrimaryDatabaseManager(primaryDatabaseManager);
        primaryDatabaseManager.ensureGenresTableExists();
        Stage categoriesStage = new Stage();
        categoriesStage.setTitle("Жанры");
        categoriesStage.setScene(new Scene(categoriesContent));
        categoriesStage.setResizable(false); // Отключить изменение размера окна
        genresController.updateTable(); // Обновить таблицу сразу
        categoriesStage.show();
    }

    public void showOrderItemsWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("order-items-view.fxml"));
        VBox orderItemsContent = fxmlLoader.load();
        OrderItemsController orderItemsController = fxmlLoader.getController();
        orderItemsController.setPrimaryDatabaseManager(primaryDatabaseManager); // Установить DatabaseManager перед отображением окна
        Stage orderItemsStage = new Stage();
        orderItemsStage.setTitle("Товары в заказах");
        orderItemsStage.setScene(new Scene(orderItemsContent));
        orderItemsStage.setResizable(false);// Отключить изменение размера окна
        orderItemsController.updateTable(); // Обновить таблицу сразу
        orderItemsStage.show();
    }

    public void showUsersWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("users-view.fxml"));
        VBox usersContent = fxmlLoader.load();
        DesktopController usersController = fxmlLoader.getController();
        usersController.setPrimaryDatabaseManager(primaryDatabaseManager);
        Stage usersStage = new Stage();
        usersStage.setTitle("Пользователи");
        usersStage.setScene(new Scene(usersContent));
        usersStage.setResizable(false); // Отключить изменение размера окна
        usersController.updateTable();  // Обновить таблицу сразу
        usersStage.show();
    }

    public void showOrdersWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("orders-view.fxml"));
        VBox ordersContent = fxmlLoader.load();
        DesktopController ordersController = fxmlLoader.getController();
        ordersController.setPrimaryDatabaseManager(primaryDatabaseManager);
        Stage ordersStage = new Stage();
        ordersStage.setTitle("Заказы");
        ordersStage.setScene(new Scene(ordersContent));
        ordersStage.setResizable(false); // Отключить изменение размера окна
        ordersController.updateLoansTable(); // Обновить таблицу сразу
        ordersStage.show();
    }

    @FXML
    public void updateTable() {
        tableView.setItems(primaryDatabaseManager.fetchUsers());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        cleanupFields();
    }

    @FXML
    public void addRow() {
        primaryDatabaseManager.insertUsers(fieldUsername.getText(), fieldRole.getText());
        updateTable();
        cleanupFields();
    }

    @FXML
    public void editRow() {
        primaryDatabaseManager.updateUser(Integer.parseInt(fieldID.getText()), fieldUsername.getText(), fieldRole.getText());
        updateTable();
        cleanupFields();
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Вы действительно хотите удалить пользователя %s с ID=%s?", fieldUsername.getText(), fieldID.getText()))) {
            primaryDatabaseManager.deleteUser(Integer.parseInt(fieldID.getText()));
            updateTable();
            cleanupFields();
        }
    }

    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                fieldID.setText(String.valueOf(selectedUser.getId()));
                fieldUsername.setText(selectedUser.getUsername());
                fieldEmail.setText(selectedUser.getEmail());
                fieldRole.setText(selectedUser.getRole());
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            }
        }
    }

    // Метод для обработки закрытия меню
    public void handleMenuClose(ActionEvent event) {
        if (primaryStage != null && showConfirmationDialog("Вы действительно хотите выйти?")) {
            primaryDatabaseManager.disconnect();
            primaryStage.close();
        }
    }

    // Метод для отображения окна информации
    public void showInfoWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("info-view.fxml"));
        VBox infoContent = fxmlLoader.load();
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("О программе");
        primaryStage.setResizable(false); // Отключить изменение размера окна
        dialog.getDialogPane().setContent(infoContent);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            dialog.close();
        });

        dialog.showAndWait();
    }

    // Метод для очистки полей ввода пользователей
    private void cleanupFields() {
        fieldID.clear();
        fieldUsername.clear();
        fieldEmail.clear();
        fieldRole.clear();
    }

    @FXML
    public void updateLoansTable() {
        ordersTableView.setItems(primaryDatabaseManager.fetchLoans());
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        orderTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        cleanupOrderFields();
    }

    @FXML
    public void addOrder() {
        primaryDatabaseManager.insertLoan(
                Integer.parseInt(orderUserIdField.getText()),
                orderStatusField.getText(),
                new BigDecimal(totalPriceField.getText())
        );
        updateLoansTable();
        cleanupOrderFields();
    }

    @FXML
    public void editOrder() {
        primaryDatabaseManager.updateLoan(
                Integer.parseInt(orderIdField.getText()),
                Integer.parseInt(orderUserIdField.getText()),
                orderStatusField.getText(),
                new BigDecimal(totalPriceField.getText())
        );
        updateLoansTable();
        cleanupOrderFields();
    }

    @FXML
    public void deleteOrder() {
        if (showConfirmationDialog("Are you sure you want to delete this order?")) {
            primaryDatabaseManager.deleteLoan(Integer.parseInt(orderIdField.getText()));
            updateLoansTable();
            cleanupOrderFields();
        }
    }

    @FXML
    private void onOrderRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Orders selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                orderIdField.setText(String.valueOf(selectedOrder.getId()));
                orderUserIdField.setText(String.valueOf(selectedOrder.getUserId()));
                totalPriceField.setText(selectedOrder.getTotalPrice() != null ? selectedOrder.getTotalPrice().toString() : "0.00");
                orderStatusField.setText(selectedOrder.getStatus());
                btnEditOrder.setDisable(false);
                btnDeleteOrder.setDisable(false);
            }
        }
    }



    // Метод для очистки полей ввода заказов
    private void cleanupOrderFields() {
        orderIdField.clear();
        orderUserIdField.clear();
        totalPriceField.clear();
        orderStatusField.clear();
        btnEditOrder.setDisable(true);
        btnDeleteOrder.setDisable(true);
    }

    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}