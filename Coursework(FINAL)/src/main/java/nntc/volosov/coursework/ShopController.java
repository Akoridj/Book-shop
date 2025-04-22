package nntc.volosov.coursework;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

import java.math.BigDecimal;

public class ShopController {

    @FXML
    private TableView<Genres> tableView;
    @FXML
    private TableColumn<Genres, Integer> idColumn;
    @FXML
    private TableColumn<Genres, String> nameColumn;

    @FXML
    private TableView<Books> productsTableView;
    @FXML
    private TableView<Books> cartTableView;
    @FXML
    private TableColumn<Books, Integer> productIdColumn;
    @FXML
    private TableColumn<Books, String> productNameColumn;
    @FXML
    private TableColumn<Books, String> productDescriptionColumn;
    @FXML
    private TableColumn<Books, BigDecimal> productPriceColumn;
    @FXML
    private TableColumn<Books, Integer> productStockQuantityColumn;
    @FXML
    private TableColumn<Books, Integer> productCategoryIdColumn;

    @FXML
    private TableColumn<Books, Integer> cartProductIdColumn;
    @FXML
    private TableColumn<Books, String> cartProductNameColumn;
    @FXML
    private TableColumn<Books, String> cartProductDescriptionColumn;
    @FXML
    private TableColumn<Books, BigDecimal> cartProductPriceColumn;
    @FXML
    private TableColumn<Books, Integer> cartProductQuantityColumn;


    @FXML
    private TextField quantityField;
    @FXML
    private TextField totalPriceField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField categorySearchField;

    private DatabaseManager primaryDatabaseManager;
    private ObservableList<Books> cartItems = FXCollections.observableArrayList();

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        productCategoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

        cartProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cartProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cartProductDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        cartProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartProductQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInCart"));

        cartTableView.setItems(cartItems);


        updateProductsTable();
        updateGenresTable();
    }

    // Добавляет выбранный продукт в корзину
    @FXML
    private void addToCart() {
        if (quantityField.getText().isEmpty() || quantityField.getText().equals("0")) {
            showWarning("Ошибка", "Введите количество товара.");
            return;
        }

        int quantityToAdd = Integer.parseInt(quantityField.getText());
        Books selectedBooks = productsTableView.getSelectionModel().getSelectedItem();

        if (selectedBooks != null) {
            if (quantityToAdd > selectedBooks.getAvailableCopies()) {
                showWarning("Ошибка", "Недостаточно товара на складе.");
                return;
            }

            boolean productExistsInCart = false;
            for (Books books : cartItems) {
                if (books.getId() == selectedBooks.getId()) {
                    int newQuantityInCart = books.getQuantityInCart() + quantityToAdd;
                    if (newQuantityInCart > selectedBooks.getAvailableCopies()) {
                        showWarning("Ошибка", "Недостаточно товара на складе.");
                        return;
                    }
                    books.setQuantityInCart(newQuantityInCart);
                    productExistsInCart = true;
                    break;
                }
            }

            if (!productExistsInCart) {
                selectedBooks.setQuantityInCart(quantityToAdd);
                cartItems.add(selectedBooks);
            }

            // Уменьшаем количество доступных книг
            selectedBooks.setAvailableCopies(selectedBooks.getAvailableCopies() - quantityToAdd);

            // Обновляем таблицу продуктов
            productsTableView.refresh();

            updateTotalPrice();
            cartTableView.refresh();
        }
    }

    // Удаляет выбранный продукт из корзины
    @FXML
    private void removeFromCart() {
        if (quantityField.getText().isEmpty() || quantityField.getText().equals("0")) {
            showWarning("Ошибка", "Введите количество товара.");
            return;
        }

        Books selectedBooks = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedBooks != null) {
            int quantityToRemove = Integer.parseInt(quantityField.getText());
            if (quantityToRemove > selectedBooks.getQuantityInCart()) {
                showWarning("Ошибка", "Недостаточно товара в корзине.");
                return;
            }

            // Увеличиваем количество доступных книг
            selectedBooks.setAvailableCopies(selectedBooks.getAvailableCopies() + quantityToRemove);

            selectedBooks.setQuantityInCart(selectedBooks.getQuantityInCart() - quantityToRemove);
            if (selectedBooks.getQuantityInCart() == 0) {
                cartItems.remove(selectedBooks);
            }

            // Обновляем таблицы
            productsTableView.refresh();
            cartTableView.refresh();
            updateTotalPrice();
        }
    }

    // Ищет продукты по имени
    @FXML
    private void searchProductsByName() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Books> filteredBooks = FXCollections.observableArrayList();
        for (Books books : primaryDatabaseManager.fetchBooks()) {
            if (books.getName().toLowerCase().contains(searchText)) {
                filteredBooks.add(books);
            }
        }
        productsTableView.setItems(filteredBooks);
    }

    // Ищет продукты по категории
    @FXML
    private void searchProductsByCategory() {
        String searchText = categorySearchField.getText().toLowerCase();
        ObservableList<Books> filteredBooks = FXCollections.observableArrayList();
        for (Books books : primaryDatabaseManager.fetchBooks()) {
            if (String.valueOf(books.getCategoryId()).contains(searchText)) {
                filteredBooks.add(books);
            }
        }
        productsTableView.setItems(filteredBooks);
    }

    // Создает заказ из товаров в корзине
    @FXML
    private void createOrder() {
        if (cartItems.isEmpty()) {
            showWarning("Ошибка", "Корзина пуста.");
            return;
        }

        BigDecimal totalPrice = new BigDecimal(totalPriceField.getText());
        primaryDatabaseManager.insertLoan(1, "PURCHASED", totalPrice);

        Optional<Orders> optionalLoan = primaryDatabaseManager.fetchLoans().stream()
                .filter(loan -> loan.getStatus().equals("PURCHASED"))
                .findFirst();

        if (optionalLoan.isPresent()) {
            int orderId = optionalLoan.get().getId();

            for (Books books : cartItems) {
                primaryDatabaseManager.insertLoanItem(orderId, books.getId(), books.getQuantityInCart(), books.getPrice());
            }

            cartItems.clear();
            updateTotalPrice();
            updateProductsTable();
            showWarning("Заказ создан", "Ваш заказ успешно создан.");
        } else {
            showWarning("Ошибка", "Не удалось создать заказ.");
        }
    }

    // Обновляет общую цену товаров в корзине
    private void updateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Books books : cartItems) {
            total = total.add(books.getPrice().multiply(new BigDecimal(books.getQuantityInCart())));
        }
        totalPriceField.setText(total.toString());
    }

    private void updateProductsTable() {
        productsTableView.setItems(primaryDatabaseManager.fetchBooks());
    }

    private void updateGenresTable() {
        tableView.setItems(primaryDatabaseManager.fetchGenres());
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}