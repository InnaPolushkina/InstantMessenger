package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;
import messenger.model.serverServices.RoomService;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains methods for handling activities in room
 */
public class RoomActivity {
    private UserConnection userConnection;
    private RoomService roomService;
    private static final Logger logger = Logger.getLogger(RoomActivity.class);
    private UserKeeper userKeeper;
    private HistoryMessage historyMessage;
    private MessageService messageService;
    private UserService userService;
    /**
     * The field roomNow contain info about room where user is now
     */
    private Room roomNow;

    public RoomActivity(UserConnection userConnection, RoomService roomService, UserKeeper userKeeper, HistoryMessage historyMessage, MessageService messageService) {
        this.userConnection = userConnection;
        this.roomService = roomService;
        this.userKeeper = userKeeper;
        this.historyMessage = historyMessage;
        this.messageService = messageService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * The method sets new value to roomNow
     * @param roomNowData have string with data from client about changing room
     * method calls method parseRoomForSwitch() from RoomService interface for parsing string from client to object of class Room
     */
    public void setRoomNow(String roomNowData) {
        Room switchedRoom = roomService.parseRoomForSwitch(roomNowData);
        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(switchedRoom.getRoomName())) {
               roomNow = room;
            }
        }
    }

    /**
     * The method creates new room on the server
     * if user created room he is admin of this room
     * @param roomData data for creating room
     */
    public void createRoom(String roomData) {
        Room room = roomService.parseNewRoomData(roomData);
        room.addUser(userConnection);
        room.setAdmin(userConnection.getUser().getName());
        Router.getInstense().getRoomList().add(room);
        System.out.println("new room created");

        roomNow = room;
    }

    /**
     * The method adds some user to room
     * sends notification to added user about it, and to room
     * @param data data for adding user
     */
    public void addUserToRoom(String data) {
        User addedUser = roomService.parseUserForAddToRoom(data);
        UserConnection connectionAdd = null;
        for (UserConnection conn: Router.getInstense().getUserList()) {
            if(conn.getUser().getName().equals(addedUser.getName())) {
                connectionAdd = conn;
            }
        }
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(roomNow.getRoomName()) && !r.getUserList().contains(connectionAdd) && connectionAdd != null) {
                if(!r.isUserInRoom(connectionAdd)) {
                    r.addUser(connectionAdd);
                    System.out.println("user successfully added");
                    try {
                        connectionAdd.getOut().write(messageService.createServerAction("ADDED_TO_ROOM") + messageService.createAddToRoomNotify(r));
                        connectionAdd.getOut().flush();

                        historyMessage.sendHistoryOfSomeRoom(r, connectionAdd);
                        String notification = "Added new user " + connectionAdd.getUser().getName();
                        notifyRoom(notification, r.getRoomName());

                    } catch (IOException e) {
                        logger.warn("while sending notify to user about addition to room and sending history messages of room", e);
                    }
                }
                break;
            }
        }


    }

    /**
     * The method for leaving user room
     * if user leaved room, server sends notify to room about it
     * @param data name of leaving room
     */
    public void leaveRoom(String data) {
        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(roomNow.getRoomName())) {
                room.removeUser(userConnection);
                notifyRoom("User " + userConnection.getUser().getName() + "leaved room",room.getRoomName());
            }
        }
    }

    /**
     * The method sends list of online users
     */
    public void sendOnlineUserList() {
        try {
            userConnection.getOut().write(messageService.createServerAction("ONLINE_LIST") + userKeeper.userListToString(getOnlineUser()) + "\n");
            userConnection.getOut().flush();
        }
        catch (IOException e) {
            logger.info("while sending list of online users to client",e);
        }

    }

    /**
     * The method for searching online user
     * @return list of online users
     */
    private List<User> getOnlineUser() {
        List<User> resultList = new ArrayList<>();

        for (UserConnection connection: Router.getInstense().getUserList()) {
            System.out.println(connection.getUser().getName());
            if( connection.getUser() != null  && !connection.getUser().getName().equals(userConnection.getUser().getName()) && connection.getUser().isOnline()) {
                resultList.add(connection.getUser());
            }
        }

        return resultList;
    }

    /**
     * The method sends message with notification to room
     * @param msg notified message
     * @param roomName name of notified room
     */
    private void notifyRoom(String msg, String roomName) {
        String testNotify = messageService.createMessage(msg,"Room notify",roomName);
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(roomName)) {
               /* for (UserConnection uc: r.getUserList()) {
                    try {
                        uc.getOut().write(messageService.createServerAction("SEND_MSG") + testNotify + "\n");
                        uc.getOut().flush();
                    }
                    catch (IOException e) {
                        logger.info("while sending notify to room",e);
                    }

                }*/
               for(String name: r.getUserList()) {
                   try {
                       UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                       uc.getOut().write(messageService.createServerAction("SEND_MSG") + testNotify + "\n");
                       uc.getOut().flush();
                   }
                   catch (IOException e) {
                       logger.warn("while sending notify to room");
                   }
               }
            }
        }
    }

    /**
     * The method sends all history of messages of some room
     * @param clientData client recipient
     */
    public void sendHistoryOfRooms(String clientData) {
        List<Room> roomNameList = roomService.parseListOfRooms(clientData);
        LocalDateTime lastOnline = userService.parseLastOnline(clientData);
        for (Room room: roomNameList) {
            historyMessage.sendHistoryRoomAfterDate(room,userConnection,lastOnline);
        }

    }

    /**
     * The method sends to admin of room list of users for banning
     */
    public void sendListUserForBan() {
        Room room = Router.getInstense().getRoomByName(roomNow.getRoomName());
        if(userConnection.getUser().getName().equals(room.getAdmin())) {
            List<User> usersForBan = new ArrayList<>();
            /*for (UserConnection uc: room.getUserList()) {
                usersForBan.add(uc.getUser());
            }*/
            for(String name: room.getUserList()) {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                if(!room.isUserBanned(uc)) {
                    usersForBan.add(Router.getInstense().getUserConnectionByName(name).getUser());
                }
            }

            try {
                userConnection.getOut().write(messageService.createServerAction("BAN_LIST") + roomService.prepareListUserForBan(usersForBan) + "\n");
                userConnection.getOut().flush();
            }
            catch (IOException e) {
                logger.info("while sending list user for parseBanUser",e);
            }

        }
    }

    /**
     * The method sends to admin of room list of users for unbanning
     */
    public void sendListUserForUnBan() {
        Room room = Router.getInstense().getRoomByName(roomNow.getRoomName());
        if(userConnection.getUser().getName().equals(room.getAdmin())) {
            List<User> usersForUnBan = new ArrayList<>();
            /*for (UserConnection uc: room.getBanList()) {
                usersForUnBan.add(uc.getUser());
            }*/
            for (String name: room.getBanList()) {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                if(room.isUserBanned(uc)) {
                    usersForUnBan.add(uc.getUser());
                }

            }

            try {
                userConnection.getOut().write(messageService.createServerAction("UNBAN_LIST") + roomService.prepareListUserForBan(usersForUnBan) + "\n");
                userConnection.getOut().flush();
            }
            catch (IOException e) {
                logger.info("while sending list user who can be unbanned",e);
            }

        }
    }

    /**
     * The method for banning user in the room
     * admin of room send request with data user for banning, server banned user and send him and room where he was banned notification about it
     * @param userData user name for banning
     */
    public void banUser(String userData) {
        User banUser = userService.parseBanUser(userData);
        Room room = Router.getInstense().getRoomByName(roomNow.getRoomName());
       /* for (UserConnection uc: room.getUserList()) {
            if(uc.getUser().getName().equals(banUser.getName())) {
                Router.getInstense().getRoomByName(roomNow.getRoomName()).getBanList().add(uc);
                notifyRoom("User " + banUser.getName() + " was banned by admin " + userConnection.getUser().getName(),room.getRoomName());
                try {
                    uc.getOut().write(messageService.createServerAction("BAN") + userService.prepareBanNotify(room));
                    uc.getOut().flush();
                }
                catch (IOException e) {
                    logger.warn("while banning user ",e);
                }
                break;
            }
        }*/
        for (String name: room.getUserList()) {
            if(name.equals(banUser.getName())) {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                Router.getInstense().getRoomByName(room.getRoomName()).getBanList().add(name);
                notifyRoom("User " + banUser.getName() + " was banned by admin " + userConnection.getUser().getName(),room.getRoomName());
                try {
                    uc.getOut().write(messageService.createServerAction("BAN") + userService.prepareBanNotify(room));
                    uc.getOut().flush();
                }
                catch (IOException e) {
                    logger.warn("while banning user ",e);
                }
                break;
            }
        }
    }

    /**
     * The method for unbanning user in the room
     * admin of room send request with data user for unbanning, server unbanned user and send him and room where he was unbanned notification about it
     * @param userData user name for unbanning
     */
    public void unBanUser(String userData) {
        User unBanUser = userService.parseUnBanUser(userData);
        Room room = Router.getInstense().getRoomByName(roomNow.getRoomName());
        /*for (UserConnection uc: room.getUserList()) {
            if(uc.getUser().getName().equals(unBanUser.getName())) {
                Router.getInstense().getRoomByName(roomNow.getRoomName()).unBanUser(uc);
                notifyRoom("User " + unBanUser.getName() + " was unbanned by admin " + userConnection.getUser().getName(),room.getRoomName());
                try {
                    uc.getOut().write(messageService.createServerAction("UNBAN") + userService.prepareUnBanNotify(room));
                    uc.getOut().flush();
                }
                catch (IOException e) {
                    logger.warn("while banning user ",e);
                }
                break;
            }
        }*/
        for (String name: room.getUserList()
             ) {
            if(name.equals(unBanUser.getName())) {

                try {
                    UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                    if(uc.getUser().isOnline()) {
                        Router.getInstense().getRoomByName(room.getRoomName()).unBanUser(uc);
                        notifyRoom("User " + unBanUser.getName() + " was unbanned by admin " + userConnection.getUser().getName(), room.getRoomName());
                        uc.getOut().write(messageService.createServerAction("UNBAN") + userService.prepareUnBanNotify(room));
                        uc.getOut().flush();
                    }
                }
                catch (IOException e) {
                    logger.warn("while banning user ",e);
                }
                catch (NullPointerException e) {
                    logger.info("unbanning user",e);
                }
                break;
            }
        }
    }

    /**
     * The method for deleting room from server
     * if user is admin of room, server delete send to user notification about deleting room, send to room last message and delete this room
     * @param data user message with name of deleting room
     */
    public void deleteRoomByAdmin(String data) {
        String roomName = roomService.parseRoomNameForDelete(data);
        Room room = Router.getInstense().getRoomByName(roomName);
        /*for (UserConnection uc: room.getUserList()) {
            try {
                uc.getOut().write(messageService.createServerAction("DELETED_ROOM"));
                uc.getOut().write(roomService.prepareDeletedRoomNotification(roomName) + "\n");
                uc.getOut().flush();
            }
            catch (IOException e) {
                logger.warn("send notify to room about deleting",e);
            }
        }*/
        for (String name: room.getUserList()) {
            try {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                uc.getOut().write(messageService.createServerAction("DELETED_ROOM"));
                uc.getOut().write(roomService.prepareDeletedRoomNotification(roomName) + "\n");
                uc.getOut().flush();
            }
            catch (IOException e) {
                logger.warn("send notify to room about deleting",e);
            }
        }
        notifyRoom("This room was deleted by admin, but you can read history messages",roomName);
        Router.getInstense().getRoomList().remove(room);
    }

    public void sendListUserFromRoom(String data) {
        Room room = Router.getInstense().getRoomByName(roomService.parseRoomName(data));
        try {
            userConnection.getOut().write(messageService.createServerAction("USERS_IN_ROOM"));
            userConnection.getOut().write(roomService.prepareUsersForSending(room) + "\n");
            userConnection.getOut().flush();
        }
        catch (IOException e) {
            logger.warn("send list users from room ",e);
        }
    }
}
