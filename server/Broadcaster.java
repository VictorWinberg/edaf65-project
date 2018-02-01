package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Broadcaster extends Thread {

  private Mailbox mailbox;
  private List<Socket> sockets;
  
  public Broadcaster(Mailbox mailbox) {
    this.mailbox = mailbox;
    sockets = new ArrayList<Socket>();
  }

  public synchronized void add(Socket socket) {
    sockets.add(socket);
  }

  public synchronized void remove(Socket socket) {
    sockets.remove(socket);
  }

  @Override
  public void run() {
    while(true) {
      try {
        String value = mailbox.get();
        for (Socket socket : sockets) {
          OutputStream out = socket.getOutputStream();
          out.write((value + "\n").getBytes());
          out.flush();
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}