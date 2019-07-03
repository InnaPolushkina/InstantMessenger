package messenger.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewRegister {
    @FXML
    private Button registrationButton;
    @FXML
    private TextField userName;
    @FXML
    private TextField userPassword;
    @FXML
    private Label errorMsg;

    public ViewRegister() {
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

    public void setUserPassword(TextField userPassword) {
        this.userPassword = userPassword;
    }

    public Label getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Label errorMsg) {
        this.errorMsg = errorMsg;
    }
}
