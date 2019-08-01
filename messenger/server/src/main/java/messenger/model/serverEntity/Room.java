package messenger.model.serverEntity;


import java.util.HashSet;
import java.util.Set;

/**
 * The class for containing data about room and methods for working with room
 * @author Danil
 */
public class Room {

    private String roomName;

    private Set<UserConnection> userList = new HashSet<>();
    private String admin;
    private Set<UserConnection> banList = new HashSet<>();

    /**
     * The constructor of this class
     * @param roomName name of room
     */
    public Room(String roomName) {
        this.roomName = roomName;
    }

    /**
     * The method adds user to room
     * @param userConnection user data
     */
    public void addUser(UserConnection userConnection){
        userList.add(userConnection);
    }

    /**
     * The method removes user from room
     * @param userConnection user data
     */
    public void removeUser(UserConnection userConnection) {
        userList.remove(userConnection);
        banList.remove(userConnection);
    }

    /**
     * The method finds UserConnection by user name
     * @param userName string with user name
     * @return if user is found return connection with him, else return null
     * @throws NullPointerException if user not found
     */
    public UserConnection getUserConnectionByName(String userName) throws NullPointerException{
        UserConnection result = null;
        for (UserConnection uc: userList) {
            if(uc.getUser().getName().equals(userName)) {
                result = uc;
            }
        }
        return result;
    }

    /**
     * The getter for room name
     * @return name of room
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * The setter for room name
     * @param roomName name of room
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * The getter for user connection
     * @return connection of user
     */
    public Set<UserConnection> getUserList() {
        return userList;
    }

    /**
     * The getter for admin of room
     * @return admin of room
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * The setter for room admin
     * @param admin admin of room
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * The getter for list with banned users
     * @return set of users connections
     */
    public Set<UserConnection> getBanList() {
        return banList;
    }

    /**
     * The methods for unbanning user
     * @param userConnection user connection
     */
    public void unBanUser(UserConnection userConnection) {
        banList.remove(userConnection);
    }
}
