package messenger.model.service;

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
    void ban(User user);

    /**
     * the method unbans user in room
     * @param user contains object of class User
     */
    void unban(User user);

}
