package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.ServerRegistrationException;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Recoder {
    private UserConnection userConnection;
    private UserRegistrationService userRegistrationService;
    private UserKeeper userKeeper;


    /**
     * The method for registering new user
     * checks unique user name, if it's true, register new user and add to big chat
     * @param userData have string with about new user
     * @return authorized user
     * @throws ServerRegistrationException if user have any problems with registration
     */
    public User register(String userData) throws ServerRegistrationException{
        boolean result = userRegistrationService.registration(userData);
        User user = null;
        try {
            userConnection.getOut().write(String.valueOf(result) + "\n");
            userConnection.getOut().flush();

            if (result) {
                Router.getInstense().getUserList().add(userConnection);
                Router.getInstense().getViewLogs().print("User registered and authorized");
                user = userRegistrationService.getAuthorizedUser();
                user.setOnline(true);
                //userConnection.getOut().write(userRegistrationService.);
               // userConnection.getOut().write(userKeeper.userListToString(userKeeper.loadFromFile()) + "\n");
                //userConnection.getOut().flush();


                userConnection.setUser(user);
                Router.getInstense().addUserToBigRoom(userConnection);

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

    public Recoder(UserConnection userConnection, UserRegistrationService userRegistrationService, UserKeeper userKeeper) {
        this.userConnection = userConnection;
        this.userRegistrationService = userRegistrationService;
        this.userKeeper = userKeeper;
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
