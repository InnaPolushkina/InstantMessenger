package messenger.model.serverEntity;


import java.util.HashSet;
import java.util.Set;

/**
 * The class for containing data about room and methods for working with room
 */
public class Room {

    private String roomName;
    private String admin;
    private Set<String> userList = new HashSet<>();

    private Set<String> banList = new HashSet<>();

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
   /* public void addUser(UserConnection userConnection){
        userList.add(userConnection);
    }*/
   public void addUser(UserConnection userConnection) {
       userList.add(userConnection.getUser().getName());
   }

    /**
     * The method removes user from room
     * @param userConnection user data
     */
    public void removeUser(UserConnection userConnection) {
        /*userList.remove(userConnection);
        banList.remove(userConnection);*/
        userList.remove(userConnection.getUser().getName());
        userList.remove(userConnection.getUser().getName());
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
  /*  public Set<UserConnection> getUserList() {
        return userList;
    }*/
      public Set<String> getUserList() {
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
    /*public Set<UserConnection> getBanList() {
        return banList;
    }*/
    public Set<String> getBanList() {
        return banList;
    }

    /**
     * The methods for unbanning user
     * @param userConnection user connection
     */
    public void unBanUser(UserConnection userConnection) {
        //banList.remove(userConnection);
        banList.remove(userConnection.getUser().getName());
    }

    /**
     * The method for checking user banned status in room
     * if user is banned in room return true, if user is not banned in room return false
     * @param uc data about user connection
     * @return true if user is banned in room, else return false
     */
    public boolean isUserBanned(UserConnection uc) {
        String userName = uc.getUser().getName();
        for (String name: banList) {
            if(name.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserBanned(String userName) {
        //String userName = uc.getUser().getName();
        for (String name: banList) {
            if(name.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method for checking user presence in room
     * if user is in room return true, else return false
     * @param uc data about user connection
     * @return
     */
    public boolean isUserInRoom(UserConnection uc) {
        String nameUser = uc.getUser().getName();
        for (String name: userList) {
            if(name.equals(nameUser)) {
                return true;
            }
        }
        return false;
    }
}
