package messenger.controller;

import messenger.model.Room;
import messenger.model.User;
import messenger.model.UserConnection;
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

    public static void main(String[] args) throws Exception{
        viewLogs.print("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        }
        finally {
            listener.close();
        }
    }
   /* private void save(){
        userRegistrationService.saveUsers("server/src/main/java/messenger/db/users.txt");
    }
    private void load(){
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
