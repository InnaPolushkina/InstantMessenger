package messenger.controller;

import messenger.model.User;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

public class Handler extends Thread{
    private User user = new User();
    public Handler(Socket socket) {
        user.setUserSocket(socket);
    }
    public void run() {
        System.out.println("OK");
        try {
            user.setIn(new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream())));
            user.setOut(new BufferedWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream())));
            boolean isValidLogin = true;
            while (true) {
                user.setName(user.getIn().readLine());
                if (user.getName().length() == 0) {
                    isValidLogin = false;
                }
                synchronized (user.getName()) {
                    if (validateUser(user.getName())) {
                        Rooter.getInstense().getUserList().add(user);
                        Rooter.getInstense().getViewLogs().print("New User");
                        break;
                    }else {
                        isValidLogin = false;
                        break;
                    }
                }
            }
            user.getOut().write(String.valueOf(isValidLogin) + "\n");
            user.getOut().flush();
            while (true) {
                String input = user.getIn().readLine();
                if (input == null) {
                    continue;
                }
                Rooter.getInstense().getXmlGen().sendMassage(input,user.getName());
                for (User writer : Rooter.getInstense().getUserList()) {
                    File massage = new File("src/main/java/messenger/model/xml/xmlFiles/SendMassage.xml");
                    Reader msg = new BufferedReader(new FileReader(massage));
                    writer.getOut().write(((BufferedReader) msg).readLine() + "\n");
                    writer.getOut().flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                user.getUserSocket().close();
            } catch (IOException e) {
            }
        }
    }
    private boolean validateUser(String name) {
        Iterator<User> iterator = Rooter.getInstense().getUserList().iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            if(user.getName().equals(name)){
                return false;
            }
        }
        return true;
    }
}
