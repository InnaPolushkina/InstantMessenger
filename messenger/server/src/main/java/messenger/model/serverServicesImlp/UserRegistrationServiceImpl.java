package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.User;
import messenger.model.serverServices.UserKeeper;
import messenger.model.serverServices.UserRegistrationService;
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
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationServiceImpl implements UserRegistrationService {

    private List<User> userList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(UserRegistrationServiceImpl.class);
    private User user;
    private UserKeeper userKeeper;

    /**
     * the public constructor
     * create object of this class
     */
    public UserRegistrationServiceImpl(/*List<User> userList*/ UserKeeper userKeeper) {
        this.userList = userKeeper.loadFromFile();
       // getUsers(fileUsers);
        this.userKeeper = userKeeper;
    }

    @Override
    public boolean registration(String userData) {
       userList = userKeeper.loadFromFile();
        try {

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(userData)));

            NodeList nodeList = document.getElementsByTagName("reg");

            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();

                user = new User(nick,password);
            }

            for (User u: userList) {
                if(u.getName().equals(user.getName()) )
                    return false;
            }
            userList.add(user);
           userKeeper.saveToFile(userList);
            return true;

        } catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with xml from client when one registering ",e);
        }
            catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when one registering ",e);
        }
            catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when one registering ",e);
        }

        return false;
    }

    @Override
    public boolean auth(String userData) {
        userList = userKeeper.loadFromFile();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(userData)));

            NodeList nodeList = document.getElementsByTagName("auth");

            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();

                user = new User(nick,password);
            }
                for (User us: userList) {
                    if (us.equals(user)) {
                        return true;
                    }
                }

        } catch (ParserConfigurationException e) {
           logger.warn("exception while parsing string with xml from client when one authorizing ",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when one authorizing ",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when one authorizing ",e);
        }

        return false;
    }

    /**
     * the method for getting user, what authorized to server
     * @return object of User
     */
    public User getAuthorizedUser() {
        return user;
    }
}
