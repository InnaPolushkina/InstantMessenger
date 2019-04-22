package client.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class User {
    private String name;
    private int id;
    private Socket userSocket;
    private boolean isOnline;

    private Map<Room, ArrayList<Message>> chatText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getUserSocket() {
        return userSocket;
    }

    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }

    /*public String getUserStatus() {

    }*/
    public boolean isOnline() {
        return isOnline;
    }

    public Map<Room, ArrayList<Message>> getChatText() {
        return chatText;
    }

    public void setChatText(Map<Room, ArrayList<Message>> chatText) {
        this.chatText = chatText;
    }
}
