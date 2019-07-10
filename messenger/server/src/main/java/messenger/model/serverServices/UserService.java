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

    /**
     * the method mutes user
     * @param user contains object of class User
     */
    void mute(User user);

    /**
     * the method unmutes
     * @param user contains object of class User
     */
    void unmute(User user);
}
