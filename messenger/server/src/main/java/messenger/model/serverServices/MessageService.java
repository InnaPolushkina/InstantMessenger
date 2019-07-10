package messenger.model.serverServices;

import messenger.model.serverEntity.MessageServer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface MessageService {
    void sendMessage(MessageServer msg);

    /**
     * the method parse got string message from server
     * @param message string with message from server
     * @return object of class MessageServer
     * @throws ParserConfigurationException if can`t parse message
     * @throws IOException if can`t parse message
     * @throws SAXException if can`t parse message
     * @see MessageServer
     */
    MessageServer parseMessage(String message);
}
