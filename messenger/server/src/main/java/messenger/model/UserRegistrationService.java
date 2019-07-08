package messenger.model;

public interface UserRegistrationService {

    boolean registration(User user);
    boolean auth(String userData);
}
