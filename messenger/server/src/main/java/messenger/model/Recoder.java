package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.ServerRegistrationException;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.UserRegistrationService;

import java.io.IOException;

public class Recoder {
    private UserConnection userConnection;
    private UserRegistrationService userRegistrationService;
    //private Router router;

    public User register(String userData) throws ServerRegistrationException{
        boolean result = userRegistrationService.registration(userData);
        User user = null;
        try {
            userConnection.getOut().write(String.valueOf(result) + "\n");
            userConnection.getOut().flush();

            if (result) {
                /*router = Router.getInstense();
                router.getViewLogs().print("User authorized");
                router.getUserList().add(userConnection);*/
                Router.getInstense().getUserList().add(userConnection);
                Router.getInstense().getViewLogs().print("User registered and authorized");
                user = userRegistrationService.getAuthorizedUser();
                return user;
            }
            else {
                throw new ServerRegistrationException("Can't register, server have user with such nick");
            }
        }
        catch (IOException e) {
            throw new ServerRegistrationException("Can't give response on registration query",e);
        }
    }


    public Recoder() {
        super();
    }

    public Recoder(UserConnection userConnection, UserRegistrationService userRegistrationService) {
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
