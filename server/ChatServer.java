package server;

import java.io.*;
import java.net.*;
import java.util.stream.*;

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

    private String welcome(Socket socket, BufferedReader in, OutputStream out) throws IOException {
        out.write(("Server: Welcome! Please enter your username:" + "\n").getBytes());
        while (!socket.isClosed()) {
            String input = in.readLine();
            if (input == null) {
                socket.close();
            } else if (input.matches("^[a-zA-Z0-9_]{3,14}$")) {
                out.write(("Server: Hi " + input + " use /help to see the server commands.\n").getBytes());
                return input;
            } else {
                out.write(("Server: Please only use alphanumeric values with underscore between 3 and 14 values.\n").getBytes());
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected: " + socket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();
            String username = welcome(socket, in, out);
            User user = new User(username, socket);
            broadcaster.add(user);

            while (!socket.isClosed()) {
                try {
                    String input = in.readLine();
                    if (input == null) {
                        socket.close();
                        break;
                    }
                    input += " ";
                    String command = input.split(" ", 2)[0].trim();
                    String message = input.split(" ", 2)[1].trim();
                    System.out.println("Client: " + message);
                    switch (command) {
                        case "/help":
                            out.write(("Server: Available commands: /help, /all, /echo, /show, /quit\n").getBytes());
                            break;
                        case "/all":
                            mailbox.set(new Message(user, message));
                            break;
                        case "/echo":
                            out.write(("Server: " + message + "\n").getBytes());
                            break;
                        case "/quit":
                            socket.close();
                            break;
                        case "/show":
                            String users = broadcaster.getUsers().stream()
                                    .map(Object::toString).collect(Collectors.joining(","));
                            out.write(("Server: Online Users - " + users + "\n").getBytes());
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    System.out.println("Client incorrect command");
                    out.write("Server: Incorrect command, use /help to see the server commands.\n".getBytes());
                }
            }

            in.close();
            out.close();
            broadcaster.remove(user);
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
