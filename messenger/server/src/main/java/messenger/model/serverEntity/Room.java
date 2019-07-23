package messenger.model.serverEntity;


import java.util.HashSet;
import java.util.Set;

/**
 * @author Danil
 */
public class Room {

    private String roomName;

    private Set<UserConnection> userList = new HashSet<>();
    private Set<UserConnection> adminList = new HashSet<>();
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<UserConnection> getUserList() {
        return userList;
    }

}
