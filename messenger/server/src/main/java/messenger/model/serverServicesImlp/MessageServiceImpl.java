package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.ClientAction;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverServices.MessageService;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class MessageServiceImpl implements MessageService {

    private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);
    private String historyMessages = "messenger/model/db/messagesHistory.xml";


  /*  @Override
    public String createMessage(MessageServer msg) {
        return "<message><nick>" + msg.getSender().getName() + "</nick><text>" + msg.getText() + "</text><recipient>" + msg.getRecipient().getRoomName() + "</recipient></message>";
    }*/

    @Override
    public String createMessage(String text, String nameSender, String roomRecipient) {
        return "<message><nick>" + nameSender + "</nick><text>" + text + "</text><recipient>" + roomRecipient + "</recipient></message>";
    }

    @Override
    public MessageServer parseMessage(String message) {
        MessageServer msg = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(message)));
            NodeList nodeList = document.getElementsByTagName("message");

            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                String text = element.getElementsByTagName("text").item(0).getTextContent();
                String recipient = element.getElementsByTagName("recipient").item(0).getTextContent();
                System.out.println(recipient);
                User userSender = new User();
                userSender.setName(nick);
                Room roomRecipient = new Room(recipient);
                msg = new MessageServer(userSender, text);
                msg.setRecipient(roomRecipient);
            }
        }
         catch (IOException e) {
            logger.warn("while parsing message from user",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing message from user",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing message from user",e);
        }
        return msg;
    }

    @Override
    public ClientAction parseClientAction(String clientAction) {
        ClientAction result = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(clientAction)));

            NodeList nodeList = document.getElementsByTagName("action");
            Node node = nodeList.item(0);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                result = ClientAction.valueOf(ClientAction.class,element.getTextContent());
                return result;
            }
        }
        catch (IOException e) {
            logger.warn("while parsing action from user",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing action from user",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing action from user",e);
        }
        catch (IllegalArgumentException e) {
            logger.warn("user send unknown action",e);
        }
        return result;
    }

    @Override
    public String createServerAction(String action) {
        return "<action>" + action + "</action>\n";
    }


    @Override
    public String createAddToRoomNotify(Room room) {
        return "<room admin = \"" + room.getAdmin() + "\">" + room.getRoomName() + "</room>\n";
    }
}
