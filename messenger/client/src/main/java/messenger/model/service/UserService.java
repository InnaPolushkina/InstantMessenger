package messenger.model.service;

import messenger.model.entity.Room;
import messenger.model.entity.User;

/**
 * Interface UserService contains methods for service users in room
 * @author Inna
 */
public interface UserService {
    /**
     * The method prepare user for banning
     * parses user info for banning into string with server format before sending to server
     * @param user contains object of class User
     */
    String prepareBanUser(User user);

    /**
     * The method prepare user for unbanning
     * parses user info for unbanning into string with server format before sending to server
     * @param user contains object of class User
     */
    String prepareUnBanUser(User user);

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
