package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.ServerAuthorizationException;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;
import org.apache.log4j.Logger;

import java.io.*;

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
     * <p>firstly checks user info at the server, if user send correct name and password go to the next step</p>
     * <p>secondly checks online user status, if user is not online now set true to online user status and send response to user about his successfully authorizing,
     * else if user is online now from other device send send response to user about his no successfully authorizing</p>
     * if user authorized checks him presence at "Big chat", and if user is not at "Big chat" (such situation may occur if server was reload) add user to "Big chat"
     * @param userData have string with user data
     * @return authorized user
     * @throws ServerAuthorizationException if user have any problem with checkRegisteringUserInfo
     */
    public User authorize(String userData) throws ServerAuthorizationException {
        User user = null;
        //first check
        boolean result = userRegistrationService.checkAuthorizingUserInfo(userData);
        try {
            if (result) {
                user = userRegistrationService.getAuthorizedUser();
                userConnection.setUser(user);
                UserConnection authConn = Router.getInstense().getUserConnectionByName(user.getName());
                if(authConn == null) {
                    userConnection.getUser().setOnline(true);
                    Router.getInstense().getUserList().add(userConnection);
                    Router.getInstense().addUserToBigRoom(userConnection);
                    Router.getInstense().getUserConnectionByName(userConnection.getUser().getName()).getUser().setOnline(true);
                    userConnection.sendMessage(userRegistrationService.prepareAuthRegResponse("successful",true) + "\n");
                    Router.getInstense().getViewLogs().print("User  authorized");
                }
                //second check
                else if (authConn.getUser().isOnline()) {
                       userConnection.sendMessage(userRegistrationService.prepareAuthRegResponse("you is online now from other device",false) + "\n");
                        throw new ServerAuthorizationException("it is not possible to authorize one user simultaneously from two devices");
                }
                else {
                    Router.getInstense().getUserConnectionByName(user.getName()).setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
                    Router.getInstense().getUserConnectionByName(user.getName()).setIn(new BufferedReader(new InputStreamReader(userConnection.getUserSocket().getInputStream())));
                    Router.getInstense().getUserConnectionByName(userConnection.getUser().getName()).getUser().setOnline(true);
                    userConnection.sendMessage(userRegistrationService.prepareAuthRegResponse("successful",true) + "\n");
                    Router.getInstense().getViewLogs().print("User  authorized");
                }

                System.out.println("Count of users " + Router.getInstense().getUserList().size());

                return user;
            }
            else {
                userConnection.sendMessage(userRegistrationService.prepareAuthRegResponse("no correct name or password",false) + "\n");
                throw new ServerAuthorizationException("can't authorized user, no correct name or password");
            }
        }
        catch (IOException e) {
            throw new ServerAuthorizationException("Can't give response on checkRegisteringUserInfo query",e);
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
