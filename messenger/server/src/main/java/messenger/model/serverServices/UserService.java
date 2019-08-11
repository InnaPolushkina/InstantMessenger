package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;

import java.time.LocalDateTime;

/**
 * The interface for servicing actions with users
 */
public interface UserService {
    /**
     * The method parses from string with data user for banning object of class User
     * @see User
     * @param msg contains String with user data for banning
     */
    User parseBanUser(String msg);

    /**
     * The method parses from string with data user for unbanning object of class User
     * @see User
     * @param msg contains String with user data for unbanning
     */
    User parseUnBanUser(String msg);

    /**
     * The method for parsing date of last user online
     * @param data string with data from user
     * @return date of last user online
     */
    LocalDateTime parseLastOnline(String data);

    /**
     * The method prepares for sending notify about banning user in some room
     * parses notify to room in format of client
     * @param room room where user was banned
     * @return string with notify about banning user in some room
     */
    String prepareBanNotify(Room room);

    /**
     * The method prepares for sending notify about unbanning user in some room
     * parses notify to room in format of client
     * @param room room where user was unbanned
     * @return string with notify about unbanning user in some room
     */
    String prepareUnBanNotify(Room room);
}
