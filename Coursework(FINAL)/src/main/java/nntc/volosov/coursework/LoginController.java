package nntc.volosov.coursework;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.Preferences;
import java.io.IOException;

public class LoginController {

    // Поля ввода из login-view.fxml
    public TextField subdAddress;
    public TextField subdPort;
    public TextField subdDbname;
    public TextField subdUser;
    public TextField subdPassword;
    @FXML
    private TextField loginField;

    // Настройки, сохраняемые в пространстве пользователя
    private Preferences prefs;
    private Stage stage;

    // Метод жизненного цикла контроллера JavaFX, который вызывается после отрисовки FXML
    // Здесь можно считать сохранённые данные из Preferences или подставить умолчальные
    // (если пока сохранённых данных нет -- если это первый запуск и данные ещё не сохранялись)
    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(DesktopApplication.class);
        subdAddress.setText(prefs.get("subdAddress", "localhost"));
        subdPort.setText(prefs.get("subdPort", "5432"));
        subdDbname.setText(prefs.get("subdDbname", "postgres"));
        subdUser.setText(prefs.get("subdUser", "postgres"));
        subdPassword.setText(prefs.get("subdPassword", "postgres"));
    }

    // Устанавливает основной Stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Метод, вызываемый при нажатии на кнопку "Подключиться!"
    @FXML
    public void connect(ActionEvent actionEvent) {
        // Сохраняем параметры в Preferences
        prefs.put("subdAddress", subdAddress.getText());
        prefs.put("subdPort", subdPort.getText());
        prefs.put("subdDbname", subdDbname.getText());
        prefs.put("subdUser", subdUser.getText());
        prefs.put("subdPassword", subdPassword.getText());

        try {
            DesktopApplication app = new DesktopApplication();
            String login = loginField.getText().trim();
            if ("admin".equalsIgnoreCase(login)) {
                app.showMainView(stage);
            } else if ("user".equalsIgnoreCase(login)) {
                app.showUserMainView(stage);
            } else {
                ErrorDialog.showError("Ошибка входа", "Неверный логин");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

