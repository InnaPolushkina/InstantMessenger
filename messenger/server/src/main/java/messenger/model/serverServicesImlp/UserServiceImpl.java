package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverServices.UserService;
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
import java.time.LocalDateTime;

public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Override
    public User parseBanUser(String msg) {
        User user = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(msg)));

            NodeList nodeList = document.getElementsByTagName("parseBanUser");
            Node node = nodeList.item(0);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String userName = element.getTextContent();
                user = new User(userName);
            }
        }
        catch (IOException e) {
            logger.warn("while parsing user for banning from user",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing user for banning from user",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing user for banning from user",e);
        }
        return user;
    }

    @Override
    public User parseUnbanUser(String msg) {
        User user = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(msg)));

            NodeList nodeList = document.getElementsByTagName("parseUnbanUser");
            Node node = nodeList.item(0);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String userName = element.getTextContent();
                user = new User(userName);
            }
        }
        catch (IOException e) {
            logger.warn("while parsing user for banning from user",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing user for banning from user",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing user for banning from user",e);
        }
        return user;
    }

    @Override
    public LocalDateTime parseLastOnline(String data) {
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));
            Element element = document.getDocumentElement();
            String s = element.getAttribute("after");
            localDateTime = LocalDateTime.parse(s);
        }catch (IOException e) {
            logger.warn("parsing date of last user online",e);
        }
        catch (SAXException e) {
            logger.warn("parsing date of last user online",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("parsing date of last user online",e);
        }
        return localDateTime;
    }

    @Override
    public String sendBanNotify(Room room) {
        return "<ban>" + room.getRoomName() + "</ban>\n";
    }

    @Override
    public String sendUnBanNotify(Room room) {
        return "<unban>" + room.getRoomName() + "</unban>\n";
    }
}
