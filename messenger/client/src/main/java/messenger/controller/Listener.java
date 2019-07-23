package messenger.controller;

import javafx.application.Platform;

import messenger.model.entity.*;
import messenger.model.service.MessageService;
import messenger.model.service.RoomService;
import messenger.model.service.UserRegistrationService;
import messenger.model.serviceRealization.MessageServiceImpl;
import messenger.view.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * This class listen entering massages from socket and show its to user
 * @author Inna
 */
public class Listener extends Thread {

    private User user;
    private UserServerConnection userServerConnection;
    private static final Logger logger = Logger.getLogger(Listener.class);
    //private ViewLogin viewLogin;
    private ViewChat viewChat;
    private MessageService messageService;
    private RoomService roomService;
    private UserRegistrationService userRegistrationService;

    /**
     * The public constructor for class Listener
     * @param socket for connect to server
     */
    public Listener(Socket socket) throws IOException {
        user = new User();
        userServerConnection = new UserServerConnection(user,socket);
        this.viewChat = viewChat;
        userServerConnection.setIn();
    }

    /**
     * the method listens server streams and shows messages from server
     */
    @Override
    public void run() {
        while (true) {
            try {
                //userServerConnection.setIn();
                //String action = messageFromServer();
                ServerAction serverAction = messageService.parseServerAction(messageFromServer());
                switch (serverAction) {
                    case SEND_MSG:
                        showMessage();
                        break;
                    case ONLINE_LIST:
                        //set online list to observable list from view
                        String onlineList = messageFromServer();
                        List<User> userList = userRegistrationService.parseUserList(onlineList);
                        Platform.runLater(() -> {
                            viewChat.setList(userList);
                            viewChat.showAddUserView();
                        });
                        break;
                    case ADDED_TO_ROOM:
                        //set new room to room list, observable list from view, notify user by Notificator
                        String s = messageFromServer();
                        Room room = roomService.parseNotifyAddedToRoom(s);
                        Router.getInstance().getRoomList().add(room);
                        Platform.runLater( ()->{
                            Notificator notificator = new Notificator();
                                notificator.notifyUser(room.getRoomName(),"You added to room", TrayIcon.MessageType.INFO);
                                viewChat.addRoom(room.getRoomName());
                        });

                        break;
                }
            } catch (Exception e) {
                logger.error("catch NullPointerException, server don't work ",e);
                Platform.runLater(
                        () -> {
                            viewChat.setServerError("Connection was lost, please reload application");
                        });
                //Router.getInstance().saveHistory();
                //System.out.println(Router.getInstance().getRoomList().toString());
                try {
                    userServerConnection.getOut().close();
                    userServerConnection.getIn().close();
                }
                catch (IOException ex) {
                    logger.info(ex);
                }
                break;
           }
        }
    }

    /**
     * the method gets message from input stream and show message to user
     * @see MessageService parseMessage()
     * @see MessageServiceImpl parseMessage()
     */
    public void showMessage() throws Exception{
        String msg = messageFromServer();
        Message message = messageService.parseMessage(msg);
        Router.getInstance().getRoomByName(message.getNameRoomRecipient()).addMessageToRoom(message);
        Platform.runLater(
                () -> {
                    viewChat.showMessage(message);
                });
    }

    /**
     * the method for getting string with message from user's socket InputStream
     * @return String with message
     * @throws IOException If an I/O error occurs
     */
    public String messageFromServer() throws IOException{
        //userServerConnection.setIn();
        String s = userServerConnection.getIn().readLine();
        return s;
    }

    public void setViewChat(ViewChat viewChat) {
        this.viewChat = viewChat;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }
}