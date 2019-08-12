package messenger.model;

import messenger.controller.Router;
import messenger.model.exceptions.ServerRegistrationException;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomKeeper;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;

import java.io.IOException;

/**
 * The class for registering user at the server
 */
public class Recoder {
    private UserConnection userConnection;
    private UserRegistrationService userRegistrationService;
    private UserKeeper userKeeper;
    private RoomKeeper roomKeeper;


    /**
     * The method for registering new user
     * checks unique user name, if it's true, register new user and add to big chat
     * @param userData have string with about new user
     * @return authorized user
     * @throws ServerRegistrationException if user have any problems with checkRegisteringUserInfo
     */
    public User register(String userData) throws ServerRegistrationException{
        boolean result = userRegistrationService.checkRegisteringUserInfo(userData);
        User user;
        try {

            if (result) {
                Router.getInstense().getUserList().add(userConnection);
                Router.getInstense().getViewLogs().print("User registered and authorized");
                user = userRegistrationService.getAuthorizedUser();
                user.setOnline(true);

                userConnection.setUser(user);
                Router.getInstense().addUserToBigRoom(userConnection);
                userConnection.sendMessage(userRegistrationService.prepareAuthRegResponse("successful",result) + "\n");

                roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());

                return user;
            }
            else {
                /*userConnection.getOut().write(userRegistrationService.prepareAuthRegResponse("Can't register, server have user with such nick",result) + "\n");
                userConnection.getOut().flush();*/
                userConnection.sendMessage(userRegistrationService.prepareAuthRegResponse("Can't register, server have user with such nick",result) + "\n");
                throw new ServerRegistrationException("Can't register, server have user with such nick");
            }
        }
        catch (IOException e) {
            throw new ServerRegistrationException("Can't give response on checkRegisteringUserInfo query",e);
        }
    }

    /**
     * The constructor of this class
     * @param userConnection connection with user
     * @param userRegistrationService object of class that implements interface UserRegistrationService
     * @see UserRegistrationService
     * @param userKeeper object of class that implements interface UserKeeper
     * @see UserKeeper
     */
    public Recoder(UserConnection userConnection, UserRegistrationService userRegistrationService, UserKeeper userKeeper) {
        this.userConnection = userConnection;
        this.userRegistrationService = userRegistrationService;
        this.userKeeper = userKeeper;
    }

    /**
     * The setter for room keeper
     * @param roomKeeper object of class that implements interface RoomKeeper
     * @see RoomKeeper
     */
    public void setRoomKeeper(RoomKeeper roomKeeper) {
        this.roomKeeper = roomKeeper;
    }
}
