package messenger.controller;

import messenger.model.User;
import messenger.model.UserConnection;
import messenger.model.UserRegistrationService;
import messenger.model.UserRegistrationServiceImpl;
import messenger.view.ViewLogs;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

/**
 * @author Danil
 */
public class Handler extends Thread{
    private UserConnection userConnection;
    private User user;
    private static final Logger logger = Logger.getLogger(Handler.class);
    private UserRegistrationServiceImpl userRegistrationService = new UserRegistrationServiceImpl();
    private Router router;




    public Handler(Socket socket) {
        userConnection = new UserConnection(socket);
       // user = new User();
        //user.setName("Ivan");
    }

    @Override
    public void run() {

        logger.info("Server started . . . ");
        try {
            userConnection.setIn(new BufferedReader(new InputStreamReader((userConnection.getUserSocket().getInputStream()))));
            userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
            while(true) {
                String authorize = userConnection.getIn().readLine();
                boolean isAuth = userRegistrationService.auth(authorize);
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

                while (true) {
                    String input = userConnection.getIn().readLine();
                    Router.getInstense().getXmlGen().sendMassage(input, user.getName());
                    for (UserConnection writer : Router.getInstense().getUserList()) {
                       // File massage = new File( "server/src/main/java/messenger/model/xml/xmlFiles/SendMassage.xml");
                        //Reader msg = new BufferedReader(new FileReader(massage));

                       // writer.getOut().write(((BufferedReader) msg).readLine() + "\n");

                        writer.getOut().write(input + "\n");
                        writer.getOut().flush();
                    }
                }
        }
        catch (IOException e) {
            logger.warn(e);
        }
        finally {
            try {
                userConnection.getUserSocket().close();
            } catch (IOException e) {
                logger.warn("close client socket in sever", e);
            }
        }



        /*System.out.println("OK");
        logger.info("Server started . . .");
        try {
            userConnection.setIn(new BufferedReader(new InputStreamReader(userConnection.getUserSocket().getInputStream())));
            userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
            boolean isValidLogin = true;
            while (true) {
                //user.setName(userConnection.getIn().readLine());
                //userConnection.setName(userConnection.getIn().readLine());
                if (userConnection.getName().length() == 0) {
                    isValidLogin = false;
                }
                synchronized (userConnection.getName()) {
                    if (validateUser(userConnection.getName())) {
                        Router.getInstense().getUserList().add(userConnection);
                        Router.getInstense().getViewLogs().print("New User");
                        break;
                    }else {
                        isValidLogin = false;
                        break;
                    }
                }
            }
            userConnection.getOut().write(String.valueOf(isValidLogin) + "\n");
            userConnection.getOut().flush();
            while (true) {
                String input = userConnection.getIn().readLine();
                if (input == null) {
                    continue;
                }
                Router.getInstense().getXmlGen().sendMassage(input, userConnection.getName());
                for (User writer : Router.getInstense().getUserList()) {
                    File massage = new File( "server/src/main/java/messenger/model/xml/xmlFiles/SendMassage.xml");
                    Reader msg = new BufferedReader(new FileReader(massage));
                    writer.getOut().write(((BufferedReader) msg).readLine() + "\n");
                    writer.getOut().flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                userConnection.getUserSocket().close();
            } catch (IOException e) {
            }
        }*/
    }




    /*private boolean validateUser(String name) {
        Iterator<User> iterator = Router.getInstense().getUserList().iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            if(user.getName().equals(name)){
                return false;
            }
        }
        return true;
    }*/


}
