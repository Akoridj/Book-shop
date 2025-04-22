package nntc.volosov.coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

public class DesktopApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        showLoginView(stage);
    }

    // Метод для отображения окна входа
    public void showLoginView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DesktopApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Авторизация");
        stage.setResizable(false); // Отключить изменение размера окна
        stage.show();
    }

    // Метод для отображения основного окна пользователя
    public void showUserMainView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DesktopApplication.class.getResource("USERmain-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);

        DatabaseManager dbManager = new DatabaseManager();
        dbManager.connect();

        UserMainController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
        controller.setPrimaryDatabaseManager(dbManager);

        stage.setScene(scene);
        stage.setTitle("Окно пользователя");
        stage.show();
    }

    // Метод для отображения основного окна приложения
    public void showMainView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DesktopApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        DatabaseManager dbManager = new DatabaseManager();
        dbManager.connect();

        try {
            dbManager.ensureUsersTableExists();
            dbManager.ensureGenresTableExists();
            dbManager.ensureBooksTableExists();
            dbManager.ensureLoansTableExists();
            dbManager.ensureLoanItemsTableExists();
        } catch (Exception e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
            Platform.runLater(() -> ErrorDialog.showError("Ошибка при создании таблицы", e.getMessage()));
            dbManager.disconnect();
        }

        DesktopController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
        controller.setPrimaryDatabaseManager(dbManager);

        // Обработчик события закрытия окна
        stage.setOnCloseRequest(event -> {
            event.consume();
            controller.handleMenuClose(null);
        });

        stage.setTitle("Main");
        stage.setScene(scene);
        stage.setResizable(false); // Отключить изменение размера окна
        stage.show();
    }
}