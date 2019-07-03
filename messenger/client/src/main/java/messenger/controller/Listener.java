package messenger.controller;

import javafx.application.Platform;
import messenger.model.*;
import messenger.view.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * This class listen entering massages from socket and show its to user
 * @author Inna
 */
public class Listener extends Thread {

    private User user;
    private UserServerConnection userServerConnection;
    private static final Logger logger = Logger.getLogger(Listener.class);
    private ViewLogin viewLogin;
    private ViewChat viewChat;

    /**
     * The public constructor for class Listener
     * @param socket for connect to server
     */
    public Listener(Socket socket,ViewChat viewChat) throws IOException {
        user = new User();
        userServerConnection = new UserServerConnection(user,socket);
        this.viewChat = viewChat;
        userServerConnection.setIn(new BufferedReader(new InputStreamReader(userServerConnection.getUserSocket().getInputStream())));
        //user.setUserSocket(socket);
       // viewLogin = new ViewLogin();
    }

    /**
     * the method listens server streams and shows messages from server
     */
    @Override
    public void run() {
        while (true) {
            try {
                userServerConnection.setIn(new BufferedReader(new InputStreamReader(userServerConnection.getUserSocket().getInputStream())));
                showMessage();
            } catch (IOException ex) {
                //System.out.println(ex);
                logger.error(ex);
           }
        }
    }

    /**
     * the method gets message from input stream and show message to user
     * @see MessageService parseMessage()
     * @see MessageServiceImpl parseMessage()
     */
    public void showMessage() {
        try {
            String msg = messageFromServer();
            MessageService messageService = new MessageServiceImpl();
            Message message = messageService.parseMessage(msg);
            Platform.runLater(
                    () -> {
                        viewChat.showMessage(message);
                    }
            );

        } catch (Exception e) {
            logger.info(e);
        }
    }

    /**
     * the method for getting string with message from user's socket InputStream
     * @return String with message
     * @throws IOException If an I/O error occurs
     */
    public String messageFromServer() throws IOException{
        String s = userServerConnection.getIn().readLine();
        return s;
    }

}