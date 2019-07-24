package messenger.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messenger.controller.Router;
import messenger.model.entity.Message;
import messenger.model.entity.Room;
import messenger.model.entity.User;
import org.apache.log4j.Logger;

import java.awt.*;
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
    private ListView<String> roomListView;
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
    private Label serverError;
    @FXML
    private Button muteRoom;

    private User user;
    private Notificator notificator;



    @FXML
    private ListView<String> messagesList = new ListView<>();

    ObservableList<String> observableListMessages = FXCollections.observableArrayList();
    ObservableList<String> roomObservableList = FXCollections.observableArrayList("Big chat");
    private Router router;
    private static final Logger logger = Logger.getLogger(ViewChat.class);

    private List<User> list;
    //private Set<Room> roomList;



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
        notificator = new Notificator();
    }

    @FXML
    public void initialize() {
        serverError.setText("");
        roomListView.setItems(roomObservableList);
        messagesList.setItems(observableListMessages);

        sendButton.setOnAction(event -> {
            if (messageText.getText() != null && !messageText.getText().trim().equals("")) {
                router.sendAction("SEND_MSG");
                router.sendMessage(messageText.getText().replace("\n", ""));
               // Router.getInstance().sendAction("SEND_MSG");
               // Router.getInstance().sendMessage(messageText.getText().replace("\n",""));
                messageText.setText("");
                messageText.requestFocus();
            }
        });
        logoutButton.setOnAction(event -> {
            //router.saveHistory();
            System.exit(0);
        });
        createNewRoom.setOnAction(event -> {
            ViewCreateRoom viewCreateRoom = new ViewCreateRoom(new Stage(),this);
            /*viewCreateRoom.getCreateNewRoomButton().setOnAction(event1 -> {
                router.sendAction("CREATE_ROOM");
                router.createRoom(viewCreateRoom.getNameNewRoom().getText());
            });*/
        });
        addUser.setOnAction(event -> {
            //list = router.getOnlineUser();
            //System.out.println(list.toString());
            router.sendAction("ONLINE_USERS");

        });
        roomListView.setOnMouseClicked(event -> {
            if (getNameOfSelectedRoom() != null) {
                nameRoom.setText(getNameOfSelectedRoom());
                setMessageList(getNameOfSelectedRoom());
                if (Router.getInstance().getRoomByName(nameRoom.getText()).isMuted()) {
                    muteRoom.setText("UnMute");
                }
            }
        });
        muteRoom.setOnAction(event -> {
            muteRoom();
        });


    }

    public void showAddUserView() {
        ViewAddUser viewAddUser = new ViewAddUser(new Stage(),list);
    }

    public void showMessage(Message message) {
        if(message.getNameRoomRecipient().equals(nameRoom.getText())) {
            String mess = message.getUserSender().getName() + " << " + message.getText();
            observableListMessages.add(mess);
        }
        if(!message.getUserSender().getName().equals(user.getName()) && !Router.getInstance().getRoomByName(message.getNameRoomRecipient()).isMuted() ) {
            notificator.notifyUser(message.getText(), "New message, from " + message.getUserSender().getName(), TrayIcon.MessageType.INFO);
        }
    }

    public void muteRoom() {
        String roomName = nameRoom.getText();
        if (Router.getInstance().getRoomByName(roomName).isMuted()) {
            Router.getInstance().getRoomByName(roomName).setMuted(false);
            muteRoom.setText("Mute");
        }
        else {
            Router.getInstance().getRoomByName(roomName).setMuted(true);
            muteRoom.setText("UnMute");
        }
    }

    public void setList(List<User> list) {
        this.list = list;
        for (User user: list) {
            roomObservableList.add(user.getName());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Room getSelectedRoom() {
        String nameSelectedRoom = roomListView.getSelectionModel().getSelectedItem();
        Room room = new Room(nameSelectedRoom);
        return room;
    }

    private String getNameOfSelectedRoom() {
        return roomListView.getSelectionModel().getSelectedItem();
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom.setText(nameRoom);
    }

    public void addRoom(String nameRoom) {
        roomObservableList.add(nameRoom);
    }

    public String getNameRoom() {
        return nameRoom.getText();
    }

    public void setServerError(String serverError) {
        this.serverError.setText(serverError);
    }

    public void setMessageList(String roomName) {
        try {
            if (roomName != null) {
                observableListMessages.clear();
                Room room = Router.getInstance().getRoomByName(roomName);
                for (Message mes : room.getMessageSet()) {
                    if (mes != null) {
                        observableListMessages.add(mes.getUserSender().getName() + " << " + mes.getText());
                    }
                }
            }
        }
        catch (Exception e) {
            logger.info("testing . . . .",e);
        }
    }
}
