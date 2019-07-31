package messenger.controller;

import javafx.application.Platform;

import messenger.model.HistorySaver;
import messenger.model.entity.*;
import messenger.model.service.MessageService;
import messenger.model.service.RoomService;
import messenger.model.service.UserRegistrationService;
import messenger.model.service.UserService;
import messenger.model.serviceRealization.MessageServiceImpl;
import messenger.model.serviceRealization.UserServiceImpl;
import messenger.view.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class listen entering massages from socket and show its to user
 * @author Inna
 */
public class Listener extends Thread {

    private User user;
    private UserServerConnection userServerConnection;
    private static final Logger logger = Logger.getLogger(Listener.class);
    private ViewChat viewChat;
    private MessageService messageService;
    private RoomService roomService;
    private UserRegistrationService userRegistrationService;
    private UserService userService = new UserServiceImpl();

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
     * The method listens server streams and handles server messages
     */
    @Override
    public void run() {
        while (true) {
            try {
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
                        room.setAdmin(new User("Anybody"));
                        Router.getInstance().getRoomList().add(room);
                        Platform.runLater( ()->{
                            Notificator notificator = new Notificator();
                                notificator.notifyUser(room.getRoomName(),"You added to room", TrayIcon.MessageType.INFO);
                                viewChat.addRoom(room.getRoomName());
                        });

                        break;
                    case BAN_LIST:
                        //if user is admin in room server send him list with users, whose can will be banned
                        String banList = messageFromServer();
                        List<User> listForBan = roomService.parseListForBanUnBan(banList);
                        System.out.println(listForBan.toString());
                        Platform.runLater(() -> {
                            viewChat.setList(listForBan);
                            viewChat.showBanUserView();
                        });
                        break;
                    case UNBAN_LIST:
                        //if user is admin in room server send him list with users, whose can will be unbanned
                        String unBanList = messageFromServer();
                        List<User> listForUnBan = roomService.parseListForBanUnBan(unBanList);
                        Platform.runLater(() -> {
                            viewChat.setList(listForUnBan);
                           // viewChat.showUnBanUserView();
                        });
                        break;
                    case BAN:
                        //if admin of room banned user, user get notification from server about it
                        String banNotification = messageFromServer();
                        Room banRoom = userService.parseBanNotification(banNotification);
                        Router.getInstance().getRoomByName(banRoom.getRoomName()).banUser(userServerConnection,true);
                        Router.getInstance().getRoomByName(banRoom.getRoomName()).setBanned(true);
                        // parse room where user banned
                        // for rooms where user is banned set visible false for send button and textField
                        break;
                    case UNBAN:

                        break;
                }

            } catch (Exception e) {
                logger.error("catch NullPointerException, server don't work ",e);
                Platform.runLater(
                        () -> {
                            viewChat.setServerError("Connection was lost, please reload application");
                        });
                HistorySaver historySaver = new HistorySaver();
                historySaver.saveHistory(Router.getInstance().getRoomList(), LocalDateTime.now());
                /*try {
                    userServerConnection.getOut().close();
                    userServerConnection.getIn().close();
                }
                catch (IOException ex) {
                    logger.info(ex);
                }*/
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