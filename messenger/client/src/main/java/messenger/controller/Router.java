package messenger.controller;

import javafx.stage.Stage;
import messenger.model.entity.Room;
import messenger.model.entity.UserServerConnection;
import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;
import messenger.model.serverEntity.UserConnection;
import messenger.model.service.RoomService;
import messenger.model.service.UserRegistrationService;
import messenger.model.serviceRealization.RoomServiceImlp;
import messenger.model.serviceRealization.UserRegistrationServiceImpl;
import messenger.model.entity.Message;
import messenger.model.service.MessageService;
import messenger.model.serviceRealization.MessageServiceImpl;
import messenger.model.entity.User;
import messenger.view.ViewChat;
import messenger.view.ViewLogin;

import messenger.view.ViewRegister;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
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
    //private User user;
    private Set<Room> roomList = new HashSet<>();
    private Listener listener;
    private Socket socket;
    private UserServerConnection userConnection;
    private static final Logger logger = Logger.getLogger(Router.class);
   // private ViewLogin mainView ;
   private Stage stage ;
   private ViewLogin viewLogin;
   private ViewChat viewChat;
   private ViewRegister viewRegister;
   private static final Router instance = new Router();
   private UserRegistrationService userRegistrationService;
   private MessageService messageService;
   private RoomService roomService;

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
        stage = new Stage();
        viewLogin = new ViewLogin(stage);
        userRegistrationService = new UserRegistrationServiceImpl(this);
        roomService = new RoomServiceImlp();
        try {
            socket = new Socket("localhost", 2020);
            userConnection = new UserServerConnection(new User(),socket);
            listener = new Listener(socket);
            //create object of interface MessageService
            messageService = new MessageServiceImpl();
            listener.setMessageService(messageService);
            logger.info("start client ");
            //actions();
        }
        catch (IOException ex) {
            logger.error(ex);
            viewLogin.setErrorUserMessage("Can't connect to server");
        }

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
       // return user;
        return userConnection.getUser();
    }

    public UserServerConnection getUserConnection() {
        return userConnection;
    }

    /**
     * the method for authorization user
     * get response from server and return main form of messenger if user was authorized
     * @see UserRegistrationService
     */
    public void login(String name, String pass) {
        try {
            sendAction("AUTH");
            userRegistrationService.auth(name,pass);
            showMainChat(name);
            viewChat.setList(userRegistrationService.parseUserList(listener.messageFromServer()));
            listener.start();

        } catch (AuthException e) {
            logger.warn("User can't authorize",e);
            viewLogin.setErrorUserMessage("Name or password is`t true");
        }
        catch (Exception e) {
            logger.info(e);
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
                sendAction("REGISTER");
                userRegistrationService.registration(name,password);

                //List<User> list = userRegistrationService.parseUserList(listener.messageFromServer());
                showMainChat(name);
                viewChat.setList(userRegistrationService.parseUserList(listener.messageFromServer()));
                listener.start();

            } catch (UserRegistrationException e) {
                logger.warn("when registering new user ",e);
                viewRegister.setErrorMsg(e.getMessage());
            }
            catch (Exception e) {
                logger.info(e);
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
        viewChat = new ViewChat(stage);
        listener.setViewChat(viewChat);
        viewChat.setUserName(name);
    }

    /**
     * the method send string message to server
     * @param msg String message
     */
    public void sendMessage(String msg) {
        try {
            //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedWriter out = userConnection.getOut();
            out.write(messageService.sendMessage(new Message(msg,userConnection.getUser())) + "\n");
            out.flush();
            //out.close();
        } catch (IOException e) {
            logger.info(e);
        }
    }

    public void sendAction(String msg) {
        try {
            BufferedWriter out = userConnection.getOut();
            out.write(messageService.sendAction(msg) + "\n");
            out.flush();
            //out.close();
        }
        catch (IOException e) {
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
    public void createRoom(String roomName) {
        try {
            //sendAction("CREATE_ROOM");

            BufferedWriter out = userConnection.getOut();
            out.write(roomService.createRoom(roomName) + "\n");
            out.flush();
           // out.close();
            Room room = new Room(roomName);
            room.addNewUser(userConnection);
            roomList.add(room);
        }
        catch (IOException e) {
            logger.warn(e);
        }
    }

    public void leaveRoom(Room room) {
        for (Room r: roomList) {
            if(r.equals(room)) {
                //r.removeUser(user);
                /* send to server request for leaving room from this user
                server parse request from user and send response about leaving user from room to users from room
                all clients delete user from room and show it in the view

                 */

            }
        }
    }
}
