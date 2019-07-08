package messenger.model;

import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;

/**
 * Interface UserRegistrationService contains methods for authorization and registration user
 * @author Inna
 */
public interface UserRegistrationService {
    /**
     * the method for registration user
     * @param username contain String with user's name
     * @param password contain String with user's password
     * @throws UserRegistrationException if fall exception when user is registering
     * @see UserRegistrationException
     */
    void registration(String username, String password) throws UserRegistrationException;

    /**
     * the method for authorization user
     * @param username contain String with user's name
     * @param password contain String with user's password
     * @throws AuthException if fall exception when user is authorizing
     * @see AuthException
     */
    void auth(String username, String password) throws AuthException;

}
