package ua.sumdu.lab2.group7.Controller;

import ua.sumdu.lab2.group7.Model.Room;
import ua.sumdu.lab2.group7.Model.User;
import ua.sumdu.lab2.group7.Model.XML.XMLGen;
import ua.sumdu.lab2.group7.View.ViewLogs;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class Rooter {
    private Set<User> userList = new HashSet<>();
    private Set<Room> roomList;
    private static final int PORT = 2020;
    private static ViewLogs viewLogs = new ViewLogs();
    private static Rooter instense = new Rooter();
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
    private Rooter(){

    }

    public static Rooter getInstense() {
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
