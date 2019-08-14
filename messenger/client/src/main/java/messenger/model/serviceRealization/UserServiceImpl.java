package messenger.model.serviceRealization;

import messenger.model.entity.Room;
import messenger.model.entity.User;
import messenger.model.service.UserService;
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

public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Override
    public String prepareBanUser(User user) {
        return "<parseBanUser>" + user.getName() + "</parseBanUser>";
    }

    @Override
    public String prepareUnBanUser(User user) {
        return "<parseUnBanUser>" + user.getName() + "</parseUnBanUser>";
    }

    @Override
    public Room parseBanNotification(String msg) {
        Room room = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(msg)));

            NodeList nodeList = document.getElementsByTagName("ban");
            Node node = nodeList.item(0);
            Element element = (Element) node;
            String roomName = element.getTextContent();
            System.out.println("Room name " + roomName);
            room = new Room(roomName);
        }
        catch (IOException e) {
            logger.warn("while parsing notify about banning ", e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing notify about banning ", e);
        }
        catch (SAXException e) {
            logger.warn("while parsing notify about banning ", e);
        }

        return room;
    }

    @Override
    public Room parseUnBanNotification(String msg) {
        Room room = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(msg)));

            NodeList nodeList = document.getElementsByTagName("unban");
            Node node = nodeList.item(0);
            String roomName = node.getTextContent();
            room = new Room(roomName);
        }
        catch (IOException e) {
            logger.warn("while parsing notify about unbanning ", e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing notify about unbanning ", e);
        }
        catch (SAXException e) {
            logger.warn("while parsing notify about unbanning ", e);
        }

        return room;
    }
}
