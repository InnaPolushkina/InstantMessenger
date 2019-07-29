package messenger.model.serverServices;

import messenger.model.serverEntity.User;

import java.time.LocalDateTime;

public interface UserService {
    /**
     * the method bans user in room
     * @param user contains object of class User
     */
    void ban(User user);

    /**
     * the method unbans user in room
     * @param user contains object of class User
     */
    void unban(User user);

    /**
     * The method for parsing date of last user online
     * @param data string with data from user
     * @return date of last user online
     */
    LocalDateTime parseLastOnline(String data);
}
