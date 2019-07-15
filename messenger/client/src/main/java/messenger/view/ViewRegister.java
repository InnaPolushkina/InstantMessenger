package messenger.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messenger.controller.Router;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ViewRegister {
    @FXML
    private Button registrationButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Label errorMsg;

    private Router router;
    private static final Logger logger = Logger.getLogger(ViewRegister.class);

    public ViewRegister(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            logger.warn("while opening form for registration",e);
        }


        this.router = Router.getInstance();
    }

    public void initialize() {
        setErrorMsg("");
        registrationButton.setOnAction(event -> {
            String name = userName.getText().trim();
            String password = userPassword.getText().trim();
            if (name.length() != 0 && password.length() !=0) {
                Router.getInstance().register(name, password);
                Router.getInstance().getUser().setName(name);
            }
            else {
                setErrorMsg("Fields can't be empty !");
            }
        });
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg.setText(errorMsg);
    }

    public Button getRegistrationButton() {
        return registrationButton;
    }

    public void setRegistrationButton(Button registrationButton) {
        this.registrationButton = registrationButton;
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

    public Label getErrorMsg() {
        return errorMsg;
    }


}
