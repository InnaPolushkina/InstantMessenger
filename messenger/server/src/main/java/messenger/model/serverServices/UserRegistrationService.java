package messenger.model.serverServices;

import messenger.model.serverEntity.User;

/**
 * The interface for servicing user registration / authorization
 */
public interface UserRegistrationService {
    /**
     * the method register new user
     * @param userData have String with user data for registration in xml
     * @return true if user registered, else return false
     */
    boolean registration(String userData);
    /**
     * the method authorize user
     * @param userData have String with user data for authorizing in xml
     * @return true if user authorized, else return false
     */
    boolean auth(String userData);

    /**
     * The method for getting authorized user
     * @return authorized user
     */
    User getAuthorizedUser();

}
