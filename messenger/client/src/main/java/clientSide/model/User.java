package main.java.clientSide.model;


import ua.sumdu.lab2.group7.Model.Room;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class User {
    private String name;
    private int id;
    private Socket userSocket;
    private boolean isOnline;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader userMes;

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    public BufferedReader getUserMes() {
        return userMes;
    }

    public void setUserMes(BufferedReader userMes) {
        this.userMes = userMes;
    }

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

    public String getUserStatus() {
        if (isOnline) {
            return "Online";
        }
        else {
            return "Offline";
        }
    }
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
