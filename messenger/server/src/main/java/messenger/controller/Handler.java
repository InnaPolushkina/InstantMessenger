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

    private boolean running = true;

    /**
     * The public constructor of class Handler
     * @param socket for connection user to server
     */
    public Handler(Socket socket, UserRegistrationService userRegistrationService) throws IOException{
        userConnection = new UserConnection(socket);
        this.userRegistrationService = userRegistrationService;
        userConnection.setIn(new BufferedReader(new InputStreamReader((userConnection.getUserSocket().getInputStream()))));
        userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
    }

    /**
     * The simple constructor of this class
     */
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
        this.userConnection.setIn(new BufferedReader(new InputStreamReader((userConnection.getUserSocket().getInputStream()))));
        this.userConnection.setOut(new BufferedWriter(new OutputStreamWriter(userConnection.getUserSocket().getOutputStream())));
    }

    /**
     * The setter for room service
     * @param roomService object of class that implements interface RoomService
     * @see RoomService
     */
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * The setter for user
     * @param user user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The setter for userRegistrationService
     * @param userRegistrationService object of class that implements interface UserRegistrationService
     * @see UserRegistrationService
     */
    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    /**
     * The setter for message service
     * @param messageService object of class that implement interface MessageService
     * @see MessageService
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * The setter for router
     * @param router router
     */
    public void setRouter(Router router) {
        this.router = router;
    }

    /**
     * The setter for user keeper
     * @param userKeeper object of class that implements interface UserKeeper
     * @see UserKeeper
     */
    public void setUserKeeper(UserKeeper userKeeper) {
        this.userKeeper = userKeeper;
    }

    /**
     * The setter for history messages
     * @param historyMessage object of class HistoryMessages
     * @see HistoryMessage
     */
    public void setHistoryMessage(HistoryMessage historyMessage) {
        this.historyMessage = historyMessage;
    }

    /**
     * The setter for user service
     * @param userService object of class that implements interface UserService
     * @see UserService
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * The method creates new Thread for one user
     *  and handles user actions using other classes :
     * @see Authorizer
     * @see Recoder
     * @see ClientAction
     * @see RoomActivity
     * @see SenderMessage
     * @see HistoryMessage
     */
   @Override
   public void run() {
       try {
           while (running) {
                String clientMsgAction = userConnection.getIn().readLine();
                String clientData = userConnection.getIn().readLine();
                if(clientMsgAction != null && clientData != null) {
                    clientAction = messageService.parseClientAction(clientMsgAction);
                    System.out.println(clientAction);
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
                        case BAN_LIST:
                            roomActivity.sendListUserForBan();
                            break;
                        case UNBAN_LIST:
                            roomActivity.sendListUserForUnBan();
                            break;
                        case BAN:
                            roomActivity.banUser(clientData);
                            break;
                        case UNBAN:
                            roomActivity.unBanUser(clientData);
                            break;
                        case DELETE_ROOM:
                            roomActivity.deleteRoomByAdmin(clientData);
                            System.out.println("room deleted");
                            break;
                        case LOGOUT:
                            //userConnection.getUser().setOnline(false);
                            disconnect();
                            break;
                        case USERS_IN_ROOM:
                            roomActivity.sendListUserFromRoom(clientData);
                            break;
                    }
                }
           }
       }
       catch (IOException e) {
           try {
               logger.warn("client " + user.getName() + " disconnected ", e);
               view.print("client " + user.getName() + " disconnected or connection was lost");
               userConnection.getUser().setOnline(false);
           }
           catch (NullPointerException ex) {
               logger.info("some client disconnected before authorizing/registering ",ex);
           }
       }
       catch (NullPointerException e) {
           logger.warn("connection was lost", e);
           view.print("client " + user.getName() + " disconnected or connection was lost");
           userConnection.getUser().setOnline(false);
       }
      finally {
           try {
               userConnection.getUserSocket().close();
               //running = false;
               System.out.println("Closed user socket in finally block . . .");
           } catch (IOException e) {
               logger.warn("close client socket in sever", e);
               view.print("close client socket");
           }
       }
   }

  public void disconnect() throws IOException{

       //running = false;
       Router.getInstense().getUserConnectionByName(userConnection.getUser().getName()).getUser().setOnline(false);
       //userConnection.getUser().setOnline(false);
       userConnection.getUserSocket().close();
       running = false;
       System.out.println("User disconnected  . . . ");
      // throw new IOException("client disconnected");
   }



}
