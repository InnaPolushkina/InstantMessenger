package messenger.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * @author Danil
 */
public class User {
    private String name;
    private  int id;
    private Socket userSocket;
    private boolean isOnline;
    private BufferedReader in;
    private BufferedWriter out;

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

    public boolean isOnline() {
        return isOnline;
    }

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
}
