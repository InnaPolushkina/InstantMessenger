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
/**
 *  The class has methods for working with view when client is unbanning user in room
 */
public class ViewUnBanUser {

    private String adminName;
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(ViewUnBanUser.class);
    private List<User> list;
    Room room;
    Router router;
    private Stage stage;
    @FXML
    private ListView<String> listViewUsers;
    @FXML
    private Button unBanUser;
    @FXML
    private Button cancel;
    @FXML
    private Label errorMsg;

    /**
     * The constructor of this class
     * @param list list with user, who can be unbanned
     * @param room room where user can be unbanned
     * @param adminName name of admin this room
     */
    public ViewUnBanUser(List<User> list, Room room, String adminName) {
        this.stage = new Stage();
        this.list = list;
        this.room = room;
        this.adminName = adminName;
        router = Router.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/unBanUserInRoom.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("UnBan user in this room");
            stage.setScene(scene);
            stage.show();

            logger.info("show scene for adding user to room");

        } catch (IOException e) {
            logger.warn("while showing unbanning user to room scene ", e);
        }
    }

    /**
     * The method for initializing components of form values and handlers for client actions
     */
    @FXML
    public void initialize() {
        setUserToObserver(list);
        listViewUsers.setItems(observableList);
        if (list.size() != 0) {
            errorMsg.setText("");
        }
        else {
            errorMsg.setText("You can't unBan now any user, nobody is online");
        }
        cancel.setOnAction(event -> {
            stage.close();
        });
        unBanUser.setOnAction(event -> {
            User user = getSelectedUser();
            router.banUser(user,room,false);
        });
    }

    /**
     * The method sets list of user to observable list
     * @param list list with users
     */
    private void setUserToObserver(List<User> list) {
        for (User user: list) {
            if(!user.getName().equals(adminName)) {
                observableList.add(user.getName());
            }
        }
    }

    /**
     * The method for getting selected user from list
     * @return selected user, if any user is selected, else return null
     */
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
