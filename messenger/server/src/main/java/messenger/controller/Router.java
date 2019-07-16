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
import java.util.HashSet;
import java.util.Set;

/**
 * @author Danil
 * fixed by Inna
 */
public class Router {
    private Set<UserConnection> userList = new HashSet<>();
    private Set<Room> roomList;
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
                handler.setUserConnection(new UserConnection(listener.accept()));
                handler.setUserRegistrationService(userRegistrationService);
                handler.setMessageService(messageService);
                handler.setUserKeeper(userKeeper);
                handler.start();
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
    private void createRoom(User[] users){

    }
    private Router(){

    }

   /* public UserRegistrationServiceImpl getUserRegistrationService() {
        return userRegistrationService;
    }*/

    public static Router getInstense() {
        return instense;
    }

    public ViewLogs getViewLogs() {
        return viewLogs;
    }

    public Set<UserConnection> getUserList() {
        return userList;
    }

}
