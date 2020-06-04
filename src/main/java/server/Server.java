package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.SQLException;
import java.util.ArrayList;

public class Server {

    private ArrayList<User> Users = new ArrayList<>();

    private DB db;

    public Server() {


        try {
            db = new DB();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database \"DBchat.db\" is NOT connected");
        }


    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Socket \"%d\" is created\n", port);

            while (true) {
                Socket candidateSocket = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(candidateSocket.getOutputStream());
                DataInputStream in = new DataInputStream(candidateSocket.getInputStream());
                final String socetMSG = in.readUTF();
                try {
                    initUser(socetMSG);
                    out.writeUTF("/succeeded");
                } catch (AuthException e) {
                    e.printStackTrace();
                    out.writeUTF("/error");
                }


                /*
                System.out.println(preparedMsg[0]);*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        private User regUser(String[] message) throws AuthException {
            User user = new User(message[1], message[2]);
            if (!authService.addNewUser(user)) {
                throw new AuthException("Login busy");
            }
            return user;
        }*/

    }


    private void initUser(String msg) throws AuthException {
        final String[] preparedMsg = msg.split(" ");
        if (preparedMsg.length != 3)
            throw new AuthException("Wrong message");

        if (preparedMsg[0].equals("/auth")) {
            Users.add(checkUser(preparedMsg));

        } else if (preparedMsg[0].equals("/reg")) {
            Users.add(regUser(preparedMsg));
        } else {
            throw new AuthException("Wrong message");
        }
    }

    private User regUser(String[] message) throws AuthException {
        boolean isUnic = true;
        for (User user : Users)
            isUnic = isUnic && (!user.getName().equals(message[1]));

        if ((!isUnic) || (db.checkUser(message[1]) != -1)) {
            throw new AuthException("Login busy");
        } else {
            System.out.printf("User \"%s\" registred\n",message[1]);
            return new User(db.insertUser(message[1], message[2]), message[1], message[2]);
        }
    }

    private User checkUser(String[] message) throws AuthException {
        boolean isUnic = true;
        for (User user : Users)
            isUnic = isUnic && (!user.getName().equals(message[1]));
        if (!isUnic) throw new AuthException("Already logined");
        int id = db.checkUser(message[1], message[2]);
        if (id == -1) {
            throw new AuthException("Wrong login or password");
        }
        System.out.printf("User \"%s\" logined\n",message[1]);
        return new User(id, message[1], message[2]);
    }

}
