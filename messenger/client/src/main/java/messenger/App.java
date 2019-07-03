package messenger;

import javafx.application.Application;
import javafx.stage.Stage;
import messenger.controller.Router;

/**
 * Class App contain start point of Application
 * @see Application
 * @author Inna
 */

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Router router = Router.getInstance();
    }

    public static void main(String [] args) {
        launch(args);
    }

}
