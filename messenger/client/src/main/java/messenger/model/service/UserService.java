package messenger.model.service;

import messenger.model.entity.Room;
import messenger.model.entity.User;

/**
 * Interface UserService contains methods for service users in room
 * @author Inna
 */
public interface UserService {
    /**
     * the method bans user in room
     * @param user contains object of class User
     */
    String ban(User user);

    /**
     * the method unbans user in room
     * @param user contains object of class User
     */
    String unban(User user);

    /**
     * The method parses notification from server about banning user
     * @param msg message from server
     * @return room where user was banned
     */
    Room parseBanNotification(String msg);

    /**
     * The method parses notification from server about unbanning user
     * @param msg message from server
     * @return room where user was unbanned
     */
    Room parseUnBanNotification(String msg);

}
