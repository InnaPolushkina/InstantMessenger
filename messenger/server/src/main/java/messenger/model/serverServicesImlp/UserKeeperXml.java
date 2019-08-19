package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.User;
import messenger.model.serverServices.UserKeeper;
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

public class UserKeeperXml implements UserKeeper {

    private static final Logger logger = Logger.getLogger(UserKeeperXml.class);
    private String fileName;

    public UserKeeperXml(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public void saveToFile( List<User> userList) {
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
            StreamResult streamResult = new StreamResult(new File(fileName));
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
    public List<User> loadFromFile() {
        List<User> userList = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new InputSource(new FileReader(fileName)));

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
            try {
                PrintWriter writer = new PrintWriter(fileName, "UTF-8");
                writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<users/>");
                writer.close();
            }
            catch (IOException ex) {
                logger.info("while creating new file for user info",e);
            }
        }
        catch (IOException e) {
            logger.warn(e);
        }
        catch (SAXException e) {
            logger.warn(e);
        }

        return userList;
    }

    @Override
    public String userListToString(List<User> userList) {
        String result = null;
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
            }
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, new StreamResult(sw));
            result = sw.toString();
        } catch (TransformerException e) {
            logger.warn("when transformed list of user to xml");
        } catch (ParserConfigurationException e) {
            logger.warn("when parsing list of users to xml ");
        }
        return result;
    }

}
