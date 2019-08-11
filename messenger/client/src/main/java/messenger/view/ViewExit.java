package messenger.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import messenger.controller.Router;
import messenger.model.HistorySaver;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The class for confirm user exit from app
 */
public class ViewExit {
    private Stage mainStage;
    private Stage simpleStage;
    private static final Logger logger = Logger.getLogger(ViewExit.class);
    @FXML
    private Button exitFromApp;
    @FXML
    private Button cancelExit;

    /**
     * The constructor of class
     * @param mainStage stage from main view
     */
    public ViewExit(Stage mainStage) {
        this.mainStage = mainStage;
        this.simpleStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        try {
            loader.setLocation(Router.class.getResource("/exitView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            simpleStage.setTitle("Confirm exit");
            simpleStage.setScene(scene);
            simpleStage.show();
            logger.info("show confirm exit view");
        }
        catch (IOException e) {
            logger.warn("while showing confirm exit view",e);
        }
    }

    /**
     * The method for initialize confirm view handlers events
     */
    public void initialize() {
        cancelExit.setOnAction(event -> simpleStage.close());
        exitFromApp.setOnAction(event -> {
            HistorySaver historySaver = new HistorySaver();
            historySaver.saveHistory(Router.getInstance().getRoomList(), LocalDateTime.now());
            Router.getInstance().logout();
            ViewLogin viewLogin = new ViewLogin(mainStage);
            simpleStage.close();
        });
    }
}
