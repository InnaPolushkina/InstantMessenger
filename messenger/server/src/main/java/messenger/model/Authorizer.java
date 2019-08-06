package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.ServerAuthorizationException;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class for authorizing user at the server
 */
public class Authorizer {
    private UserConnection userConnection;
    private UserRegistrationService userRegistrationService;
    private UserKeeper userKeeper;
    private static final Logger logger = Logger.getLogger(Authorizer.class);

    /**
     * The method for authorizing user
     * if user authorized add to big room
     * @param userData have string with user data
     * @return authorized user
     * @throws ServerAuthorizationException if user have any problem with registration
     */
    public User authorize(String userData) throws ServerAuthorizationException {
        User user = null;
        boolean result = userRegistrationService.auth(userData);
        try {
            userConnection.getOut().write(String.valueOf(result) + "\n");
            userConnection.getOut().flush();

            if (result) {
                //Router.getInstense().getUserList().add(userConnection);

                Router.getInstense().getViewLogs().print("User  authorized");
                user = userRegistrationService.getAuthorizedUser();
                user.setOnline(true);
                userConnection.setUser(user);
                if(Router.getInstense().getUserByName(userConnection.getUser().getName()) == null) {
                    Router.getInstense().getUserList().add(userConnection);
                    Router.getInstense().addUserToBigRoom(userConnection);
                }
                else {
                    Router.getInstense().getUserByName(user.getName()).setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
                    Router.getInstense().getUserByName(user.getName()).setIn(new BufferedReader(new InputStreamReader(userConnection.getUserSocket().getInputStream())));
                    Router.getInstense().getUserByName(userConnection.getUser().getName()).getUser().setOnline(true);
                    /*UserConnection remove = Router.getInstense().getUserByName(user.getName());
                    Router.getInstense().getUserList().remove(remove);
                    Router.getInstense().getUserList().add(userConnection);
                    Router.getInstense().addUserToBigRoom(userConnection);*/
                }
                System.out.println("Count of users " + Router.getInstense().getUserList().size());


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

    /**
     * The constructor of this class
     * @param userConnection user connection for authorizing
     * @param userRegistrationService object of class that implements interface UserRegistrationService
     * @see UserRegistrationService
     * @param userKeeper object of class that implements interface UserKeeper
     * @see UserKeeper
     */
    public Authorizer(UserConnection userConnection, UserRegistrationService userRegistrationService, UserKeeper userKeeper) {
        this.userConnection = userConnection;
        this.userRegistrationService = userRegistrationService;
        this.userKeeper = userKeeper;
    }

}
