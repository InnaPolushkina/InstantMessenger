package messenger.controller;

import messenger.model.Room;
import messenger.model.User;
import messenger.model.xml.XMLGen;
import messenger.view.ViewLogs;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class Router {
    private Set<User> userList = new HashSet<>();
    private Set<Room> roomList;
    private static final int PORT = 2020;
    private static ViewLogs viewLogs = new ViewLogs();
    private static Router instense = new Router();
    private XMLGen xmlGen = new XMLGen();

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
    private void save(){

    }
    private void load(){

    }
    private void createRoom(User[] users){

    }
    private Router(){

    }

    public static Router getInstense() {
        return instense;
    }

    public ViewLogs getViewLogs() {
        return viewLogs;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public XMLGen getXmlGen() {
        return xmlGen;
    }
}
