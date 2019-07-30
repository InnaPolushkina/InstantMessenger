package messenger.controller;

import messenger.model.*;
import messenger.model.exceptions.ServerAuthorizationException;
import messenger.model.exceptions.ServerRegistrationException;
import messenger.model.serverEntity.ClientAction;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.*;
import messenger.view.ViewLogs;
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
    private UserRegistrationService userRegistrationService;
    private Router router;
    private ClientAction clientAction;
    private ViewLogs view = new ViewLogs();
    private MessageService messageService;
    private UserKeeper userKeeper;
    private RoomService roomService;
    private RoomActivity roomActivity;
    private SenderMessage senderMessage;
    private HistoryMessage historyMessage;
    private UserService userService;

    /**
     * The public constructor of class Handler
     * @param socket for connection user to server
     */
    public Handler(Socket socket, UserRegistrationService userRegistrationService) throws IOException{
        userConnection = new UserConnection(socket);
        //userRegistrationService.getUsers();
        this.userRegistrationService = userRegistrationService;
        userConnection.setIn(new BufferedReader(new InputStreamReader((userConnection.getUserSocket().getInputStream()))));
        userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
    }

    public Handler() {
        super();
    }

    /**
     * The method sets user connection to this class and
     *    sets I/O streams to user socket
     * @param userConnection data for connecting
     * @throws IOException
     */
    public void setUserConnection(UserConnection userConnection) throws IOException{
        this.userConnection = userConnection;
        userConnection.setIn(new BufferedReader(new InputStreamReader((userConnection.getUserSocket().getInputStream()))));
        userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void setUserKeeper(UserKeeper userKeeper) {
        this.userKeeper = userKeeper;
    }

    public UserConnection getUserConnection() {
        return userConnection;
    }

    public void setHistoryMessage(HistoryMessage historyMessage) {
        this.historyMessage = historyMessage;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * The method creates new Thread for one user
     *  and handles user actions using other classes
     * @see Authorizer
     * @see Recoder
     * @see ClientAction
     */
   @Override
   public void run() {
       try {
           while (true) {
                String clientMsgAction = userConnection.getIn().readLine();
                String clientData = userConnection.getIn().readLine();
                if(clientMsgAction != null && clientData != null) {
                    clientAction = messageService.parseClientAction(clientMsgAction);
                    switch (clientAction) {
                        case REGISTER:
                            //call methods from class for registration
                            try {
                                Recoder recoder = new Recoder(userConnection, userRegistrationService, userKeeper);
                                user = recoder.register(clientData);
                                roomActivity = new RoomActivity(userConnection, roomService, userKeeper, historyMessage, messageService);
                                senderMessage = new SenderMessage(messageService, userConnection, historyMessage);
                                historyMessage.sendStory(userConnection);
                            } catch (ServerRegistrationException e) {
                                logger.warn(e.getMessage(), e);
                            }
                            break;
                        case AUTH:
                            //call methods from class for authorization
                            try {
                                Authorizer authorizer = new Authorizer(userConnection, userRegistrationService, userKeeper);
                                user = authorizer.authorize(clientData);
                                roomActivity = new RoomActivity(userConnection, roomService, userKeeper, historyMessage, messageService);
                                senderMessage = new SenderMessage(messageService, userConnection, historyMessage);
                                //historyMessage.sendStory(userConnection);
                            } catch (ServerAuthorizationException e) {
                                logger.warn(e.getMessage(), e);
                            }
                            break;
                        case SEND_MSG:
                            //send message
                            senderMessage.sendMessage(clientData);
                            // sendMessage(clientData);
                            break;
                        //There are cases for other client actions . . .
                        case CREATE_ROOM:
                            //RoomActivity roomCreator = new RoomActivity(userConnection,roomService);
                            roomActivity.createRoom(clientData);
                            break;
                        case SWITCH_ROOM:
                            roomActivity.setRoomNow(clientData);
                            break;
                        case ADD_TO_ROOM:
                            roomActivity.addUserToRoom(clientData);
                            break;
                        case ONLINE_USERS:
                            //roomActivity.getOnlineUsers(userKeeper);
                            roomActivity.sendOnlineUserList();
                            break;
                        case LEAVE_ROOM:
                            //leave user from room
                            //roomActivity.setMessageService(messageService);
                            roomActivity.leaveRoom(clientData);
                            //senderMessage.sendMessage(messageService.sendMessage(new MessageServer(userConnection.getUser(),"!!! Leaved room !!!")));
                            break;
                        case HISTORY:
                            roomActivity.setUserService(userService);
                            roomActivity.sendHistoryOfRooms(clientData);
                            break;
                    }
                }
           }
       }
       catch (IOException e) {
           logger.warn("client " + user.getName() + " disconnected ",e);
           view.print("client " + user.getName() + " disconnected or connection was lost");
       }
       catch (NullPointerException e) {
           logger.warn("connection was lost", e);
           view.print("client " + user.getName() + " disconnected or connection was lost");
       }
       finally {
           try {
               userConnection.getUserSocket().close();
           } catch (IOException e) {
               logger.warn("close client socket in sever", e);
               view.print("close client socket");
           }
       }
   }



}
