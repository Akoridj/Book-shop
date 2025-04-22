package nntc.volosov.coursework;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class UserMainController {

    private Stage primaryStage;
    private DatabaseManager primaryDatabaseManager;

    // Устанавливает основную сцену приложения
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setResizable(false); // Отключает изменение размера окна
        this.primaryStage.setOnCloseRequest(this::handleWindowClose); // Добавляет обработчик запроса на закрытие окна
    }

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    @FXML
    public void showShopWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shop-view.fxml"));
        fxmlLoader.setControllerFactory(param -> {
            ShopController shopController = new ShopController();
            shopController.setPrimaryDatabaseManager(primaryDatabaseManager);
            return shopController;
        });
        VBox shopContent = fxmlLoader.load();
        Stage shopStage = new Stage();
        shopStage.setTitle("Shop");
        shopStage.setScene(new Scene(shopContent, 1000, 800));
        shopStage.setResizable(false);
        shopStage.show();
    }

    @FXML
    public void showInfoWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("info-view.fxml"));
        VBox infoContent = fxmlLoader.load();
        Stage infoStage = new Stage();
        infoStage.setTitle("About");
        infoStage.setScene(new Scene(infoContent));
        infoStage.setResizable(false);
        infoStage.show();
    }

    @FXML
    public void returnToConnectionScreen(ActionEvent event) throws IOException {
        if (showConfirmationDialog("Вы действительно хотите вернуться к подключению к СУБД?")) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            DesktopApplication app = new DesktopApplication();
            app.showLoginView(stage);
        }
    }

    @FXML
    public void handleMenuClose(ActionEvent event) {
        handleWindowClose(null);
    }

    private void handleWindowClose(WindowEvent event) {
        if (primaryStage != null && showConfirmationDialog("Вы действительно хотите выйти?")) {
            primaryStage.close();
        } else if (event != null) {
            event.consume();
        }
    }

    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
