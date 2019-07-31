package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;

import java.time.LocalDateTime;

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

    String sendBanNotify(Room room);
    String sendUnBanNotify(Room room);
}
