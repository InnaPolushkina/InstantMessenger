package messenger.model.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The class contains methods for working data with Room
 */
public class Room {
    private String roomName;
    private boolean isMuted;
    private boolean isBanned;
    private boolean isDeleted;
    private User admin;
    private Set<UserServerConnection> users = new HashSet<>();
    private List<Message> messageSet = new LinkedList<>();

    /**
     * The getter for messages from room
     * @return list of messages
     */
    public List<Message> getMessageSet() {
        return messageSet;
    }

    /**
     * The constructor of this class
     * @param roomName name of room
     */
    public Room(String roomName) {
        this.roomName = roomName;
    }

    /**
     * The constructor of this class
     * @param roomName name of room
     * @param users users from room
     */
    public Room(String roomName, Set<UserServerConnection> users) {
        this.roomName = roomName;
        this.users = users;
    }


    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", users=" + users +
                ", messageSet=" + messageSet +
                '}';
    }

    /**
     * The method for adding new user to room
     * @param user user for adding
     */
    public void addNewUser(UserServerConnection user) {
        users.add(user);
    }

    /**
     * The method for removing user from room
     * @param user removing user
     */
    public void removerUser(UserServerConnection user) {
        users.remove(user);
    }

    /**
     * The method for banning user in room
     * @param user banned user
     * @param banStatus ban status
     */
    public void banUser(UserServerConnection user, boolean banStatus) {
        user.getUser().setBanned(banStatus);
    }

    /**
     * The getter for delete status of room
     * @return delete status, if room was deleted return true, else false
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * The setter for delete status of room
     * @param deleted delete status of room
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * The method for adding message to room
     * @param message message for adding
     */
    public void addMessageToRoom(Message  message) {
        messageSet.add(message);
    }

    /**
     * The getter for name of room
     * @return name of room
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * The getter for data of user in room
     * @return set of users
     */
    public Set<UserServerConnection> getUsers() {
        return users;
    }

    /**
     * The setter for data of user in room
     * @param users set of users
     */
    public void setUsers(Set<UserServerConnection> users) {
        this.users = users;
    }

    /**
     * The getter for muted status of room
     * @return muted status
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * The setter for muted status of room
     * @param muted muted status
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    /**
     * The getter for admin of room
     * @return object of class User
     * @see User
     */
    public User getAdmin() {
        return admin;
    }

    /**
     * The setter for admin of room
     * @param admin data about admin
     */
    public void setAdmin(User admin) {
        this.admin = admin;
    }

    /**
     * The getter for banned status of room
     * @return banned status of room
     */
    public boolean isBanned() {
        return isBanned;
    }

    /**
     * The setter for banned status of room
     * @param banned banned status
     */
    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
