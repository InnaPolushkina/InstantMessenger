package messenger.model.serverServices;

import messenger.model.serverEntity.User;

import java.util.List;

/**
 * Interface UserKeeper have methods for keeping user information in server
 */

public interface UserKeeper {
    /**
     * The method for saving user list to file in server
     */
    void saveToFile(/*String fileName,*/ List<User> userList);

    /**
     * The method for loading user list from server file
     * @return List of users
     */
    List<User> loadFromFile(/*String fileName*/);

    /**
     * The method turns to String list of users
     * @param userList have list of users
     * @return String with users data
     */
    String userListToString(List<User> userList);
}
