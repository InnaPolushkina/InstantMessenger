package messenger.model.serverServices;

import messenger.model.serverEntity.User;

/**
 * The interface for servicing user checkRegisteringUserInfo / authorization
 */
public interface UserRegistrationService {
    /**
     * The method parse and check info from user for registering
     * checks in server: if server have user with such name, user can't be registering
     * @param userData have String with user data for registering new user
     * @return true if user can be registered, else return false
     */
    boolean checkRegisteringUserInfo(String userData);

    /**
     * The method parses and check info from user for authorizing
     * checks in server: if user sent correct name and password, user can be authorized, else he can't
     * @param userData have String with user data for authorizing
     * @return true if user can be authorized, else return false
     */
    boolean checkAuthorizingUserInfo(String userData);

    /**
     * The method for getting authorized user
     * @return authorized user
     */
    User getAuthorizedUser();

    /**
     * The method for preparing response on client authorizing/registering
     * @param message message to client with description situation
     * @param status boolean status of response
     * @return ready for sending string with response on client authorizing/registering
     */
    String prepareAuthRegResponse(String message, boolean status);

}
