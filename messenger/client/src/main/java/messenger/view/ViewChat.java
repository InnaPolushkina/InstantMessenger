package messenger.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import messenger.controller.Router;
import messenger.model.Message;

import java.time.LocalDate;
import java.util.Date;


/**
 * Class contain methods and FXML components for handling actions of user with main of messenger
 * @see @FXML
 * @see javafx.scene.control
 * @author Inna
 */
public class ViewChat {
    @FXML
    private VBox vBox;
    @FXML
    private ListView<String> usersList;
    @FXML
    private TextArea messageText;
    @FXML
    private Button sendButton;
    @FXML
    private Label userName;
    @FXML
    private Label nameRoom;
    @FXML
    private Button logoutButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<String> messagesList = new ListView<>();

    ObservableList<String> observableListMessages = FXCollections.observableArrayList();
    private Router router;

    public ViewChat(Router router) {
        this.router = router;
    }

    @FXML
    public void initialize() {
       // Date date = new Date();

        messagesList.setItems(observableListMessages);
       // observableListMessages.add(date.);

        sendButton.setOnAction(event -> {
            if (messageText.getText() != null && !messageText.getText().trim().equals("")) {
                router.sendMessage(messageText.getText());
                messageText.setText("");
            }
        });
        logoutButton.setOnAction(event -> {
            System.exit(0);
        });
    }

    public void showMessage(Message message) {
        String mess = message.getUser().getName() + " << " + message.getText();
        observableListMessages.add(mess);
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public ListView getUsersList() {
        return usersList;
    }

    public void setUsersList(ListView usersList) {
        this.usersList = usersList;
    }

    public String getMessageText() {
        return messageText.getText();
    }

    public void setMessageText(TextArea messageText) {
        this.messageText = messageText;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public void setSendButton(Button sendButton) {
        this.sendButton = sendButton;
    }

    public Label getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    public Label getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(Label nameRoom) {
        this.nameRoom = nameRoom;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void setLogoutButton(Button logoutButton) {
        this.logoutButton = logoutButton;
    }

    public Label getUserNameLabel() {
        return userNameLabel;
    }

    public void setUserNameLabel(Label userNameLabel) {
        this.userNameLabel = userNameLabel;
    }
}
