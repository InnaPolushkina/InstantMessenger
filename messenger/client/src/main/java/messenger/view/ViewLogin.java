package messenger.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import messenger.controller.Router;
import org.apache.log4j.Logger;

import java.io.IOException;

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

    /**
     * The constructor of this class
     * @param stage stage for showing login scene
     */
    public ViewLogin(Stage stage) {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        try {
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

    /**
     * The method for initializing values to components of form and setting handlers for client actions
     */
    @FXML
    public void initialize() {
      errorUserMessage.setText(" ");
      loginButton.setOnAction(event -> {
          login();
        });
      registerButton.setOnAction(event -> {
          viewRegister = new ViewRegister(stage);
      });
      userName.setOnKeyPressed(event -> {
          if ( event.getCode() == KeyCode.ENTER ) {
              userPassword.requestFocus();
          }
      });
      userPassword.setOnKeyPressed(event -> {
          if ( event.getCode() == KeyCode.ENTER ) {
             login();
          }
      });
      stage.setOnCloseRequest(event -> {
          System.out.println("Close stage");
          System.exit(0);
      });
   }

    /**
     * The method for user authorizing
     * calls methods from Router for user login
     * @see Router
     */
   private void login() {
       try {
           String name = userName.getText().trim();
           String password = userPassword.getText().trim();
           Router.getInstance().getUserConnection().getUser().setName(name);
           Router.getInstance().login(name, password);
       }
       catch (NullPointerException e) {
           setErrorUserMessage("All fields must be filled !");
       }
   }

    /**
     * The getter for button for user checkRegisteringUserInfo
     * @return checkRegisteringUserInfo Button
     */
    public Button getRegisterButton() {
        return registerButton;
    }

    /**
     * The method for setting message to user about some error
     * @param errorUserMessage message text
     */
    public void setErrorUserMessage(String errorUserMessage) {
        this.errorUserMessage.setText(errorUserMessage);
    }

    /**
     * The getter for login button
     * @return login button
     */
    public Button getLoginButton() {
        return loginButton;
    }
}
