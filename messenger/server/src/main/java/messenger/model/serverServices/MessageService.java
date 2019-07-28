package messenger.model.serverServices;

import messenger.model.serverEntity.ClientAction;
import messenger.model.serverEntity.MessageServer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface MessageService {
    String sendMessage(String text, String nameSender, String roomRecipient);

    /**
     * the method parses got string message from server
     * @param message string with message from server
     * @return object of class MessageServer
     * @throws ParserConfigurationException if can`t parse message
     * @throws IOException if can`t parse message
     * @throws SAXException if can`t parse message
     * @see MessageServer
     */
    MessageServer parseMessage(String message);

    /**
     * the method parses message with client action from string to object of ClientAction
     * @param clientAction have string with client action
     * @return object of enum ClientAction
     * @see ClientAction
     */
    ClientAction parseClientAction(String clientAction);

    String sendServerAction(String action);

    String sendNameNewRoom(String nameRoom);
}
