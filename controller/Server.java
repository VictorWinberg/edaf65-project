package controller;

import java.io.*;
import java.net.*;
import java.util.stream.*;

import model.Minesweeper;

public class Server {

    public static void main(String args[]) throws IOException {
        int port = Integer.parseInt(args[0]);

        Mailbox mailbox = new Mailbox();
        Broadcaster broadcaster = new Broadcaster(mailbox);
        broadcaster.start();

        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            ServerExchange thread = new ServerExchange(mailbox, broadcaster, socket);
            thread.start();
        }
    }
}

class ServerExchange extends Thread {

    private Minesweeper minesweeper;
    private Mailbox mailbox;
    private Broadcaster broadcaster;
    private Socket socket;

    public ServerExchange(Mailbox mailbox, Broadcaster broadcaster, Socket socket) {
        this.mailbox = mailbox;
        this.broadcaster = broadcaster;
        this.socket = socket;
    }

    private String welcome(Socket socket, BufferedReader in, OutputStream out) throws IOException {
        out.write(("Server: Welcome!\nPlease enter your username:" + "\n").getBytes());
        while (!socket.isClosed()) {
            String input = in.readLine().trim();
            if (input == null) {
                socket.close();
            } else if (input.matches("^[a-zA-Z0-9_]{3,14}$")) {
                out.write(("Server: Hi " + input + "\nHint: Use /help to see the server commands.\n").getBytes());
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
            mailbox.set(new Message(new User("Server", null), username + " joined."));

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
                        case "/h":
                        case "/help":
                            out.write(("Server: Available commands: /help, /all, /echo, /show, /play [size] [bombs], /quit\n").getBytes());
                            break;
                        case "/a":
                        case "/all":
                            mailbox.set(new Message(user, message));
                            break;
                        case "/e":
                        case "/echo":
                            out.write((message + "\n").getBytes());
                            break;
                        case "/q":
                        case "/quit":
                            socket.close();
                            break;
                        case "/show":
                            String users = broadcaster.getUsers().stream()
                                    .map(Object::toString).collect(Collectors.joining(", "));
                            out.write(("Online Users - " + users + "\n").getBytes());
                            break;
                        case "/play": {
                            int size = Integer.parseInt(message.split(" ", 2)[0]);
                            int bombs = Integer.parseInt(message.split(" ", 2)[1]);
                            minesweeper = new Minesweeper(size, bombs);
                            out.write(("/play " + size + " " + bombs + "\n" + minesweeper.toString() + "\n" +
                                       "Game started with size " + size + " and bombs " + bombs + "!\n" +
                                       "Commands: /pick [x] [y]").getBytes());
                            break;
                        }
                        case "/pick": {
                            int x = Integer.parseInt(message.split(" ", 2)[0]);
                            int y = Integer.parseInt(message.split(" ", 2)[1]);
                            String board = minesweeper.pick(x - 1, y - 1);
                            out.write(("/board " + x + " " + y + "\n" + board + "\n" +
                                       "Picked position (" + x + ", " + y + ")\n\n").getBytes());
                            break;
                        }
                        default:
                            throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Client incorrect command");
                    out.write("Server: Incorrect command, use /help to see the server commands.\n".getBytes());
                } catch (ArrayIndexOutOfBoundsException a) {
                    System.out.println("Client incorrect command");
                    out.write("Missing arguments!".getBytes());
                }
            }

            in.close();
            out.close();
            broadcaster.remove(user);
            mailbox.set(new Message(new User("", null), username + " left."));
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
