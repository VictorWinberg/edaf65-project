package server;

import java.io.*;
import java.net.*;

public class ChatServer {

  public static void main(String args[]) throws IOException {
    int port = Integer.parseInt(args[0]);

    Mailbox mailbox = new Mailbox();
    Broadcaster broadcaster = new Broadcaster(mailbox);
    broadcaster.start();

    ServerSocket serverSocket = new ServerSocket(port);

    while (true) {
      Socket socket = serverSocket.accept();
      ServerThread thread = new ServerThread(mailbox, broadcaster, socket);
      thread.start();
    }
  }
}

class ServerThread extends Thread {

  private Mailbox mailbox;
  private Broadcaster broadcaster;
  private Socket socket;

  public ServerThread(Mailbox mailbox, Broadcaster broadcaster, Socket socket) {
    this.mailbox = mailbox;
    this.broadcaster = broadcaster;
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      System.out.println("Client connected: " + socket.getInetAddress());
      broadcaster.add(socket);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      OutputStream out = socket.getOutputStream();

      while (!socket.isClosed()) {
        try {
          String input = in.readLine();
          if (input == null) {
            socket.close();
            break;
          }
          input += " ";
          char command = input.split(":")[0].trim().charAt(0);
          String message = input.split(":")[1].trim();
          System.out.println("Client: " + message);
          switch (command) {
            case 'M':
              mailbox.set(message);
              break;
            case 'E':
              out.write((message + "\n").getBytes());
              break;
            case 'Q':
              socket.close();
              break;
            default:
              throw new IllegalArgumentException("Client incorrect command: " + command);
          }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
          System.out.println("Client incorrect command");
          out.write("Incorrect command, use \"M: <message>\", \"E: <message>\" or \"Q:\".\n".getBytes());
        }
      }

      in.close();
      out.close();
      broadcaster.remove(socket);
      System.out.println("Client disconnected: " + socket.getInetAddress());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
