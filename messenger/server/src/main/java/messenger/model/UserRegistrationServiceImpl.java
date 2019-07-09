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
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationServiceImpl implements UserRegistrationService {

    private List<User> userList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(UserRegistrationServiceImpl.class);
    private User user;
    private String fileUsers = "server/src/main/java/messenger/db/users.txt";

    /**
     * the public constructor
     * create object of this class
     */
    public UserRegistrationServiceImpl() {
       // getUsers(fileUsers);
    }

    /**
     * the method deserialize list of users from file
     */
    public void getUsers(/*String fileName*/) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileUsers);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            userList = (List<User>) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();

        } catch (FileNotFoundException e) {
            logger.warn("can't find file for loading list of users ",e);
        }
        catch (IOException e) {
            logger.warn("can't load list of users",e);
        }
        catch (ClassNotFoundException e) {
            logger.warn("problems with loading list of users from file",e);
        }
    }

    /**
     * the method serialize list of users to file
     */
    public void saveUsers(/*String fileName*/) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileUsers);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(userList);

            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            logger.warn("can't find for saving list of users",e);
        }
        catch (IOException e) {
            logger.warn("can't save list of users to file ",e);
        }
    }

    @Override
    public boolean registration(String userData) {
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

            if(userList.contains(user)) {
                return false;
            }
            else {
                userList.add(user);
                return true;
            }
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
        try {
            //user = getUserFromXml(userData);
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
     * the method for getting user, what connecting to server
     * @return object of User
     */
    public User getUser() {
        return user;
    }
}
