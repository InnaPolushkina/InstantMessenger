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
import messenger.model.service.UserService;
import messenger.model.serviceRealization.RoomServiceImlp;
import messenger.model.serviceRealization.UserRegistrationServiceImpl;
import messenger.model.entity.Message;
import messenger.model.service.MessageService;
import messenger.model.serviceRealization.MessageServiceImpl;
import messenger.model.entity.User;
import messenger.model.serviceRealization.UserServiceImpl;
import messenger.view.ViewChat;
import messenger.view.ViewLogin;

import messenger.view.ViewRegister;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
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
    private static final Router instance = new Router();
    private UserRegistrationService userRegistrationService;
    private MessageService messageService;
    private RoomService roomService;
    private UserService userService;

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
        userService = new UserServiceImpl();
        messageService = new MessageServiceImpl();
        connectToServer();

    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 2020);
            userConnection = new UserServerConnection(new User(),socket);
            listener = new Listener(socket);
            listener.setMessageService(messageService);
            listener.setRoomService(roomService);
            listener.setUserRegistrationService(userRegistrationService);
            listener.setUserService(userService);
            logger.info("start client ");
        }
        catch (IOException ex) {
            logger.error(ex);
            viewLogin.setErrorUserMessage("Can't connect to server");
            viewLogin.getRegisterButton().setVisible(false);
        }
    }

    public void disconnect() {
        listener.stopThread();
        try {
            socket.close();
        }
        catch (IOException e) {
            logger.info(e);
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
        return userConnection.getUser();
    }

    /**
     * The getter for UserConnection
     * @return object of Class UserServerConnection
     * @see UserServerConnection
     */
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
            viewLogin.setErrorUserMessage(e.getMessage());
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
    public void register(String name, String password) throws UserRegistrationException {
        try {
            sendAction("REGISTER");
            userRegistrationService.registration(name,password);
            roomList = new HashSet<>();
            Room bigChat = new Room("Big chat");
            bigChat.setAdmin(new User("Server"));
            roomList.add(bigChat);
            showMainChat(name);
            viewChat.addRoom("Big chat");

            listener.start();
            listener.setDaemon(true);

        } catch (UserRegistrationException e) {
            logger.warn("when registering new user ",e);
            throw e;
        }
        catch (Exception e) {
            logger.info(e);
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
    }

    /**
     * The method loads history of all room where is user
     * saved history loads to list of room,
     * sends to server request for getting history of rooms when user was online
     */
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
            String roomName = viewChat.getNameRoom();
            Message msg = new Message(msgText,userConnection.getUser(),roomName);
            sendMessageToServer(messageService.sendMessage(msg));
        } catch (IOException e) {
            logger.info(e);
        }
    }

    /**
     * The method sends to server service info about client actions
     * @param msg message with client action
     */
    public void sendAction(String msg) {
        try {
            sendMessageToServer(messageService.sendAction(msg));
        }
        catch (IOException e) {
            logger.info(e);
        }
    }

    /**
     * The method send simple message to server
     * @param msg message to server
     */
    public void sendSimpleMsg(String msg) {
        try {
            sendMessageToServer(msg);
        }
        catch (IOException e) {
            logger.info(e);
        }
    }

    /**
     * The method sends message to server
     * @param msg message
     * @throws IOException if I/O occur
     */
    private void sendMessageToServer(String msg) throws IOException{
        BufferedWriter out = userConnection.getOut();
        out.write(msg + "\n");
        out.flush();

    }

    /**
     * The getter for list of all rooms
     * @return set of rooms
     */
    public Set<Room> getRoomList() {
        return roomList;
    }

    /**
     * The method searches room in room list by room name
     * @param roomName name of searched room
     * @return object of class Room, if room was found, else return null
     */
    public Room getRoomByName(String roomName) {
        Room result = null;
        for (Room r: roomList) {
            if(r.getRoomName().equals(roomName)) {
                result = r;
            }
        }
        return result;
    }

    /**
     * The method sends to server request about creating new room
     * @param roomName name of creating room
     */
    public void createRoom(String roomName) {
        try {
            sendAction("CREATE_ROOM");

            BufferedWriter out = userConnection.getOut();
            out.write(roomService.createRoom(roomName) + "\n");
            out.flush();
            Room room = new Room(roomName);
            room.addNewUser(userConnection);
            room.setAdmin(userConnection.getUser());
            roomList.add(room);
        }
        catch (IOException e) {
            logger.warn(e);
        }
    }

    /**
     * The method sends to server info for adding new user to room
     * @param user user for adding
     */
    public void addUserToRoom(User user) {
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

    public void getUserFromRoom(String roomName) {
        sendAction("USERS_IN_ROOM");
        try {
            BufferedWriter out = userConnection.getOut();
            out.write(roomService.prepareForSendRoom(roomName) + "\n");
            out.flush();
        }
        catch (IOException e) {
            logger.warn(e);
        }
    }

    /**
     * The method sends to server info about user switching room
     * @param roomName name of switched room
     */
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

    /**
     * The method sends to server info about leaving room
     * @param roomName name of leaving room
     */
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

    /**
     * The method sends to server request for banning/unbanning user
     * @param user user for banning/unbanning
     * @param room data about room
     * @param banStatus ban status
     */
    public void banUser(User user, Room room, boolean banStatus) {
        if (banStatus) {
            sendAction("BAN");
            try {
                sendMessageToServer(userService.ban(user));
            }
            catch (IOException e) {
                logger.warn("while banning user in room",e);
            }
        }
        else {
            sendAction("UNBAN");
            try {
                sendMessageToServer(userService.unban(user));
            }
            catch (IOException e) {
                logger.warn("while unbanning user in room",e);
            }
        }

    }

    /**
     * The method sends request to server about deleting room
     * @param roomName name of room for deleting
     */
    public void deleteRoom(String roomName) {
        sendAction("DELETE_ROOM");
        sendMessage(roomService.deleteRoom(roomName));
        viewChat.setFocusToRoom(roomName);
    }

}
