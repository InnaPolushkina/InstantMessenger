package messenger.controller;

import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServicesImlp.UserRegistrationServiceImpl;
import messenger.model.serverServices.MessageService;
import messenger.model.serverServicesImlp.MessageServiceImpl;
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
    private UserRegistrationServiceImpl userRegistrationService = new UserRegistrationServiceImpl();
    private Router router;
    private MessageService messageService = new MessageServiceImpl();


    /**
     * The public constructor of class Handler
     * @param socket for connection user to server
     */
    public Handler(Socket socket) {
        userConnection = new UserConnection(socket);
        userRegistrationService.getUsers();
    }


    /**
     * the method creates new thread for one user
     * authorizes or registers user, send messages to user
     * after client's disconnecting save new users on server
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

                        user = userRegistrationService.getUser();
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

                        user = userRegistrationService.getUser();

                        userRegistrationService.saveUsers();
                        userRegistrationService.getUsers();

                        break;
                    }
                }
            }
            sendMessage();

        } catch (IOException e) {
            logger.warn("client " + user.getName() + " disconnected ",e);
        } finally {
            try {
                userConnection.getUserSocket().close();
                userRegistrationService.saveUsers();
            } catch (IOException e) {
                logger.warn("close client socket in sever", e);
            }
        }
    }

    public void sendMessage() throws IOException {
        while (true) {
            String input = userConnection.getIn().readLine();
            //MessageServer messageServer = messageService.parseMessage(input);
            //User recipient = messageServer.getUser();
            for (UserConnection writer : Router.getInstense().getUserList()) {
                    writer.getOut().write(input + "\n");
                    writer.getOut().flush();
            }
        }
    }


}
