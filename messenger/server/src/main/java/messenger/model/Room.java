package messenger.model;

import messenger.сontroller.Rooter;


import java.io.*;
import java.util.Set;

public class Room {
    private Set<User> userList;
    private Set<User> adminList;
    private Set<User> banList;
    private Set<User> muteList;

    public void sendMassage(User from, String text) {
        try {
            File massage = new File("src/ua/sumdu/lab2/group7/Model/XML/XMLFiles/SendMassage.xml");
            Reader msg = new BufferedReader(new FileReader(massage));
            String tmpMsg = ((BufferedReader) msg).readLine();
            for (User writer : Rooter.getInstense().getUserList()) {
                if(writer.equals(from))
                writer.getOut().write(tmpMsg+"\n");
                writer.getOut().flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(User user){
        if (!userList.contains(user)){
            userList.add(user);
            return true;
        }else{
            return false;
        }
    }
}
