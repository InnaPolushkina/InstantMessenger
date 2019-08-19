package messenger.model.serverEntity;

import java.util.Objects;

/**
 * The method contains user data and methods for working with it
 * @author Danil
 */
public class User {
    private String name;
    private String password;
    private boolean isOnline;

    /**
     * The constructor of this class
     * @param name name of user
     * @param password user password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * The constructor of this class
     * @param name name of user
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * The simple constructor of this class
     */
    public User() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    /**
     * The setter for online user status
     * @param online online status
     */
    public void setOnline(boolean online) {
        isOnline = online;
    }

    /**
     * The getter for name of user
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * The setter for user name
     * @param name name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getter for online user status
     * @return online status
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * The getter for user password
     * @return user password
     */
    public String getPassword() {
        return password;
    }
}
