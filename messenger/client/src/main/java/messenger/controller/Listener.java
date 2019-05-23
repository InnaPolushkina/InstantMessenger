package messenger.controller;

import messenger.model.User;

import java.io.*;
import java.net.Socket;
/* в этом классе добавлю два встроеных класса для чтения и записи сообщения в поток
    после прочтения сообщения из потока Reader оно будет передавться в класс XMLPars, который выташит из него все необходимые нам данные
    после преобразования xml в строки они передадуться в класс контроллера, который отбразит их для пользователя на экран
 * обаботчики событий отправки сообщеня будут вызывать метод отправки
 */

public class Listener extends Thread {

    private User user;

    public Listener(Socket socket) {
        user = new User();
        user.setUserSocket(socket);
    }

    @Override
    public void run() {
        /*System.out.println("Enter your name ");
        System.out.println("User " + user.getName() + " connected");*/
        while (true) {
            try {
                user.setIn(new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream())));
                //user.setOut(new BufferedWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream())));
                //user.setUserMes(new BufferedReader(new InputStreamReader(System.in)));

                   /* String str = user.getUserMes().readLine();
                    user.getOut().write(str + "\n");
                    user.getOut().flush();
                    */
                    showMessage();


            } catch (IOException ex) {
                System.out.println(ex);

                break;
            }
        }


    }


    private void showMessage() {
        try {
            System.out.println(user.getIn().readLine() + "\n");

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}