package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Broadcaster extends Thread {

    private Mailbox mailbox;
    private Set<User> users;

    public Broadcaster(Mailbox mailbox) {
        this.mailbox = mailbox;
        users = new HashSet<>();
    }

    public synchronized void add(User user) {
        users.add(user);
    }

    public synchronized void remove(User user) {
        users.remove(user);
    }

    public synchronized Set<User> getUsers() {
        return users;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = mailbox.get();
                for (User user : users) {
                    OutputStream out = user.socket.getOutputStream();
                    out.write((msg.user.username + ": " + msg.message + "\n").getBytes());
                    out.flush();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}