package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.MessageServer;
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


    @Override
    public void sendMessage(MessageServer msg) {

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
                User user = new User();
                user.setName(nick);
                msg = new MessageServer(user, text);
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
}
