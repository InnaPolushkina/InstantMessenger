package messenger.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messenger.controller.Router;
import messenger.model.entity.Message;
import messenger.model.entity.Room;
import messenger.model.entity.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


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
    private Button createNewRoom;
    @FXML
    private Button addUser;



    @FXML
    private ListView<String> messagesList = new ListView<>();

    ObservableList<String> observableListMessages = FXCollections.observableArrayList();
    ObservableList<String> userObservableList = FXCollections.observableArrayList();
    private Router router;
    private static final Logger logger = Logger.getLogger(ViewChat.class);

    private List<User> list;
    private List<Room> roomList;



    public ViewChat(Stage stage) {
        this.router = Router.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/mainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Messenger");
            stage.setScene(scene);
            stage.show();

            logger.info("show main scene ");

        } catch (IOException e) {
            logger.warn("while showing main scene ", e);
        }
    }

    @FXML
    public void initialize() {
       // Date date = new Date();

        usersList.setItems(userObservableList);

        messagesList.setItems(observableListMessages);
       // observableListMessages.add(date.);

        sendButton.setOnAction(event -> {
            if (messageText.getText() != null && !messageText.getText().trim().equals("")) {
                router.sendAction("SEND_MSG");
                router.sendMessage(messageText.getText().replace("\n", ""));
                messageText.setText("");
            }
        });
        logoutButton.setOnAction(event -> {
            System.exit(0);
        });
        createNewRoom.setOnAction(event -> {
            ViewCreateRoom viewCreateRoom = new ViewCreateRoom(new Stage(),this);
            /*viewCreateRoom.getCreateNewRoomButton().setOnAction(event1 -> {
                router.sendAction("CREATE_ROOM");
                router.createRoom(viewCreateRoom.getNameNewRoom().getText());
            });*/
        });

    }



    public void showMessage(Message message) {
        String mess = message.getUserSender().getName() + " << " + message.getText();
        observableListMessages.add(mess);
    }

    public void setList(List<User> list) {
        this.list = list;
        for (User user: list) {
            userObservableList.add(user.getName());
        }
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

    public void setNameRoom(String nameRoom) {
        this.nameRoom.setText(nameRoom);
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
