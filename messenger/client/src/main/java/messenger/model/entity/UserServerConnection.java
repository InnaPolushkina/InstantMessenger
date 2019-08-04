package messenger.model.entity;

import java.io.*;
import java.net.Socket;

/**
 * Class UserServerConnection contains info for user's connection to serve with socket
 * @see Socket
 * @see User
 * @author Inna
 */
public class UserServerConnection implements Serializable{
    private User user;
    private Socket userSocket;
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * The constructor of this class
     * @param user user
     * @param userSocket user socket
     * @throws IOException if I/O exception occurs
     */
    public UserServerConnection(User user, Socket userSocket) throws IOException{
        this.user = user;
        this.userSocket = userSocket;
        setIn();
        setOut();
    }

    /**
     * The getter for user
     * @return object of class User
     * @see User
     */
    public User getUser() {
        return user;
    }

    /**
     * The setter for user
     * @param user object of class User
     * @see User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The getter for input stream
     * @return stream
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * The setter for input stream
     * @throws IOException if I/O exception occurs
     */
    public void setIn() throws IOException{
       this.in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
    }

    /**
     * The getter for output stream
     * @return stream
     */
    public BufferedWriter getOut() {
        return out;
    }

    /**
     * The setter for output stream
     * @throws IOException if I/O exception occurs
     */
    public void setOut() throws IOException{
        this.out = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()));
    }

    /**
     * The method for converting object of this class to string
     * @return string object data
     */
    @Override
    public String toString() {
        return "UserServerConnection{" +
                "user=" + user +
                ", userSocket=" + userSocket +
                '}';
    }

}
