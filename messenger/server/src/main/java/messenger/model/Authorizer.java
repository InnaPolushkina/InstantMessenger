package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.ServerAuthorizationException;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.UserRegistrationService;

import java.io.IOException;

public class Authorizer {
    private UserConnection userConnection;
    private UserRegistrationService userRegistrationService;


    public User authorize(String userData) throws ServerAuthorizationException {
        User user = null;
        boolean result = userRegistrationService.auth(userData);
        try {
            userConnection.getOut().write(String.valueOf(result) + "\n");
            userConnection.getOut().flush();

            if (result) {
                Router.getInstense().getUserList().add(userConnection);
                Router.getInstense().getViewLogs().print("User  authorized");
                user = userRegistrationService.getAuthorizedUser();
                return user;
            }
            else {
                throw new ServerAuthorizationException("can't authorized user, no correct name or password");
            }
        }
        catch (IOException e) {
            throw new ServerAuthorizationException("Can't give response on registration query",e);
        }
    }

    public Authorizer() {
        super();
    }

    public Authorizer(UserConnection userConnection, UserRegistrationService userRegistrationService) {
        this.userConnection = userConnection;
        this.userRegistrationService = userRegistrationService;
    }

    public UserConnection getUserConnection() {
        return userConnection;
    }

    public void setUserConnection(UserConnection userConnection) {
        this.userConnection = userConnection;
    }

    public UserRegistrationService getUserRegistrationService() {
        return userRegistrationService;
    }

    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }
}
