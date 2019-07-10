package messenger.model.entity;

import messenger.model.entity.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * Class UserServerConnection contains info for user's connection to serve with socket
 * @see Socket
 * @see User
 * @author Inna
 */
public class UserServerConnection {
    private User user;
    private Socket userSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader userMes;

    public UserServerConnection(User user, Socket userSocket) {
        this.user = user;
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
