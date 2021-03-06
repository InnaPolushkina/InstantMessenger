package messenger.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import messenger.controller.Router;
import messenger.model.exceptions.UserRegistrationException;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * The class contain methods and FXML components for handling actions of user with form for registering user
 * @see @FXML
 * @see javafx.scene.control
 * @author Inna
 */
public class ViewRegister {
    @FXML
    private Button registrationButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Label errorMsg;
    @FXML
    private Button returnToLoginView;
    private Stage stage;


    private Router router;
    private static final Logger logger = Logger.getLogger(ViewRegister.class);

    /**
     * The constructor of this class
     * @param stage stage for showing register scene
     */
    public ViewRegister(Stage stage) {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Registration");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            logger.warn("while opening form for checkRegisteringUserInfo",e);
        }


        this.router = Router.getInstance();
    }

    /**
     * The method for initializing values to components of form and setting handlers for client actions
     */
    public void initialize() {
        setErrorMsg("");
        registrationButton.setOnAction(event -> {
           register();
        });
        userName.setOnKeyPressed(event -> {
            if ( event.getCode() == KeyCode.ENTER ) {
                userPassword.requestFocus();
            }
        });
        userPassword.setOnKeyPressed(event -> {
            register();
        });
        returnToLoginView.setOnAction(event -> returnToLoginView());
    }

    private void register() {
        String name = userName.getText().trim();
        String password = userPassword.getText().trim();
        if (name.length() != 0 && password.length() !=0 ) {
            if(password.length() >= 4) {
                try {
                    Router.getInstance().register(name, password);
                    Router.getInstance().getUser().setName(name);
                } catch (UserRegistrationException e) {
                    setErrorMsg(e.getMessage());
                }
            }
            else {
                setErrorMsg("Password length can't be less then 4 symbols !");
            }
        }
        else {
            setErrorMsg("Fields can't be empty !");
        }
    }

    /**
     * The setter for message to user about some error
     * @param errorMsg text message
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg.setText(errorMsg);
    }

    /**
     * The method for returning to login form
     */
    public void returnToLoginView() {
        new ViewLogin(stage);
    }
}
