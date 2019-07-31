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

    Room parseBanNotification(String msg);

    Room parseUnBanNotification(String msg);

}
