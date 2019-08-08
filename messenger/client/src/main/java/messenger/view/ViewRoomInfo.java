package messenger.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import messenger.controller.Router;


import messenger.model.entity.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class ViewRoomInfo {

    private static final Logger logger = Logger.getLogger(ViewRoomInfo.class);

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> userName;
    @FXML
    private TableColumn<User, String> userOnlineStatus;
    @FXML
    private TableColumn<User, String> userBannedStatus;
    @FXML
    private TableColumn<User, String> userAdminStatus;
    @FXML
    private Button okButton;
    @FXML
    private Label countUsersInRoom;

    private Stage stage;
    private List<User> userList;
    private ObservableList<User> observableList = FXCollections.observableArrayList();



    public ViewRoomInfo(List<User> list) {
        this.userList = list;
        this.stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/usersInRoom.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Users in room");
            stage.setScene(scene);
            stage.show();

            logger.info("show scene users in room");

        } catch (IOException e) {
            logger.warn("while showing users in room ", e);
        }
    }

    public void initialize() {
        userName.setCellValueFactory(new PropertyValueFactory<User,String>("nameString"));
        userOnlineStatus.setCellValueFactory(new PropertyValueFactory<User,String>("isOnlineString"));
        userBannedStatus.setCellValueFactory(new PropertyValueFactory<User,String>("isBannedString"));
        userAdminStatus.setCellValueFactory(new PropertyValueFactory<User,String>("isAdminString"));

        observableList.setAll(userList);
        usersTable.setItems(observableList);

        okButton.setOnAction(event -> {
            stage.close();
        });
        countUsersInRoom.setText(String.valueOf(userList.size()) + " users . ");
    }

}
