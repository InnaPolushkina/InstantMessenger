package messenger.controller;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;
import messenger.model.serverServicesImlp.MessageServiceImpl;
import messenger.model.serverServicesImlp.UserKeeperXml;
import messenger.model.serverServicesImlp.UserRegistrationServiceImpl;
import messenger.view.ViewLogs;

import java.net.ServerSocket;
import java.net.Socket;
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

    public static void main(String[] args) throws Exception{
        viewLogs.print("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        userKeeper = new UserKeeperXml("server/src/main/java/messenger/model/db/users.xml");
        //userRegistrationService = new UserRegistrationServiceImpl(userKeeper.loadFromFile());
        userRegistrationService = new UserRegistrationServiceImpl(userKeeper);
        messageService = new MessageServiceImpl();
        try {
            while (true) {
                Handler handler = new Handler();
               // UserConnection userConnection = new UserConnection(listener.accept());
                handler.setUserConnection(new UserConnection(listener.accept()));
                //handler.setUserConnection(userConnection);

                handler.setUserRegistrationService(userRegistrationService);
                handler.setMessageService(messageService);
                handler.setUserKeeper(userKeeper);
                handler.start();
               // addUserToBigRoom(handler.getUserConnection());
                //new Handler(listener.accept(),userRegistrationService).start();
            }
        }
        finally {
            listener.close();
        }
    }
   /* private void saveToFile(){
        userRegistrationService.saveUsers("server/src/main/java/messenger/db/users.txt");
    }
    private void loadFromFile(){
        userRegistrationService.getUsers("server/src/main/java/messenger/db/users.txt");
    }*/
    private void createRoom(UserConnection users){

    }
    private Router(){
        Room bigChat = new Room("Big chat");
        //bigChat.addUser(users);
        roomList.add(bigChat);
    }

   /* public UserRegistrationServiceImpl getUserRegistrationService() {
        return userRegistrationService;
    }*/

    /**
     * The method add user to room with all users
     * @param userConnection have data with userConnection for adding to Big room
     */
   public void addUserToBigRoom(UserConnection userConnection) {
       for (Room room: roomList) {
           if(room.getRoomName().equals("Big chat")) {
               room.addUser(userConnection);
               System.out.println("new user added");
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
