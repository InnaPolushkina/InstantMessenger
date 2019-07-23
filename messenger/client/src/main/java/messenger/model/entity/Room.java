package messenger.model.entity;


import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Room implements Serializable {
    private String roomName;
    private boolean isMuted;
    private Set<UserServerConnection> users = new HashSet<>();
    private Set<UserServerConnection> banList = new HashSet<>();
   // private Set<UserServerConnection> muteList = new HashSet<>();
    private List<Message> messageSet = new LinkedList<>();

    public List<Message> getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(List<Message> messageSet) {
        this.messageSet = messageSet;
    }

    public Room(String roomName) {
        this.roomName = roomName;
    }

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

    public void addNewUser(UserServerConnection user) {
        users.add(user);
    }

    public void removerUser(UserServerConnection user) {
        users.remove(user);
    }

    public void banUser(UserServerConnection user, boolean banStatus) {
        user.getUser().setBanned(banStatus);
        banList.add(user);
    }

    public void muteUser(UserServerConnection userConnection, boolean muteStatus) {
        userConnection.getUser().setMuted(muteStatus);
    }

    public void addMessageToRoom(Message  message) {
        messageSet.add(message);
    }

    public String getRoomName() {
        return roomName;
    }

    public Set<UserServerConnection> getUsers() {
        return users;
    }

    public Set<UserServerConnection> getBanList() {
        return banList;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setUsers(Set<UserServerConnection> users) {
        this.users = users;
    }

    public void setBanList(Set<UserServerConnection> banList) {
        this.banList = banList;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}
