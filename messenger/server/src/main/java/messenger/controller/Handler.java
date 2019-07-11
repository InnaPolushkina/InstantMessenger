package messenger.controller;

import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Class Handler using for processing from client
 * @author Danil
 * fixed by Inna
 */
public class Handler extends Thread{
    private UserConnection userConnection;
    private User user;
    private static final Logger logger = Logger.getLogger(Handler.class);
   // private UserRegistrationServiceImpl userRegistrationService = new UserRegistrationServiceImpl();
    private UserRegistrationService userRegistrationService;
    private Router router;
    private UserKeeper userKeeper;
    //private MessageService messageService = new MessageServiceImpl();


    /**
     * The public constructor of class Handler
     * @param socket for connection user to server
     */
    public Handler(Socket socket, UserRegistrationService userRegistrationService) {
        userConnection = new UserConnection(socket);
        //userRegistrationService.getUsers();
        this.userRegistrationService = userRegistrationService;
    }

    public Handler() {
        super();
    }

    public void setUserConnection(UserConnection userConnection) {
        this.userConnection = userConnection;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void setUserKeeper(UserKeeper userKeeper) {
        this.userKeeper = userKeeper;
    }

    /**
     * the method creates new thread for one user
     * authorizes or registers user, send messages to user
     * after client's disconnecting saveToFile new users on server
     */
    @Override
    public void run() {

        logger.info("Server started . . . ");
        try {
            userConnection.setIn(new BufferedReader(new InputStreamReader((userConnection.getUserSocket().getInputStream()))));
            userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
            while (true) {
                String authorize = userConnection.getIn().readLine();
                try {
                    boolean isAuth = userRegistrationService.auth(authorize);
                    //boolean isReg = userRegistrationService.registration(authorize);
                    userConnection.getOut().write(String.valueOf(isAuth) + "\n");
                    userConnection.getOut().flush();

                    if (isAuth) {
                        router = Router.getInstense();
                        router.getViewLogs().print("User authorized");
                        router.getUserList().add(userConnection);

                        user = userRegistrationService.getAuthorizedUser();
                        break;
                    }
                }
                catch (NullPointerException e) {
                    //String reg = userConnection.getIn().readLine();
                    boolean isReg = userRegistrationService.registration(authorize);
                    userConnection.getOut().write(String.valueOf(isReg) + "\n");
                    userConnection.getOut().flush();

                    if(isReg) {
                        router = Router.getInstense();
                        router.getViewLogs().print("User registered and authorized");
                        router.getUserList().add(userConnection);

                        user = userRegistrationService.getAuthorizedUser();

                       // userKeeper.saveToFile();
                        userRegistrationService.getAuthorizedUser();

                        break;
                    }
                }
            }
            //sendMessage(userRegistrationService.getUserListInXML());
            while (true) {
                String input = userConnection.getIn().readLine();
                sendMessage(input);
            }

        } catch (IOException e) {
            logger.warn("client " + user.getName() + " disconnected ",e);
        } finally {
            try {
                userConnection.getUserSocket().close();
               // userRegistrationService.saveUsers();
            } catch (IOException e) {
                logger.warn("close client socket in sever", e);
            }
        }
    }

    public void sendMessage(String input) throws IOException {
        for (UserConnection writer : Router.getInstense().getUserList()) {
            writer.getOut().write(input + "\n");
            writer.getOut().flush();
        }
    }


}
