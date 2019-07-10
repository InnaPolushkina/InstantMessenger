package messenger.model.serverEntity;

import messenger.model.serverEntity.User;

import java.util.Set;

/**
 * @author Danil
 */
public class Room {
    private Set<User> userList;
    private Set<User> adminList;
    private Set<User> banList;
    private Set<User> muteList;

    public void sendMassage(User from, String text){

    }
    public void addUser(){

    }
}
