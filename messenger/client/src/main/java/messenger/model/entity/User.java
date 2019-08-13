package messenger.model.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The class for contains info about user and methods for working with it
 * for simple working with view contains field analogues for StringProperty and methods for working with its
 * @see StringProperty
 * @see SimpleStringProperty
 * @author Inna
 */
public class User {
    private String name;
    private boolean isBanned;
    private boolean isOnline;
    private boolean isAdmin;


    private StringProperty nameString;
    private StringProperty isOnlineString;
    private StringProperty isBannedString;
    private StringProperty isAdminString;


    public String getNameString() {
        nameString.set(name);
        return nameString.get();
    }

    public StringProperty nameStringProperty() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString.set(nameString);
    }

    public String getIsOnlineString() {
        if(isOnline) {
            isOnlineString.set("Online");
        }
        else {
            isOnlineString.set(" ");
        }
        return isOnlineString.get();
    }

    public StringProperty isOnlineStringProperty() {
        return isOnlineString;
    }

    public void setIsOnlineString(String isOnlineString) {
        this.isOnlineString.set(isOnlineString);
    }

    public String getIsBannedString() {
        if(isBanned) {
            isBannedString.set("Banned");
        }
        else {
            isBannedString.set(" ");
        }
        return isBannedString.get();
    }

    public StringProperty isBannedStringProperty() {
        return isBannedString;
    }

    public void setIsBannedString(String isBannedString) {
        this.isBannedString.set(isBannedString);
    }

    public String getIsAdminString() {
        if(isAdmin) {
            isAdminString.set("Admin");
        }
        else {
            isAdminString.set(" ");
        }
        return isAdminString.get();
    }

    public StringProperty isAdminStringProperty() {
        return isAdminString;
    }

    public void setIsAdminString(String isAdminString) {
        this.isAdminString.set(isAdminString);
    }

    /**
     * The constructor of this class
     * @param name name of user
     */
    public User(String name) {
        this.name = name;
        this.nameString = new SimpleStringProperty();
        getNameString();
    }

    /**
     * The empty constructor of this class
     */
    public User() {
    }

    /**
     * The getter for name of user
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * The setter for name of user
     * @param name name of user
     */
    public void setName(String name) {
        this.name = name;

        this.nameString = new SimpleStringProperty();
        getNameString();
    }

    /**
     * The setter for banned status of user
     * @param banned banned status of user
     */
    public void setBanned(boolean banned) {
        isBanned = banned;

        this.isBannedString = new SimpleStringProperty();
        getIsBannedString();
    }

    /**
     * The getter for banned status of user
     * @return if user is banned return true, else return false
     */
    public boolean isBanned() {
        return isBanned;
    }

    /**
     * The getter for online user status
     * @return true if user is online, else return false
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * The setter for online user status
     * @param online true if user is online, else false
     */
    public void setOnline(boolean online) {
        isOnline = online;

        this.isOnlineString = new SimpleStringProperty();
        getIsOnlineString();
    }

    /**
     * The getter for user admin status
     * @return true if user is admin in some room, else return false
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * The setter for user admin status
     * @param admin boolean value
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;

        this.isAdminString = new SimpleStringProperty();
        getIsAdminString();
    }

    /**
     * The method for converting object of this class to string
     * @return String with data of user
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", isBanned=" + isBanned +
                '}';
    }


}
