package messenger.model;

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
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationServiceImpl implements UserRegistrationService {

    private List<User> userList;
    private static final Logger logger = Logger.getLogger(UserRegistrationServiceImpl.class);
    private User user;


    public UserRegistrationServiceImpl() {
        this.userList = new ArrayList<>();
        this.userList.add(new User("Ivan","1234"));
        this.userList.add(new User("Inna","1234"));
        this.userList.add(new User("Kate","1234"));
    }

    private void getUsers(String fileName) {

    }

    @Override
    public boolean registration(User user) {

        return false;
    }

    @Override
    public boolean auth(String userData) {
        //User user = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(userData)));

            NodeList nodeList = document.getElementsByTagName("user");


            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();

                user = new User(nick,password);

                for (User us: userList) {
                    if(us.equals(user)) {
                        return true;
                    }
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

    public User getUser() {
        return user;
    }
}
