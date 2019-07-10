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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationServiceImpl implements UserRegistrationService {

    private List<User> userList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(UserRegistrationServiceImpl.class);
    private User user;
    private String fileUsersXml = "server/src/main/java/messenger/db/users.xml";

    /**
     * the public constructor
     * create object of this class
     */
    public UserRegistrationServiceImpl() {
       // getUsers(fileUsers);
    }

    /**
     * the method parse list of users from xml file
     */
    public void getUsers(/*String fileName*/) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new InputSource(new FileReader(fileUsersXml)));

            Element root = document.getDocumentElement();

            NodeList nodeList = document.getElementsByTagName("user");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                        String password = element.getElementsByTagName("password").item(0).getTextContent();

                        User newUser = new User(nick,password);
                        userList.add(newUser);
                    }
                }
        }
        catch (ParserConfigurationException e) {
            logger.warn(e);
        }
        catch (FileNotFoundException e) {
            logger.warn(e);
        }
        catch (IOException e) {
            logger.warn(e);
        }
        catch (SAXException e) {
            logger.warn(e);
        }
    }

    /**
     * the method parse list of users to xml file
     */
    public void saveUsers(/*String fileName*/) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("users");
            document.appendChild(root);

            for (User user : userList) {
                Element elementUser = document.createElement("user");
                root.appendChild(elementUser);

                Element nick = document.createElement("nick");
                nick.appendChild(document.createTextNode(user.getName()));
                elementUser.appendChild(nick);

                Element password = document.createElement("password");
                password.appendChild(document.createTextNode(user.getPassword()));
                elementUser.appendChild(password);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileUsersXml));
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(domSource, streamResult);
        }
        catch (ParserConfigurationException e) {
           logger.warn("while saving list of users to xml file");
        }
        catch (TransformerException e) {
            logger.warn("while saving list of users to xml file",e);
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
