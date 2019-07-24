package messenger.controller;

import messenger.model.HistoryMessage;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;
import messenger.model.serverServices.RoomService;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;
import messenger.model.serverServicesImlp.MessageServiceImpl;
import messenger.model.serverServicesImlp.RoomServiceImpl;
import messenger.model.serverServicesImlp.UserKeeperXml;
import messenger.model.serverServicesImlp.UserRegistrationServiceImpl;
import messenger.view.ViewLogs;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
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

    private static HistoryMessage historyMessage;


    public static void main(String[] args) throws Exception{
        viewLogs.print("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        userKeeper = new UserKeeperXml("server/src/main/java/messenger/model/db/users.xml");
        //userRegistrationService = new UserRegistrationServiceImpl(userKeeper.loadFromFile());
        userRegistrationService = new UserRegistrationServiceImpl(userKeeper);
        messageService = new MessageServiceImpl();
        historyMessage = new HistoryMessage(messageService);
        roomService = new RoomServiceImpl();
        try {
            while (true) {
                Handler handler = new Handler();

                handler.setUserConnection(new UserConnection(listener.accept()));
                handler.setUserRegistrationService(userRegistrationService);
                handler.setMessageService(messageService);
                handler.setUserKeeper(userKeeper);
                handler.setRoomService(roomService);
                handler.setHistoryMessage(historyMessage);


                handler.start();
            }
        }
        finally {
            listener.close();
        }
    }

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



    public static Router getInstense() {
        return instense;
    }

    public ViewLogs getViewLogs() {
        return viewLogs;
    }

    public Set<UserConnection> getUserList() {
        return userList;
    }

    public Set<Room> getRoomList() {
        return roomList;
    }
}
