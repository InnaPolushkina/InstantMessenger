package messenger.model;


import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.*;
import java.util.LinkedList;
import java.util.List;

/**
 * The class for saving history of client messages at the server and sending it to client
 */
public class HistoryMessage {
    private List<String> stringList = new LinkedList<>();
    private List<LocalDateTime> dateList = new LinkedList<>();
    private MessageService messageService;

    private static final Logger logger = Logger.getLogger(HistoryMessage.class);

    /**
     * The constructor of class
     * @param messageService object of interface MessageService for working with client messages
     */
    public HistoryMessage(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * The method for adding message to list and adding date of message getting to date list
     * @param message client no parsed messages
     */
    public void addMessageToStory(String message) {
        stringList.add(message);
        dateList.add(LocalDateTime.now());
    }

    /**
     * The method sends story of all rooms where is client
     * @param userConnectionRecipient client connection
     * @throws IOException if client socket is closed
     */
    public void sendStory(UserConnection userConnectionRecipient) throws IOException {
        for (String s: stringList) {
            MessageServer messageServer = messageService.parseMessage(s);
            Room getter = messageServer.getRecipient();

            for (Room room: Router.getInstense().getRoomList()) {

                if(room.getRoomName().equals(getter.getRoomName()) /*&& getter.getRoomName().equals(roomName) */) {

                    /*for (UserConnection uc: room.getUserList()) {

                        if (uc.getUser().getName().equals(userConnectionRecipient.getUser().getName())) {
                            //userConnectionRecipient.getOut().write( "<action>SEND_MSG</action>\n" + s + "\n");
                            userConnectionRecipient.getOut().write(messageService.sendServerAction("SEND_MSG") + s + "\n");
                            userConnectionRecipient.getOut().flush();
                            break;
                        }
                    }*/
                    for (String name: room.getUserList()) {
                        if(name.equals(userConnectionRecipient.getUser().getName())) {
                            userConnectionRecipient.getOut().write(messageService.sendServerAction("SEND_MSG") + s + "\n");
                            userConnectionRecipient.getOut().flush();
                            break;
                        }
                    }
                }
            }
        }

    }

    /**
     * The method sends history of some room to client
     * using after client added to new room
     * @param roomRecipient room recipient
     * @param userConnectionRecipient client connection
     * @throws IOException if client socket are closed
     */
    public void sendHistoryOfSomeRoom(Room roomRecipient,UserConnection userConnectionRecipient) throws IOException{
        for (String s: stringList) {
            MessageServer messageServer = messageService.parseMessage(s);
            Room getter = messageServer.getRecipient();
            for (Room room: Router.getInstense().getRoomList()) {
                if (getter.getRoomName().equals(roomRecipient.getRoomName()) && room.getRoomName().equals(getter.getRoomName())) {
                    /*for (UserConnection uc: room.getUserList()) {
                        if (uc.getUser().getName().equals(userConnectionRecipient.getUser().getName())) {
                            //userConnectionRecipient.getOut().write( "<action>SEND_MSG</action>\n" + s + "\n");
                            userConnectionRecipient.getOut().write(messageService.sendServerAction("SEND_MSG") + s + "\n");
                            userConnectionRecipient.getOut().flush();
                            break;
                        }
                    }*/

                    for (String name: room.getUserList()) {
                        if(name.equals(userConnectionRecipient.getUser().getName())) {
                            UserConnection uc = Router.getInstense().getUserConnectionByName(name);
                            userConnectionRecipient.getOut().write(messageService.sendServerAction("SEND_MSG") + s + "\n");
                            userConnectionRecipient.getOut().flush();
                            break;
                        }
                    }


                    break;
                }
            }
        }
    }

    /**
     * The method sends history of some room after date of client disconnection
     * if Router have not any info about interested room, method create room with such roomName at the Router and add user connection to this room
     * if needed room have not userConnection with such name, userConnection add to room
     * @param roomForUser name of room where are messages from
     * @param userConnection client connection
     * @param disconnectedDate date of client disconnection
     */
    public void sendHistoryRoomAfterDate(Room roomForUser,UserConnection userConnection,LocalDateTime disconnectedDate) {

        Room r = Router.getInstense().getRoomByName(roomForUser.getRoomName());
        if(r == null) {
            Room newRoom = new Room(roomForUser.getRoomName());
            newRoom.setAdmin(roomForUser.getAdmin());
            newRoom.addUser(userConnection);
            Router.getInstense().getRoomList().add(newRoom);
            logger.info("room " + roomForUser.getRoomName() + "created and user added to it");
        }
        try {
            //UserConnection uc = r.getUserConnectionByName(userConnection.getUser().getName());
            UserConnection uc = Router.getInstense().getUserConnectionByName(userConnection.getUser().getName());
            if(uc == null) {
                Router.getInstense().getRoomByName(roomForUser.getRoomName()).addUser(userConnection);
                logger.info("user added to room " + roomForUser.getRoomName());
            }

        }catch (NullPointerException e) {
            Router.getInstense().getRoomByName(roomForUser.getRoomName()).addUser(userConnection);
            logger.info("user added to room " + roomForUser.getRoomName());
        }


        for (int i = 0; i < dateList.size(); i++) {
            if(dateList.get(i).isAfter(disconnectedDate)) {
                for (int j = i; j < stringList.size(); j++) {
                    String s = stringList.get(j);
                    MessageServer messageServer = messageService.parseMessage(s);
                    String roomGetter = messageServer.getRecipient().getRoomName();
                    for (Room room: Router.getInstense().getRoomList()) {
                        if(room.getRoomName().equals(roomGetter) && room.getRoomName().equals(roomForUser)) {
                            try {
                                userConnection.getOut().write(messageService.sendServerAction("SEND_MSG") + s + "\n");
                                userConnection.getOut().flush();
                            }
                            catch (IOException e) {
                                logger.warn("sending messages of room to user",e);
                            }
                            break;
                        }
                    }

                }
            }
        }
    }

}
