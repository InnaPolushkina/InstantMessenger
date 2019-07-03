package messenger.model;

/**
 * Interface UserRegistrationService contains methods for authorization and registration user
 * @author Inna
 */
public interface UserRegistrationService {
    /**
     * the method for registration user
     * @param username contain String with user's name
     * @param password contain String with user's password
     */
    void registration(String username, String password) /*throws UserRegistrationException*/;

    /**
     * the method for authorization user
     * @param username contain String with user's name
     * @param password contain String with user's password
     */
    void auth(String username, String password) /*throws AuthException*/;

}
