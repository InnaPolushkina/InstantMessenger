package messenger.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messenger.controller.Router;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ViewCreateRoom {
    private static final Logger logger = Logger.getLogger(ViewCreateRoom.class);
    private Stage stage;
    private Router router;
    private ViewChat viewChat;
    @FXML
    private Label errorRoomCreatorMsg;
    @FXML
    private Button createNewRoomButton;
    @FXML
    private Button cancelCreateNewRoomButton;
    @FXML
    private TextField nameNewRoom;

    public ViewCreateRoom(Stage stage, ViewChat viewChat) {
        this.viewChat = viewChat;
        this.stage = stage;
        router = Router.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            loader.setLocation(Router.class.getResource("/createNewRoom.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create new room");
            stage.setScene(scene);
            stage.show();

            logger.info("show scene for creating new room");

        } catch (IOException e) {
            logger.warn("while showing creating new room scene ", e);
        }
    }

    @FXML
    public void initialize() {
        errorRoomCreatorMsg.setText("");
        createNewRoomButton.setOnAction(event -> {
            //router.sendAction("CREATE_ROOM");
            String nameRoom = nameNewRoom.getText().trim();
            if (nameRoom != null && !nameRoom.equals("")) {
                router.createRoom(nameRoom);
                viewChat.setNameRoom(nameRoom);
                viewChat.addRoom(nameRoom);
                stage.close();
            }
            else {
                errorRoomCreatorMsg.setText("enter correct data");
            }

        });
        cancelCreateNewRoomButton.setOnAction(event -> {
            stage.close();
        });


    }

    public Button getCreateNewRoomButton() {
        return createNewRoomButton;
    }

    public Button getCancelCreateNewRoomButton() {
        return cancelCreateNewRoomButton;
    }

    public TextField getNameNewRoom() {
        return nameNewRoom;
    }
}
