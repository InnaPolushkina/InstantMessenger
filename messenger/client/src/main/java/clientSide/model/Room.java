package client.model;

import java.util.Set;
// создала просто для удобства
public class Room {
    private Set<User> usersList;
    private Set<User> adminList;


    public void addUser(User newUser) {
        usersList.add(newUser);
    }

    public boolean removeUser(User removedUser) {
        return usersList.remove(removedUser);
    }

    public Set<User> getUsersList() {
        return usersList;
    }
}
