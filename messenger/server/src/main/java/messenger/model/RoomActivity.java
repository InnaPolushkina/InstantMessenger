package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.*;
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
    private RoomKeeper roomKeeper;

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

    /**
     * The setter for room keeper
     * @param userService object of class that implements interface UserService
     * @see UserService
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * The setter for room keeper
     * @param roomKeeper object of class that implements interface RoomKeeper
     * @see RoomKeeper
     */
    public void setRoomKeeper(RoomKeeper roomKeeper) {
        this.roomKeeper = roomKeeper;
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
        roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());
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
                        connectionAdd.sendMessage(messageService.createServerAction("ADDED_TO_ROOM") + messageService.createAddToRoomNotify(r));

                        historyMessage.sendHistoryOfSomeRoom(r, connectionAdd);
                        String notification = "Added new user " + connectionAdd.getUser().getName();
                        notifyRoom(notification, r.getRoomName());

                        roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());

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
     * if count of users in room = 0, server delete this room
     */
    public void leaveRoom() {
        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(roomNow.getRoomName())) {
                room.removeUser(userConnection);
                notifyRoom("User " + userConnection.getUser().getName() + " leaved room",room.getRoomName());
                roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());
                if(room.getUserList().size() == 0) {
                    Router.getInstense().getRoomList().remove(room);
                    roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());
                    System.out.println("Room removed");
                }
                break;
            }
        }
    }

    /**
     * The method sends list of online users
     */
    public void sendOnlineUserList() {
        try {
           userConnection.sendMessage(messageService.createServerAction("ONLINE_LIST") + userKeeper.userListToString(getOnlineUser()) + "\n");
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
        String roomNotify = messageService.createMessage(msg,"Room notify",roomName);
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(roomName)) {
               for(String name: r.getUserList()) {
                   try {
                       UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                      uc.sendMessage(messageService.createServerAction("SEND_MSG") + roomNotify + "\n");
                      historyMessage.addMessageToStory(roomNotify);
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
            for(String name: room.getUserList()) {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                if(!room.isUserBanned(uc)) {
                    usersForBan.add(Router.getInstense().getUserConnectionByName(name).getUser());
                }
            }

            try {
                userConnection.sendMessage(messageService.createServerAction("BAN_LIST") + roomService.prepareListUserForBan(usersForBan) + "\n");
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
            for (String name: room.getBanList()) {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                if(room.isUserBanned(uc)) {
                    usersForUnBan.add(uc.getUser());
                }

            }

            try {
               userConnection.sendMessage(messageService.createServerAction("UNBAN_LIST") + roomService.prepareListUserForBan(usersForUnBan) + "\n");
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
        for (String name: room.getUserList()) {
            if(name.equals(banUser.getName())) {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                Router.getInstense().getRoomByName(room.getRoomName()).getBanList().add(name);
                notifyRoom("User " + banUser.getName() + " was banned by admin " + userConnection.getUser().getName(),room.getRoomName());
                try {
                    uc.sendMessage(messageService.createServerAction("BAN") + userService.prepareBanNotify(room));
                    roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());
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
        for (String name: room.getUserList()
             ) {
            if(name.equals(unBanUser.getName())) {

                try {
                    UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                    if(uc.getUser().isOnline()) {
                        Router.getInstense().getRoomByName(room.getRoomName()).unBanUser(uc);
                        notifyRoom("User " + unBanUser.getName() + " was unbanned by admin " + userConnection.getUser().getName(), room.getRoomName());
                        uc.sendMessage(messageService.createServerAction("UNBAN") + userService.prepareUnBanNotify(room));
                        roomKeeper.saveRoomsInfo(Router.getInstense().getRoomList());
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
        for (String name: room.getUserList()) {
            try {
                UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                uc.sendMessage(messageService.createServerAction("DELETED_ROOM") + roomService.prepareDeletedRoomNotification(roomName) + "\n");
            }
            catch (IOException e) {
                logger.warn("send notify to room about deleting",e);
            }
        }
        notifyRoom("This room was deleted by admin, but you can read history messages",roomName);
        Router.getInstense().getRoomByName(roomName).setDeleted(true);
        Router.getInstense().getRoomByName(roomName).removeUser(userConnection);
    }

    public void sendListUserFromRoom(String data) {
        Room room = Router.getInstense().getRoomByName(roomService.parseRoomName(data));
        try {
           userConnection.sendMessage(messageService.createServerAction("USERS_IN_ROOM") + roomService.prepareUsersForSending(room) + "\n");
        }
        catch (IOException e) {
            logger.warn("send list users from room ",e);
        }
    }
}
