package messenger.model.serviceRealization;

import messenger.controller.Router;
import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;
import messenger.model.service.UserRegistrationService;


import java.io.IOException;

public class UserRegistrationServiceImpl implements UserRegistrationService {

    private Router router;

    public UserRegistrationServiceImpl() {
        this.router = Router.getInstance();
    }

    public UserRegistrationServiceImpl(Router router) {
        this.router = router;
    }

    @Override
    public void registration(String username, String password) throws UserRegistrationException {
        String regMsg = "<reg><nick>" + username + "</nick><password>" + password + "</password></reg>";
        boolean result = false;
        router.sendMessage(regMsg);
        try {
            result = Boolean.parseBoolean(router.getListener().messageFromServer());
        } catch (IOException e) {
            throw new UserRegistrationException(e.getMessage(),e);
        }
        if(!result) {
            throw new UserRegistrationException("Name is not correct, one of users have this nick");
        }
    }

    @Override
    public void auth(String username, String password) throws AuthException {
        String authMsg = "<auth><nick>" + username + "</nick><password>" + password + "</password></auth>";
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
