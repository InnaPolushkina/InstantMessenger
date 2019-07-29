package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.User;
import messenger.model.serverServices.UserService;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    public void ban(User user) {

    }

    @Override
    public void unban(User user) {

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
}
