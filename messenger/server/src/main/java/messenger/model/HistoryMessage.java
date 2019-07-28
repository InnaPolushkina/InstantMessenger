package messenger.model;


import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * The class for saving history of client messages at the server and sending it to client
 */
public class HistoryMessage {
    private List<String> stringList = new LinkedList<>();
   // private List<Date> dateList = new LinkedList<>();
    private MessageService messageService;

    /**
     * The constructor of class
     * @param messageService object of interface MessageService for working with client messages
     */
    public HistoryMessage(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * The method for adding message to list
     * @param message 
     */
    public void addMessageToStory(String message) {
        stringList.add(message);
       // dateList.add(new Date());
    }

    public void sendStory(UserConnection userConnectionRecipient) throws IOException {
        for (String s: stringList) {
            MessageServer messageServer = messageService.parseMessage(s);
            Room getter = messageServer.getRecipient();

            for (Room room: Router.getInstense().getRoomList()) {

                if(room.getRoomName().equals(getter.getRoomName()) /*&& getter.getRoomName().equals(roomName) */) {

                    for (UserConnection uc: room.getUserList()) {

                        if (uc.getUser().getName().equals(userConnectionRecipient.getUser().getName())) {
                            //userConnectionRecipient.getOut().write( "<action>SEND_MSG</action>\n" + s + "\n");
                            userConnectionRecipient.getOut().write(messageService.sendServerAction("SEND_MSG") + s + "\n");
                            userConnectionRecipient.getOut().flush();
                            break;
                        }
                    }
                }
            }
        }

    }

    public void sendHistoryOfSomeRoom(Room roomRecipient,UserConnection userConnectionRecipient) throws IOException{
        for (String s: stringList) {
            MessageServer messageServer = messageService.parseMessage(s);
            Room getter = messageServer.getRecipient();
            for (Room room: Router.getInstense().getRoomList()) {
                if (getter.getRoomName().equals(roomRecipient.getRoomName()) && room.getRoomName().equals(getter.getRoomName())) {
                    for (UserConnection uc: room.getUserList()) {
                        if (uc.getUser().getName().equals(userConnectionRecipient.getUser().getName())) {
                            //userConnectionRecipient.getOut().write( "<action>SEND_MSG</action>\n" + s + "\n");
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

}
