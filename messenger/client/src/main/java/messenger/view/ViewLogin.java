package messenger.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import messenger.model.message.MessageServer;
import messenger.model.Room;
import messenger.model.User;

import java.util.Set;

/**
 * the class contain methods and FXML components for handling actions of user with form for authorization user
 * @see @FXML
 * @see javafx.scene.control
 * @author Inna
 */
public class ViewLogin {
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Label errorUserMessage;

    public void initialize() {
      errorUserMessage.setText(" ");
   }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(Button registerButton) {
        this.registerButton = registerButton;
    }

    public TextField getUserName() {
        return userName;
    }

    public void setUserName(TextField userName) {
        this.userName = userName;
    }

    public TextField getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(PasswordField userPassword) {
        this.userPassword = userPassword;
    }

    public Label getErrorUserMessage() {
        return errorUserMessage;
    }

    public void setErrorUserMessage(String errorUserMessage) {
        this.errorUserMessage.setText(errorUserMessage);
    }

    public ViewLogin() {
        super();
    }

    public void showListOfAllRoom(Set<Room> rooms) {

    }

    public void showListOfAllUsers(Set<User> users) {

    }

    public void showMessage(MessageServer messageServer) {

    }

    public static void showLog(String log) {
        System.out.println(log);
    }
}
