package messenger.controller;

import messenger.model.HistoryMessage;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.*;
import messenger.model.serverServicesImlp.*;
import messenger.view.ViewLogs;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
 * The main class of server
 * has method for start work of server
 * @author Danil
 * fixed by Inna
 */
public class Router {
    private static Set<UserConnection> userList = new HashSet<>();
    private Set<Room> roomList = new HashSet<>();
    private static final int PORT = 2020;
    private static ViewLogs viewLogs = new ViewLogs();
    private static Router instense = new Router();
    private static UserRegistrationService userRegistrationService;
    private static UserKeeper userKeeper;
    private static MessageService messageService;
    private static RoomService roomService;
    private static UserService userService;
    private static RoomKeeper roomKeeper;

    private static HistoryMessage historyMessage;

    /**
     * The main class of server
     * wait when user connect to server and start class Handler
     * @param args is program arguments
     * @throws Exception if any exception occur
     */
    public static void main(String[] args) throws Exception{
        viewLogs.print("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        userKeeper = new UserKeeperXml("server/src/main/java/messenger/model/db/users.xml");
        roomKeeper = new RoomKeeperImpl("server/src/main/java/messenger/model/db/rooms.xml");
        userRegistrationService = new UserRegistrationServiceImpl(userKeeper);
        messageService = new MessageServiceImpl();
        historyMessage = new HistoryMessage(messageService);
        roomService = new RoomServiceImpl();
        userService = new UserServiceImpl();
        //try {
            while (true) {
                Handler handler = new Handler();

                handler.setUserConnection(new UserConnection(listener.accept()));
                handler.setUserRegistrationService(userRegistrationService);
                handler.setMessageService(messageService);
                handler.setUserKeeper(userKeeper);
                handler.setRoomService(roomService);
                handler.setHistoryMessage(historyMessage);
                handler.setUserService(userService);
                handler.setRoomKeeper(roomKeeper);

                handler.start();
            }
        //}

        /*finally {
            //listener.close();
            System.out.println("Router closed socket");
        }*/
    }

    /**
     * The private constructor of this class
     */
    private Router(){
        Room bigChat = new Room("Big chat");
        roomList.add(bigChat);
    }


    /**
     * The method add user to room with all users
     * @param userConnection have data with userConnection for adding to Big room
     */
   public void addUserToBigRoom(UserConnection userConnection) {
       for (Room room: roomList) {
           if(room.getRoomName().equals("Big chat")) {
               room.addUser(userConnection);
               System.out.println("authorized user added to big chat");
           }
       }
   }

    /**
     * The getter for room, search room in room set by room name
     * @param roomName for search in set
     * @return object of class Room if room was founded, else return null
     */
   public Room getRoomByName(String roomName) {
       Room result = null;
       for (Room room: roomList) {
           if(room.getRoomName().equals(roomName)) {
               result = room;
           }
       }
       return result;
   }


    /**
     * The getter for single object of this class
     * @return object of Router
     */
    public static Router getInstense() {
        return instense;
    }

    public ViewLogs getViewLogs() {
        return viewLogs;
    }

    /**
     * The getter for userConnection's set
     * @return Set of UserConnection
     */
    public Set<UserConnection> getUserList() {
        return userList;
    }

    /**
     * The getter for room's set
     * @return Set of rooms
     */
    public Set<Room> getRoomList() {
        return roomList;
    }

    public UserConnection getUserConnectionByName(String name) {
        UserConnection result = null;
        for (UserConnection uc: userList) {
            if(uc.getUser().getName().equals(name)) {
                result = uc;
            }
        }

        return result;
    }
}
