package messenger.model.message;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MessageServiceImpl implements MessageService {
    @Override
    public void sendMessage(MessageServer msg) {

    }

    @Override
    public MessageServer parseMessage(String message) throws ParserConfigurationException, IOException, SAXException {
        MessageServer msg = null;

        return null;
    }
}
