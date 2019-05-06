package main.java.clientSide.controller;

import main.java.clientSide.model.User;
import main.java.clientSide.view.View;
import ua.sumdu.lab2.group7.Model.Room;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class Rooter {
    private static User user = new User();
    private Set<Room> roomList;

    public static void main(String[] args) {
        View.showLog("Start client  . . . ");
        try {
            Socket socket = new Socket("localhost", 2020);
            Listener listener = new Listener(socket);
            listener.start();

        }
        catch (IOException ex) {
            //logger.error(ex);
            ex.printStackTrace();
        }

    }

    public void createRoom(User[] users) {
        Room room = new Room();
        for(int i = 0; i <users.length; i++ ) {
           // room.addUser(users[i]);
        }
        roomList.add(room);
    }

    public void leaveRoom(Room room) {
        for (Room r: roomList) {
            if(r.equals(room)) {
                //r.removeUser(user);
            }
        }
    }

    public void login() {

    }

    public void register() {

    }

    public void sendMessage() {

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
}
