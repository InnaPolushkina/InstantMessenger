package messenger.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import messenger.controller.Router;

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

    public ViewRegister(Router router) {
        this.router = router;
    }

    public void initialize() {
        registrationButton.setOnAction(event -> {
            String name = userName.getText();
            String password = userPassword.getText();
            router.register(name,password);
            router.getUser().setName(name);
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
