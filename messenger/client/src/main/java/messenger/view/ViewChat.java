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
    @FXML
    private Button leaveRoom;
    @FXML
    private Button banUserButton;
    @FXML
    private Button unbanUserButton;
    @FXML
    private Label adminInfo;
    @FXML
    private Button deleteRoom;
    @FXML
    private Button roomInfo;

    private Stage stage;

    private User user;
    private Notificator notificator;

    @FXML
    private ListView<String> messagesList = new ListView<>();

    ObservableList<String> observableListMessages = FXCollections.observableArrayList();
    ObservableList<String> roomObservableList = FXCollections.observableArrayList();
    private Router router;
    private static final Logger logger = Logger.getLogger(ViewChat.class);

    private List<User> list;


    /**
     * Constructor of class ViewChat
     * loads main form of chat
     * @param stage have stage for loading new scene with main view
     */
    public ViewChat(Stage stage) {
        this.router = Router.getInstance();
        this.stage = stage;
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

    /**
     * The main method initializes components of form values
     * and sets handlers for client action on the form
     */
    @FXML
    public void initialize() {
        serverError.setText("");
        roomListView.setItems(roomObservableList);
        messagesList.setItems(observableListMessages);
        nameRoom.setText("Select room ...");
        adminInfo.setText("");
        hideComponents();

        sendButton.setOnAction(event -> {
            if (messageText.getText() != null && !messageText.getText().trim().equals("")) {
                router.sendAction("SEND_MSG");
                router.sendMessage(messageText.getText().replace("\n", ""));
                messageText.setText("");
                messageText.requestFocus();
            }
        });
        logoutButton.setOnAction(event -> {
            showViewExit();
        });
        createNewRoom.setOnAction(event -> {
            ViewCreateRoom viewCreateRoom = new ViewCreateRoom(new Stage(),this);
        });
        addUser.setOnAction(event -> {
            router.switchRoom(nameRoom.getText());
            router.sendAction("ONLINE_USERS");
            router.sendMessage("some text for test");

        });
        banUserButton.setOnAction(event -> {
            router.switchRoom(nameRoom.getText());
            router.sendAction("BAN_LIST");
            router.sendMessage("some text for test");
        });
        unbanUserButton.setOnAction(event -> {
            router.switchRoom(nameRoom.getText());
            router.sendAction("UNBAN_LIST");
            router.sendMessage("some text for test");
        });
        roomListView.setOnMouseClicked(event -> {
            switchRoom();
        });
        muteRoom.setOnAction(event -> {
            muteRoom();
        });
        leaveRoom.setOnAction(event -> {
            router.switchRoom(nameRoom.getText());
            router.leaveRoom(nameRoom.getText());
            observableListMessages.clear();
            roomListView.getItems().remove(nameRoom.getText());
            nameRoom.setText("Select room ...");
            hideComponents();
        });
        deleteRoom.setOnAction(event -> {
            router.deleteRoom(nameRoom.getText());
        });
        stage.setOnCloseRequest(event -> {
            showViewExit();
            event.consume();
        });
        roomInfo.setOnAction(event -> {
            Router.getInstance().getUserFromRoom(nameRoom.getText());
        });
    }

    /**
     * The method hides some fxml components
     */
    private void hideComponents() {
        muteRoom.setVisible(false);
        roomInfo.setVisible(false);
        addUser.setVisible(false);
        leaveRoom.setVisible(false);
        deleteRoom.setVisible(false);
        banUserButton.setVisible(false);
        unbanUserButton.setVisible(false);
        messageText.setVisible(false);
        sendButton.setVisible(false);
    }

    /**
     * The methods create object of class ViewAddUser
     * @see ViewAddUser
     */
    public void showAddUserView() {
        new ViewAddUser(list);
    }

    private void showViewExit() {
        new ViewExit(stage);
    }

    /**
     * The method creates object of class ViewBanUser
     * @see ViewBanUser
     */
    public void showBanUserView() {
       new ViewBanUser(list,Router.getInstance().getRoomByName(nameRoom.getText()),userName.getText());
    }

    /**
     * The method creates object of class ViewUnBanUser
     * @see ViewUnBanUser
     */
    public void showUnBanUserView() {
        new ViewUnBanUser(list,Router.getInstance().getRoomByName(nameRoom.getText()),userName.getText());
    }

    public void showRoomInfoView(List<User> users) {
        new ViewRoomInfo(users);
    }

    /**
     * the methods set info from message to view and show notification to user, if room where sets new message is not muted
     * @param message have message from server
     */
    public void showMessage(Message message) {
        if(message.getNameRoomRecipient().equals(nameRoom.getText())) {
            String mess = message.getUserSender().getName() + " << " + message.getText();
            observableListMessages.add(mess);
        }
        if(!message.getUserSender().getName().equals(user.getName()) && !Router.getInstance().getRoomByName(message.getNameRoomRecipient()).isMuted() ) {
            notificator.notifyUser(message.getText(), "New message, from " + message.getUserSender().getName(), TrayIcon.MessageType.INFO);
        }
    }

    /**
     * The methods for mute some room
     * if room is muted view will not show notifications to user from this room
     */
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

    /**
     * The method switches rooms at the form, sets messages list from room
     * method hide or show some components of form depending on user status in this room:
     * if room is not "Big chat", shows button for adding new online user, else hides this button,
     * if user is banned in the room, hides text area and button for sending messages, else shows its,
     * if user is admin of room, shows buttons for prepareBanUser/unBan users in this room, else hides its,
     * if user muted room, sets to muteButton text "UnMute", else sets "Mute"
     * if room was deleted, hide all components for working with this room
     */
    public void switchRoom() {
        try {
            String nameSelectedRoom =  getNameOfSelectedRoom();
            if (nameSelectedRoom != null) {
                nameRoom.setText(nameSelectedRoom);

                roomInfo.setVisible(true);

                setMessageList(nameSelectedRoom);
                if (Router.getInstance().getRoomByName(nameRoom.getText()).isMuted()) {
                    muteRoom.setText("UnMute");
                }
                else {
                    muteRoom.setText("Mute");
                }

                if(Router.getInstance().getRoomByName(nameRoom.getText()).isBanned() || Router.getInstance().getRoomByName(nameSelectedRoom).isDeleted()) {
                    messageText.setVisible(false);
                    sendButton.setVisible(false);
                }
                else {
                    messageText.setVisible(true);
                    sendButton.setVisible(true);
                }

                if(Router.getInstance().getRoomByName(nameSelectedRoom).isDeleted()) {
                    muteRoom.setVisible(false);
                    addUser.setVisible(false);
                    deleteRoom.setVisible(false);
                    banUserButton.setVisible(false);
                    unbanUserButton.setVisible(false);
                } else {
                    muteRoom.setVisible(true);
                    addUser.setVisible(true);
                    String roomAdmin = Router.getInstance().getRoomByName(nameRoom.getText()).getAdmin().getName();
                    if (roomAdmin.equals(userName.getText())) {
                        banUserButton.setVisible(true);
                        unbanUserButton.setVisible(true);
                        adminInfo.setText("You is admin");
                        deleteRoom.setVisible(true);
                    } else {
                        banUserButton.setVisible(false);
                        unbanUserButton.setVisible(false);
                        adminInfo.setText("You isn't admin");
                        deleteRoom.setVisible(false);
                    }
                }

                if (nameSelectedRoom.trim().equals("Big chat")) {
                    addUser.setVisible(false);
                    banUserButton.setVisible(false);
                    unbanUserButton.setVisible(false);
                    leaveRoom.setVisible(false);
                }
                else {
                    addUser.setVisible(true);
                    leaveRoom.setVisible(true);
                }

            }
        }catch (NullPointerException e) {
            logger.warn(e);
        }
    }

    /**
     * The method for setting user list
     * @param list list of users
     */
    public void setList(List<User> list) {
        this.list = list;
    }

    /**
     * The method return object of class User
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * The method for setting object of class User
     * @param user User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The method gets selected room name from list
     * @return String room name
     */
    private String getNameOfSelectedRoom() {
        return roomListView.getSelectionModel().getSelectedItem();
    }

    /**
     * The method for setting name of user to form
     * @param userName have string with name of user
     */
    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    /**
     * The method for adding new room to form
     * @param nameRoom
     */
    public void addRoom(String nameRoom) {
        roomObservableList.add(nameRoom);
    }

    /**
     * The method for getting name of room where user is now
     * @return String with name of room
     */
    public String getNameRoom() {
        return nameRoom.getText();
    }

    /**
     * The method for showing message with server error to user form
     * @param serverError string with message
     */
    public void setServerError(String serverError) {
        this.serverError.setText(serverError);
    }

    /**
     * The method for setting list of messages to view
     * @param roomName have string with name of room
     */
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
            logger.info("While setting message list to view ",e);
        }
    }

    /**
     * The method sets focus to room
     * @param roomName name of room
     */
    public void setFocusToRoom(String roomName) {
        for (String room : roomObservableList) {
            if(room.equals(roomName)) {
                roomListView.getSelectionModel().select(room);
            }
        }
    }
}
