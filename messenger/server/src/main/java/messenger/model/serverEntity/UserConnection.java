package messenger.model.serverEntity;

import messenger.model.serverEntity.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class UserConnection {
    private User user;
    private Socket userSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader userMes;

    public UserConnection(User user, Socket userSocket) {
        this.user = user;
        this.userSocket = userSocket;
    }

    public UserConnection(Socket userSocket) {
        this.userSocket = userSocket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getUserSocket() {
        return userSocket;
    }

    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
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
}
