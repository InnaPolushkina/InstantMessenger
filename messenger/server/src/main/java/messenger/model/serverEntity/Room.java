package messenger.model.serverEntity;


import java.util.HashSet;
import java.util.Set;

/**
 * @author Danil
 */
public class Room {

    private String roomName;

    private Set<UserConnection> userList = new HashSet<>();
    private UserConnection admin;
    private Set<UserConnection> banList = new HashSet<>();
    private Set<UserConnection> muteList = new HashSet<>();

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public void sendMassage(User from, String text){

    }
    public void addUser(UserConnection userConnection){
        userList.add(userConnection);
    }

    public void removeUser(UserConnection userConnection) {
        userList.remove(userConnection);
        banList.remove(userConnection);
        muteList.remove(userConnection);
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<UserConnection> getUserList() {
        return userList;
    }

    public UserConnection getAdmin() {
        return admin;
    }

    public void setAdmin(UserConnection admin) {
        this.admin = admin;
    }

    public Set<UserConnection> getBanList() {
        return banList;
    }

    public void banUser(UserConnection userConnection) {
        banList.add(userConnection);
    }

    public void unBanUser(UserConnection userConnection) {
        banList.remove(userConnection);
    }
}
