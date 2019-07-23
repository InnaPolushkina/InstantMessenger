package messenger.model.serverServices;

import messenger.model.serverEntity.User;

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
