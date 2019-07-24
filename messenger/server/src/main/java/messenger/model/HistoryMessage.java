package messenger.model;


import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HistoryMessage {
    private List<String> stringList = new LinkedList<>();
    private MessageService messageService;

    public HistoryMessage(MessageService messageService) {
        this.messageService = messageService;
    }

    public void addMessageToStory(String message) {
        stringList.add(message);
    }

    public void sendStory(UserConnection userConnectionRecipient) throws IOException {
        for (String s: stringList) {
            MessageServer messageServer = messageService.parseMessage(s);
            Room getter = messageServer.getRecipient();

            for (Room room: Router.getInstense().getRoomList()) {

                if(room.getRoomName().equals(getter.getRoomName()) /*&& getter.getRoomName().equals(roomName) */) {

                    for (UserConnection uc: room.getUserList()) {

                        if (uc.getUser().getName().equals(userConnectionRecipient.getUser().getName())) {
                            userConnectionRecipient.getOut().write( "<action>SEND_MSG</action>\n" + s + "\n");
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
                            userConnectionRecipient.getOut().write( "<action>SEND_MSG</action>\n" + s + "\n");
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
