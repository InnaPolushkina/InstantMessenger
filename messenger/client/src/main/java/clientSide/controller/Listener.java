package main.java.clientSide.controller;

import main.java.clientSide.model.User;

import java.io.*;
import java.net.Socket;

public class Listener extends Thread {

    private User user = new User();

    public Listener(Socket socket) {
        user = new User();
        user.setUserSocket(socket);
    }

    @Override
    public void run() {
        System.out.println("Enter your name ");
        System.out.println("User " + user.getName() + " connected");
        while (true) {
            try {
                user.setIn(new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream())));
                user.setOut(new BufferedWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream())));
                user.setUserMes(new BufferedReader(new InputStreamReader(System.in)));

                String str = user.getUserMes().readLine();
                user.getOut().write(str + "\n");
                user.getOut().flush();


                showMessage();


            } catch (IOException ex) {
                System.out.println(ex);
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
