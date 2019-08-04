package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;

import java.time.LocalDateTime;

/**
 * The interface for servicing actions with users
 */
public interface UserService {
    /**
     * the method bans user in room
     * @param msg contains String with user data for parseBanUser
     */
    User parseBanUser(String msg);

    /**
     * the method unbans user in room
     * @param msg contains String with user data for unbanning
     */
    User parseUnbanUser(String msg);

    /**
     * The method for parsing date of last user online
     * @param data string with data from user
     * @return date of last user online
     */
    LocalDateTime parseLastOnline(String data);

    /**
     * The method parses for sending notify about banning user in some room
     * @param room room where user was banned
     * @return string with notify about banning user in some room
     */
    String sendBanNotify(Room room);

    /**
     * The method parses for sending notify about unbanning user in some room
     * @param room room where user was unbanned
     * @return string with notify about unbanning user in some room
     */
    String sendUnBanNotify(Room room);
}
