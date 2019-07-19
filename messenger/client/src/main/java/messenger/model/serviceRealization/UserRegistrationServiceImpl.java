package messenger.model.serviceRealization;

import messenger.controller.Router;
import messenger.model.entity.User;
import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;
import messenger.model.service.UserRegistrationService;
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

    private Router router;
    private static final Logger logger = Logger.getLogger(UserRegistrationServiceImpl.class);

    public UserRegistrationServiceImpl() {
        this.router = Router.getInstance();
    }

    public UserRegistrationServiceImpl(Router router) {
        this.router = router;
    }

    @Override
    public void registration(String username, String password) throws UserRegistrationException {
        String regMsg = "<reg><nick>" + username + "</nick><password>" + password + "</password></reg>";
        boolean result = false;
        router.sendSimpleMsg(regMsg);
        try {
            result = Boolean.parseBoolean(router.getListener().messageFromServer());
        } catch (IOException e) {
            throw new UserRegistrationException(e.getMessage(),e);
        }
        if(!result) {
            throw new UserRegistrationException("Name is not correct, one of users have this nick");
        }
    }

    @Override
    public void auth(String username, String password) throws AuthException {
        String authMsg = "<auth><nick>" + username + "</nick><password>" + password + "</password></auth>";
        boolean result = false;
        router.sendSimpleMsg(authMsg);
        try {
            result = Boolean.parseBoolean(router.getListener().messageFromServer());
        } catch (IOException e) {
            throw new AuthException(e);
        }
        if(!result) {
            throw new AuthException("Name or password is not correct");
        }
    }

    @Override
    public List<User> parseUserList(String userList) {
        List<User> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(userList)));
            NodeList nodeList = document.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                    User user = new User(nick);
                    list.add(user);
                }
            }

        }
        catch (IOException e) {
            logger.warn("while parsing list of online users ",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing list of online users ",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing list of online users ",e);
        }

        return list;
    }


}
