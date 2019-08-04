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
import messenger.model.entity.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * The class has methods for working with view when client is adding new user to room
 */
public class ViewAddUser {

    private static final Logger logger = Logger.getLogger(ViewAddUser.class);
    private Stage stage;
    private List<User> list;
    private Router router;
    private ObservableList<String> observableListUser = FXCollections.observableArrayList();
    @FXML
    private Button addUser;
    @FXML
    private Button cancelAddUser;
    @FXML
    private Label errorMsg;
    @FXML
    private ListView<String> listViewUsers;

    /**
     * The constructor of this class
     * @param list list with online user for adding to room
     */
    public ViewAddUser(List<User> list) {
        this.stage = new Stage();
        this.list = list;
        router = Router.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/addUserToRoom.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Add user to this room");
            stage.setScene(scene);
            stage.show();

            logger.info("show scene for adding user to room");

        } catch (IOException e) {
            logger.warn("while showing adding user to room scene ", e);
        }

    }

    /**
     * The method for initializing components of form values and handlers for client actions
     */
    @FXML
    public void initialize() {
        setUserToObserver(list);
        listViewUsers.setItems(observableListUser);
        if (list.size() != 0) {
            errorMsg.setText("");
        }
        else {
            errorMsg.setText("You can't add now any user, nobody is online");
        }


        addUser.setOnAction(event -> {
            User user = getSelectedUser();
            if(user != null) {
                router.addUserToRoom(user);
                stage.close();
            }
        });
        cancelAddUser.setOnAction(event -> {
            stage.close();
        });

    }

    /**
     * The method sets list of user to observable list
     * @param list list with users
     */
    private void setUserToObserver(List<User> list) {
        for (User user: list) {
            observableListUser.add(user.getName());
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
