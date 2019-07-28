package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;
import messenger.model.serverServices.RoomService;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServicesImlp.MessageServiceImpl;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RoomActivity {
    private UserConnection userConnection;
    private RoomService roomService;
    private static final Logger logger = Logger.getLogger(RoomActivity.class);
    private UserKeeper userKeeper;
    private HistoryMessage historyMessage;
    private MessageService messageService;
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
     * The method sets new value to roomNow
     * @param roomNowData have string with data from client about changing room
     * method calls method changeRoom() from RoomService interface for parsing string from client to object of class Room
     */
    public void setRoomNow(String roomNowData) {
        Room switchedRoom = roomService.changeRoom(roomNowData);
        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(switchedRoom.getRoomName())) {
               roomNow = room;
            }
        }
    }

    public void createRoom(String roomData) {
        Room room = roomService.createRoom(roomData);
        room.addUser(userConnection);
        Router.getInstense().getRoomList().add(room);
        System.out.println("new room created");

        roomNow = room;
    }

    public void addUserToRoom(String data) {
        User addedUser = roomService.addUserToRoom(data);
        UserConnection connectionAdd = null;
        for (UserConnection conn: Router.getInstense().getUserList()) {
            if(conn.getUser().getName().equals(addedUser.getName())) {
                connectionAdd = conn;
            }
        }
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(roomNow.getRoomName()) && !r.getUserList().contains(connectionAdd) && connectionAdd != null) {
                r.addUser(connectionAdd);
                System.out.println("user successfully added");
                try {
                    connectionAdd.getOut().write(messageService.sendServerAction("ADDED_TO_ROOM") + messageService.sendNameNewRoom(r.getRoomName()));
                    connectionAdd.getOut().flush();

                    historyMessage.sendHistoryOfSomeRoom(r,connectionAdd);
                    String notification = "Added new user " + connectionAdd.getUser().getName();
                    notifyRoom(notification, r.getRoomName());

                }catch (IOException e) {
                    logger.warn("while sending notify to user about addition to room and sending history messages of room",e);
                }

            }
            else {
                logger.warn("can't add offline user to chat");
            }
        }


    }

    public void leaveRoom(String data) {
        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(roomNow.getRoomName())) {
                room.removeUser(userConnection);
               // sendNotifyMsg("Leaved room . . . >>",room);
                notifyRoom("User " + userConnection.getUser().getName() + "leaved room",room.getRoomName());
            }
        }
    }

    public void sendOnlineUserList() {
        try {
           // userConnection.getOut().write("<action>ONLINE_LIST</action>\n" + userKeeper.userListToString(getOnlineUser()) + "\n");
            userConnection.getOut().write(messageService.sendServerAction("ONLINE_LIST") + userKeeper.userListToString(getOnlineUser()) + "\n");
            userConnection.getOut().flush();
        }
        catch (IOException e) {
            logger.info("while sending list of online users to client",e);
        }

    }

    private List<User> getOnlineUser() {
        List<User> resultList = new ArrayList<>();

        for (UserConnection connection: Router.getInstense().getUserList()) {
            System.out.println(connection.getUser().getName());
            if( connection.getUser() != null  && !connection.getUser().getName().equals(userConnection.getUser().getName())) {
                resultList.add(connection.getUser());
            }
        }

        return resultList;
    }

    private void notifyRoom(String msg, String roomName) {
        //String testNotify = "<message><nick>Room Notify</nick><text>" + msg + "</text><recipient>" + roomName + "</recipient></message>";
        String testNotify = messageService.sendMessage(msg,"Room notify",roomName);
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(roomName)) {
                for (UserConnection uc: r.getUserList()) {
                    try {
                        uc.getOut().write(messageService.sendServerAction("SEND_MSG") + testNotify + "\n");
                        uc.getOut().flush();
                    }
                    catch (IOException e) {
                        logger.info("while sending notify to room",e);
                    }

                }
            }
        }
    }
}
