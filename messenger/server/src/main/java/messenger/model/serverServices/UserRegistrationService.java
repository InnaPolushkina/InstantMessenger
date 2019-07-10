package messenger.model.serverServices;

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
}
