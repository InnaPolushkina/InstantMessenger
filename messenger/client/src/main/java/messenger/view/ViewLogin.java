package messenger.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.entity.User;
import org.apache.log4j.Logger;

import java.io.IOException;
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
    private static final Logger logger = Logger.getLogger(ViewLogin.class);
    private Stage stage;
    private ViewRegister viewRegister;

    public ViewLogin(Stage stage) {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        try {
            //loadForm("/login.fxml",viewLogin);
            loader.setLocation(Router.class.getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Authorization");
            stage.setScene(scene);
            stage.show();
            logger.info("show login scene ");
        }
        catch (IOException e) {
            logger.warn("while showing login scene ",e);
        }
    }

    public void initialize() {
      errorUserMessage.setText(" ");
      loginButton.setOnAction(event -> {
            String name = userName.getText().trim();
            String password = userPassword.getText().trim();
            Router.getInstance().getUserConnection().getUser().setName(name);
            Router.getInstance().login(name,password);
        });
      registerButton.setOnAction(event -> {
          viewRegister = new ViewRegister(stage);
      });
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
