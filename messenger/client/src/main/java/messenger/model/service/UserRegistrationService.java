package messenger.model.service;

import messenger.model.entity.User;
import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;

import java.util.List;

/**
 * Interface UserRegistrationService contains methods for authorization/registration users
 * @author Inna
 */
public interface UserRegistrationService {
    /**
     * The method for servicing user registration
     * sends to server data registered user and listen server response
     * if user was not registered at the server throws exception
     * @param username contain String with user's name
     * @param password contain String with user's password
     * @throws UserRegistrationException if fall exception when user is registering
     * @see UserRegistrationException
     */
    void registration(String username, String password) throws UserRegistrationException;

    /**
     * The method for servicing user authorization
     * sends to server data authorized user and listen server response
     * if user was not authorized at the server throws exception
     * @param username contain String with user's name
     * @param password contain String with user's password
     * @throws AuthException if fall exception when user is authorizing
     * @see AuthException
     */
    void auth(String username, String password) throws AuthException;

    /**
     * The method parse list of users from server
     * @param userList string with list of users
     * @return List of User
     */
    List<User> parseUserList(String userList);

}
