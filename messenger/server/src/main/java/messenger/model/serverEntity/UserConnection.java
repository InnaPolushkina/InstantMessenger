package messenger.model.serverEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * The class contains data about user connection and methods for working with it
 */
public class UserConnection {
    private User user;
    private Socket userSocket;
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * The constructor of this class
     * @param user user for connecting
     * @param userSocket user socket for connecting
     */
    public UserConnection(User user, Socket userSocket) {
        this.user = user;
        this.userSocket = userSocket;
    }

    /**
     * The constructor of this class
     * @param userSocket user socket for connecting
     */
    public UserConnection(Socket userSocket) {
        this.userSocket = userSocket;
    }

    /**
     * The getter for user
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * The setter for user
     * @param user object of class user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The getter for user socket
     * @return user socket
     */
    public Socket getUserSocket() {
        return userSocket;
    }

    /**
     * The getter for input stream
     * @return input stream
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * The setter for input stream
     * @param in input stream
     */
    public void setIn(BufferedReader in) {
        this.in = in;
    }

    /**
     * The getter for output stream
     * @return output stream
     */
    public BufferedWriter getOut() {
        return out;
    }

    /**
     * The setter for output stream
     * @param out output stream
     */
    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        UserConnection guest = (UserConnection) obj;
        return this.getUser().getName().equals(guest.getUser().getName()) && userSocket.equals(guest.userSocket);
    }

}
