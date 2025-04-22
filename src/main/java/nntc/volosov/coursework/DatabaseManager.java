package nntc.volosov.coursework;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseManager {
    private Connection connection;

    // Метод для получения соединения с базой данных
    public void connect() {
        Preferences prefs = Preferences.userNodeForPackage(DesktopApplication.class);
        String url = String.format("jdbc:postgresql://%s:%s/%s", prefs.get("subdAddress", "localhost"), prefs.get("subdPort", "5432"), prefs.get("subdDbname", "postgres"));

        String user = prefs.get("subdUser", "postgres");
        String password = prefs.get("subdPassword", "postgres");

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Успешное подключение к базе данных");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка подключения к базе данных", e.getMessage()));
        }
    }

    // Метод для отключения от базы данных
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Успешное отключение от базы данных");
            } catch (SQLException e) {
                System.out.println("Ошибка отключения от базы данных: " + e.getMessage());
                Platform.runLater(() -> ErrorDialog.showError("Ошибка отключения от базы данных", e.getMessage()));
            }
        }
    }

    // Метод для проверки и создания таблицы заказов
    public void ensureUsersTableExists() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(50) UNIQUE NOT NULL,
                role VARCHAR(20) CHECK (role IN ('USER', 'ADMIN')) NOT NULL
            );
        """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Проверка и создание таблицы users завершены");
        } catch (SQLException e) {
            System.out.println("Ошибка при проверке/создании таблицы users: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при проверке/создании таблицы users", e.getMessage()));
        }
    }

    public ObservableList<User> fetchUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT id, username, role FROM users";

        try (var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при выполнении запроса", e.getMessage()));
        }
        return users;
    }

    public void insertUsers(String username, String role) {
        String query = "INSERT INTO users (username, role) VALUES (?, ?)";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role); // Исправлено с 3 на 2

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Добавлено строк: " + rowsInserted);
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при вставке данных", e.getMessage()));
        }
    }

    public void updateUser(int id, String username, String role) {
        String query = "UPDATE users SET username = ?, role = ? WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(3, role);
            preparedStatement.setInt(4, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Обновлено строк: " + rowsUpdated);
        } catch (SQLException e) {
            System.out.println("Ошибка при изменении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при изменении данных", e.getMessage()));
        }
    }

    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Удалено строк: " + rowsDeleted);
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при удалении данных", e.getMessage()));
        }
    }


    public void ensureGenresTableExists() {
        String createTableQuery = """
        CREATE TABLE IF NOT EXISTS genres (
            id SERIAL PRIMARY KEY,
            genre VARCHAR(100) UNIQUE NOT NULL
        );
    """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Проверка и создание таблицы genres завершены");
        } catch (SQLException e) {
            System.out.println("Ошибка при проверке/создании таблицы genres: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при проверке/создании таблицы genres", e.getMessage()));
        }
    }

    public ObservableList<Genres> fetchGenres() {
        ObservableList<Genres> genres = FXCollections.observableArrayList();
        String query = "SELECT id, genre FROM genres";

        try (var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                genres.add(new Genres(
                        resultSet.getInt("id"),
                        resultSet.getString("genre")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при выполнении запроса", e.getMessage()));
        }
        return genres;
    }

    public void insertGenres(String genre) {
        String query = "INSERT INTO genres (genre) VALUES (?)";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, genre);

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Добавлено строк: " + rowsInserted);
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при вставке данных", e.getMessage()));
        }
    }

    public void updateGenres(int id, String genre) {
        String query = "UPDATE genres SET genre = ? WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, genre);
            preparedStatement.setInt(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Обновлено строк: " + rowsUpdated);
        } catch (SQLException e) {
            System.out.println("Ошибка при изменении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при изменении данных", e.getMessage()));
        }
    }

    public void deleteGenres(int id) {
        String query = "DELETE FROM genres WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Удалено строк: " + rowsDeleted);
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при удалении данных", e.getMessage()));
        }
    }

    public void ensureBooksTableExists() {
        String createTableQuery = """
        CREATE TABLE IF NOT EXISTS books (
            id SERIAL PRIMARY KEY,
            title VARCHAR(255) NOT NULL,
            author VARCHAR(255) NOT NULL,
            available_copies INT NOT NULL CHECK (available_copies >= 0),
            genre_id INT REFERENCES genres(id) ON DELETE SET NULL,
            price DECIMAL(10, 2) NOT NULL DEFAULT 0.00
        );
    """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Проверка и создание таблицы books завершены");
        } catch (SQLException e) {
            System.out.println("Ошибка при проверке/создании таблицы books: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при проверке/создании таблицы books", e.getMessage()));
        }
    }

    public ObservableList<Books> fetchBooks() {
        ObservableList<Books> books = FXCollections.observableArrayList();
        String query = "SELECT id, title, author, available_copies, genre_id, price FROM books";

        try (var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                books.add(new Books(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("available_copies"),
                        resultSet.getInt("genre_id"),
                        resultSet.getBigDecimal("price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при выполнении запроса", e.getMessage()));
        }
        return books;
    }

    public void insertBook(String title, String author, int availableCopies, int genreId, BigDecimal price) {
        String query = "INSERT INTO books (title, author, available_copies, genre_id, price) VALUES (?, ?, ?, ?, ?)";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, availableCopies); // Исправлено с 4 на 3
            preparedStatement.setInt(4, genreId); // Исправлено с 5 на 4
            preparedStatement.setBigDecimal(5, price); // Исправлено с 6 на 5

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Вставлено строк: " + rowsInserted);
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при вставке данных", e.getMessage()));
        }
    }

    public void updateBook(int id, String title, String author, int availableCopies, int genreId, BigDecimal price) {
        String query = "UPDATE books SET title = ?, author = ?, available_copies = ?, genre_id = ?, price = ? WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, availableCopies); // Исправлено с 4 на 3
            preparedStatement.setInt(4, genreId); // Исправлено с 5 на 4
            preparedStatement.setBigDecimal(5, price); // Исправлено с 6 на 5
            preparedStatement.setInt(6, id); // Исправлено с 7 на 6

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Обновлено строк: " + rowsUpdated);
        } catch (SQLException e) {
            System.out.println("Ошибка при изменении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при изменении данных", e.getMessage()));
        }
    }

    public void deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Удалено строк: " + rowsDeleted);
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при удалении данных", e.getMessage()));
        }
    }

    public void ensureLoansTableExists() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS loans (
                id SERIAL PRIMARY KEY,
                user_id INT REFERENCES users(id) ON DELETE CASCADE,
                status VARCHAR(20) CHECK (status IN ('PURCHASED', 'RETURNED')) NOT NULL,
                total_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00
            );
        """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Проверка и создание таблицы loans завершены");
        } catch (SQLException e) {
            System.out.println("Ошибка при проверке/создании таблицы loans: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при проверке/создании таблицы loans", e.getMessage()));
        }
    }

    public ObservableList<Orders> fetchLoans() {
        ObservableList<Orders> loans = FXCollections.observableArrayList();
        String query = "SELECT id, user_id, status, total_price FROM loans";

        try (var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                loans.add(new Orders(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("status"),
                        resultSet.getBigDecimal("total_price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при извлечении данных из loans: " + e.getMessage());
        }

        return loans;
    }

    public void insertLoan(int userId, String status, BigDecimal totalPrice) {
        String query = "INSERT INTO loans (user_id, status, total_price) VALUES (?, ?, ?)";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            preparedStatement.setBigDecimal(3, totalPrice);

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Вставлено строк: " + rowsInserted);
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при вставке данных", e.getMessage()));
        }
    }

    public void updateLoan(int id, int userId, String status, BigDecimal totalPrice) {
        String query = "UPDATE loans SET user_id = ?, status = ?, total_price = ? WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            preparedStatement.setBigDecimal(3, totalPrice);
            preparedStatement.setInt(4, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Обновлено строк: " + rowsUpdated);
        } catch (SQLException e) {
            System.out.println("Ошибка при изменении данных: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при изменении данных", e.getMessage()));
        }
    }

    public void deleteLoan(int id) {
        String query = "DELETE FROM loans WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении данных: " + e.getMessage());
        }
    }

    public void ensureLoanItemsTableExists() {
        String createTableQuery = """
        CREATE TABLE IF NOT EXISTS loan_items (
            id SERIAL PRIMARY KEY,
            loan_id INT REFERENCES loans(id) ON DELETE CASCADE,
            book_id INT REFERENCES books(id) ON DELETE CASCADE,
            quantity INT NOT NULL,
            price DECIMAL(10, 2) NOT NULL DEFAULT 0.00
        );
    """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Проверка и создание таблицы loan_items завершены");
        } catch (SQLException e) {
            System.out.println("Ошибка при проверке/создании таблицы loan_items: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при проверке/создании таблицы loan_items", e.getMessage()));
        }
    }

    public ObservableList<OrderItems> fetchLoanItems() {
        ObservableList<OrderItems> orderItems = FXCollections.observableArrayList();
        String query = "SELECT id, loan_id, book_id, quantity, price FROM loan_items";

        try (var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                orderItems.add(new OrderItems(
                        resultSet.getInt("id"),
                        resultSet.getInt("loan_id"),
                        resultSet.getInt("book_id"),
                        resultSet.getInt("quantity"),
                        resultSet.getBigDecimal("price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
        }
        return orderItems;
    }

    public void insertLoanItem(int loanId, int bookId, int quantity, BigDecimal price) {
        String query = "INSERT INTO loan_items (loan_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, loanId);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setBigDecimal(4, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных: " + e.getMessage());
        }
    }

    public void updateLoanItem(int id, int loanId, int bookId, int quantity, BigDecimal price) {
        String query = "UPDATE loan_items SET loan_id = ?, book_id = ?, quantity = ?, price = ? WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, loanId);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setBigDecimal(4, price);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении данных: " + e.getMessage());
        }
    }

    public void deleteLoanItem(int id) {
        String query = "DELETE FROM loan_items WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении данных: " + e.getMessage());
        }
    }
    public void populateTables() {
        // Вставка пользователей
        insertUsers("admin", "ADMIN");
        insertUsers("user", "USER");

        // Вставка жанров
        insertGenres("Антиутопия");
        insertGenres("Художественная литература");
        insertGenres("Классика");

        // Вставка книг
        insertBook("1984", "Джордж Оруэлл",5, 2, new BigDecimal("500.00"));
        insertBook("Убить пересмешника", "Харпер Ли",10, 2, new BigDecimal("300.00"));
        insertBook("Война и мир", "Лев Толстой",3, 3, new BigDecimal("700.00"));
    }
}
