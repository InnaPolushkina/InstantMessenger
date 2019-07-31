package messenger.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import messenger.controller.Router;
import messenger.model.entity.Room;
import messenger.model.entity.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class ViewBanUser {

    private Stage stage;
    private static final Logger logger = Logger.getLogger(ViewAddUser.class);
    private List<User> list;
    private ObservableList<String> observableListUser = FXCollections.observableArrayList();
    private Router router;
    private Room room;
    @FXML
    private ListView<String> listViewUsers;
    @FXML
    private Button banUser;
    @FXML
    private Button cancel;
    @FXML
    private Label errorMsg;

    public ViewBanUser(List<User> list, Room room) {
        this.stage = new Stage();
        this.list = list;
        this.room = room;
        router = Router.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/banUserInRoom.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Ban user in this room");
            stage.setScene(scene);
            stage.show();

            logger.info("show scene for adding user to room");

        } catch (IOException e) {
            logger.warn("while showing adding user to room scene ", e);
        }
    }

    @FXML
    public void initialize() {
        setUserToObserver(list);
        listViewUsers.setItems(observableListUser);
        if (list.size() != 0) {
            errorMsg.setText("");
        }
        else {
            errorMsg.setText("You can't parseBanUser now any user, nobody is online");
        }
        cancel.setOnAction(event -> {
            stage.close();
        });
        banUser.setOnAction(event -> {
            User user = getSelectedUser();
            router.banUser(user,room,true);
        });
    }

    private void setUserToObserver(List<User> list) {
        for (User user: list) {
            observableListUser.add(user.getName());
        }
    }

    private User getSelectedUser() {
        String userName = listViewUsers.getSelectionModel().getSelectedItem();
        User userSelected = null;
        for (User user: list) {
            if(user.getName().equals(userName)) {
                userSelected = user;
            }
        }
        return userSelected;
    }

}
