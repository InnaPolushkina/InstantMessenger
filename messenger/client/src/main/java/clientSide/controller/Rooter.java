package client.controller;

import client.model.Room;
import client.model.User;

import java.util.Set;

public class Rooter {
    private User user;
    private Set<Room> roomList;

    public static void main(String[] args) {

    }

    public void createRoom(User[] users) {
        Room room = new Room();
        for(int i = 0; i <users.length; i++ ) {
            room.addUser(users[i]);
        }
        roomList.add(room);
    }

    public void leaveRoom(Room room) {
        for (Room r: roomList) {
            if(r.equals(room)) {
                r.removeUser(user);
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
                res = r.getUsersList();
            }
        }
        return res;
    }

    public void bunUser(User user, Room room, boolean bunStatus) {

    }

    public void muteUser(User user, Room room, int time, boolean muteStatus) {

    }
}
