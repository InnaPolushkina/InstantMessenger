package messenger.model.entity;


import java.util.Set;

public class Room {
    private String roomName;
    private Set<UserServerConnection> users;
    private Set<UserServerConnection> banList;
    private Set<UserServerConnection> muteList;


    public Room(String roomName) {
        this.roomName = roomName;
    }

    public Room(String roomName, Set<UserServerConnection> users) {
        this.roomName = roomName;
        this.users = users;
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
        muteList.add(userConnection);
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

    public Set<UserServerConnection> getMuteList() {
        return muteList;
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

    public void setMuteList(Set<UserServerConnection> muteList) {
        this.muteList = muteList;
    }
}
