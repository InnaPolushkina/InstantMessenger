package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.AuthException;

import java.io.IOException;

public class UserRegistrationServiceImpl implements UserRegistrationService {

    private Router router;

    public UserRegistrationServiceImpl(Router router) {
        this.router = router;
    }

    @Override
    public void registration(String username, String password) {

    }

    @Override
    public void auth(String username, String password) throws AuthException {
        String authMsg = "<user><nick>" + username + "</nick><password>" + password + "</password></user>";
        boolean result = false;
        router.sendMessage(authMsg);
        try {
            result = Boolean.parseBoolean(router.getListener().messageFromServer());
        } catch (IOException e) {
            throw new AuthException(e);
        }
        if(!result) {
            throw new AuthException("Name or password is not correct");
        }
    }
}
