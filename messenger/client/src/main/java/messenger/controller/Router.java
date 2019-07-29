package messenger.controller;

import javafx.stage.Stage;
import messenger.model.HistorySaver;
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
import java.util.ArrayList;
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
    private Set<Room> roomList;
    private Listener listener;
    private Socket socket;
    private UserServerConnection userConnection;
    private static final Logger logger = Logger.getLogger(Router.class);
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
            listener.setRoomService(roomService);
            listener.setUserRegistrationService(userRegistrationService);
            logger.info("start client ");
        }
        catch (IOException ex) {
            logger.error(ex);
            viewLogin.setErrorUserMessage("Can't connect to server");
            viewLogin.getRegisterButton().setVisible(false);
        }


    }

   /* public void loadHistory() {
        MessagesKeeper messagesKeeper = new MessagesKeeper(roomList);
        roomList = messagesKeeper.loadHistory();
        System.out.println("room list " + roomList.toString());
    }

    public void saveHistory() {
        MessagesKeeper messagesKeeper = new MessagesKeeper(roomList);
        messagesKeeper.saveHistory();
    }*/

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
            getHistory();
            listener.start();
            listener.setDaemon(true);

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

                showMainChat(name);

                listener.start();
                listener.setDaemon(true);

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
        viewChat.setUser(getUser());
        viewChat.setUserName(name);
       // getHistory();
    }

    private void getHistory() {
        HistorySaver historySaver = new HistorySaver();
        roomList = historySaver.loadHistory();
        for (Room room: roomList) {
            viewChat.addRoom(room.getRoomName());
        }
        if(roomList != null) {
            try {
                sendAction("HISTORY");
                String roomList = roomService.parseRoomList(getRoomList(), historySaver.getLastOnlineDate());
                sendMessageToServer(roomList);
            }catch (IOException e) {
                logger.warn("while getting history from server",e);
            }
        }
    }

    /**
     * the method send string message to server
     * @param msgText String message
     */
    public void  sendMessage(/*Message msg*/String msgText) {
        try {
            //msg.setUserSender(userConnection.getUser());
            //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //Room room = new Room(viewChat.getNameRoom());
            String roomName = viewChat.getNameRoom();
            Message msg = new Message(msgText,userConnection.getUser(),roomName);
            sendMessageToServer(messageService.sendMessage(msg));
           /* BufferedWriter out = userConnection.getOut();
            out.write(messageService.sendMessage(msg) + "\n");
            //out.write(msg + "\n");
            out.flush();
            //out.close();*/
        } catch (IOException e) {
            logger.info(e);
        }
    }

    public void sendAction(String msg) {
        try {
           /* BufferedWriter out = userConnection.getOut();
            out.write(messageService.sendAction(msg) + "\n");
            out.flush();*/
            //out.close();

            sendMessageToServer(messageService.sendAction(msg));
        }
        catch (IOException e) {
            logger.info(e);
        }
    }

    public void sendSimpleMsg(String msg) {
        try {
            /*BufferedWriter out = userConnection.getOut();
            out.write(msg + "\n");
            out.flush();*/
            //out.close();
            sendMessageToServer(msg);
        }
        catch (IOException e) {
            logger.info(e);
        }
    }

    private void sendMessageToServer(String msg) throws IOException{
        BufferedWriter out = userConnection.getOut();
        out.write(msg + "\n");
        out.flush();

    }

   /* public List<User> getOnlineUser() {
        sendAction("ONLINE_USERS");

        List<User> userList = null;
        try {
            sendMessageToServer("simple string for testing ");
            try {
                listener.sleep(3000);
            }
            catch (InterruptedException e) {
                logger.warn(e);
            }
            userList = roomService.parseOnlineUsers(listener.messageFromServer());
        }catch (IOException e) {
            logger.warn(e);
        }
        return userList;
    }*/

    public Set<Room> getRoomList() {
        return roomList;
    }

    public Room getRoomByName(String roomName) {
        Room result = null;
        for (Room r: roomList) {
            if(r.getRoomName().equals(roomName)) {
                result = r;
            }
        }
        return result;
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



    public void createRoom(String roomName) {
        try {
            sendAction("CREATE_ROOM");

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

    public void addUserToRoom(User user/*, Room room*/) {
        try {
            sendAction("ADD_TO_ROOM");

            BufferedWriter out = userConnection.getOut();
            out.write(roomService.addUserToRoom(user) + "\n");
            out.flush();
        }
        catch (IOException e) {
            logger.warn(e);
        }

    }

    public void switchRoom(String roomName) {
        sendAction("SWITCH_ROOM");
        try {
            BufferedWriter out = userConnection.getOut();
            out.write(roomService.switchRoom(roomName) + "\n");
            out.flush();
        }
        catch (IOException e) {
            logger.warn(e);
        }

    }


    public void leaveRoom(String roomName) {
        sendAction("LEAVE_ROOM");
        sendMessage(" ");
        for (Room r: roomList) {
                if(r.getRoomName().equals(roomName)) {
                    r.removerUser(userConnection);
                    roomList.remove(r);
                    break;
                }
            }
        }

    public void bunUser(User user, Room room, boolean bunStatus) {

    }

    public void muteUser(User user, Room room, int time, boolean muteStatus) {

    }
}
