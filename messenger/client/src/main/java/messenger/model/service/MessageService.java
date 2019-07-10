package messenger.model.service;

import messenger.model.entity.Message;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Interface MessageService contains methods for service messages between client and server
 * @author Inna
 */
public interface MessageService {
    String sendMessage(Message msg);

    /**
     * the method parse got string message from server
     * @param message string with message from server
     * @return object of class MessageServer
     * @throws ParserConfigurationException if can`t parse message
     * @throws IOException if can`t parse message
     * @throws SAXException if can`t parse message
     * @see Message
     */
    Message parseMessage(String message) throws ParserConfigurationException, IOException, SAXException;
}
