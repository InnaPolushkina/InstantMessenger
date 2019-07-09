package messenger.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messenger.model.*;
import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;
import messenger.view.ViewChat;
import messenger.view.ViewLogin;

import messenger.view.ViewRegister;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Set;


/**
 * The main controller class of client side
 * @see Listener
 * @see User
 * @see UserConnection
 * @see UserRegistrationService
 * @author Inna
 */
public class Router {
    private User user;
    private Set<Room> roomList;
    private Listener listener;
    private Socket socket;
    private static final Logger logger = Logger.getLogger(Router.class);
   // private ViewLogin mainView ;
   private Stage stage ;
   private ViewLogin viewLogin;
   private ViewChat viewChat;
   private ViewRegister viewRegister;
   private static final Router instance = new Router();
   private UserRegistrationServiceImpl userRegistrationService;

    /**
     * the public method for getting object of class Router
     * @return single object of this class
     */
   public static Router getInstance() {
       return instance;
   }

    /**
     * the constructor of class Router
     * connects client with server
     */
    private Router() {
        viewLogin = new ViewLogin();
        stage = new Stage();
        user = new User();
        userRegistrationService = new UserRegistrationServiceImpl(this);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(viewLogin);
        try {
            loader.setLocation(Router.class.getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger.info("show login scene ");
        }
        catch (IOException e) {
            logger.warn("while showing login scene ",e);
        }
        try {

            socket = new Socket("localhost", 2020);
            listener = new Listener(socket);
            logger.info("start client ");
            actions();
        }
        catch (IOException ex) {
            logger.error(ex);
            viewLogin.setErrorUserMessage("Can't connect to server");
        }

    }

    /**
     * the method for setting actions to components of fxml login form
     */
    private void actions() {
        viewLogin.getLoginButton().setOnAction(event -> {
            String name = viewLogin.getUserName().getText().trim();
            String password = viewLogin.getUserPassword().getText().trim();
            user.setName(name);
            login(name,password);
        });
        viewLogin.getRegisterButton().setOnAction(event -> {
            viewRegister = new ViewRegister(this);
            FXMLLoader loader = new FXMLLoader();
            loader.setController(viewRegister);

            try {
                loader.setLocation(Router.class.getResource("/register.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException e) {
                logger.warn("while opening form for registration",e);
            }
        });
    }

    /**
     * the method gets Listener of messages from server
     * @return Listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * the method gets user from Router
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * the method for authorization user
     * get response from server and return main form of messenger if user was authorized
     * @see UserRegistrationService
     */
    public void login(String name, String pass) {
        try {
            userRegistrationService.auth(name,pass);
            showMainChat(name);
            listener.start();

        } catch (AuthException e) {
            logger.warn("User can't authorize",e);
            viewLogin.setErrorUserMessage("Name or password is`t true");
        }
    }

    /**
     * the method for registration user
     * @param name of user
     * @param password of user
     */
    public void register(String name, String password) {
        if(password.length()>=4) {
            try {
                userRegistrationService.registration(name,password);
                showMainChat(name);
                listener.start();

            } catch (UserRegistrationException e) {
                logger.warn("when registering new user ",e);
                viewRegister.setErrorMsg(e.getMessage());
            }
        }
        else {
            viewRegister.setErrorMsg("password can't be less 4 symbols ");
        }
    }

    /**
     * the method show main form of messenger
     * @param name have String with name of user
     */
    private void showMainChat(String name) {
        viewChat = new ViewChat(this);
        listener.setViewChat(viewChat);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(viewChat);

        try {
            loader.setLocation(Router.class.getResource("/mainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            viewChat.setUserName(name);
            logger.info("show main scene ");

        } catch (IOException e) {
            logger.warn("while showing main scene " + e);
        }

    }

    /**
     * the method send string message to server
     * @param msg String message
     */
    public void sendMessage(String msg) {
        try {
            MessageService messageService = new MessageServiceImpl();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(messageService.sendMessage(new Message(msg,user)) + "\n");
            out.flush();
        } catch (IOException e) {
            logger.info(e);
        }
    }

    public Set<User> getUserList(Room room) {
        Set<User> res = null;
        for (Room r: roomList) {
            if(r.equals(room)) {
                //res = r.getUsersList();
            }
        }
        return res;
    }

    public void bunUser(User user, Room room, boolean bunStatus) {

    }

    public void muteUser(User user, Room room, int time, boolean muteStatus) {

    }
    public void createRoom(User[] users) {
        Room room = new Room();
        for(int i = 0; i <users.length; i++ ) {
            // room.addUser(users[i]);
        }
        roomList.add(room);
    }

    public void leaveRoom(Room room) {
        for (Room r: roomList) {
            if(r.equals(room)) {
                //r.removeUser(user);
            }
        }
    }
}
