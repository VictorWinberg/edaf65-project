package controller;

import java.io.*;
import java.net.*;
import java.util.*;
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
            String input = in.readLine();
            if (input == null) {
                socket.close();
            } else if (input.trim().matches("^[a-zA-Z0-9_]{3,14}$")) {
                out.write(("Server: Hi " + input.trim() + "\nHint: Use /help to see the server commands.\n").getBytes());
                return input.trim();
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
                            out.write(("Server: Available commands: /help, /all, /echo, /whisper [user], /show, /play [size] [bombs] <user>, /quit\n").getBytes());
                            break;
                        case "/a":
                        case "/all":
                            mailbox.set(new Message(user, message));
                            break;
                        case "/e":
                        case "/echo":
                            out.write((message + "\n").getBytes());
                            break;
                        case "/w":
                        case "/whisper":
                            String to_username = message.split(" ", 2)[0].trim();
                            message = message.split(" ", 2)[1].trim();
                            Optional<User> to = broadcaster.getUsers().stream()
                                    .filter(u -> u.username.equals(to_username)).findFirst();
                            if (to.isPresent()) {
                                out.write(("To " + to_username + ": " + message + "\n").getBytes());
                                to.get().socket.getOutputStream().write((username + " whispers: " + message + "\n").getBytes());
                            } else {
                                out.write(("Server: Could not whisper " + to_username + "\n").getBytes());
                            }
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
                            int size = Integer.parseInt(message.split(" ")[0]);
                            int bombs = Integer.parseInt(message.split(" ")[1]);
                            minesweeper = new Minesweeper(size, bombs, user.username);

                            if (message.split(" ").length > 2) {
                                String opponent_name = message.split(" ")[2];
                                Optional<User> opponent = broadcaster.getUsers().stream()
                                        .filter(u -> u.username.equals(opponent_name)).findFirst();
                                if (opponent.isPresent()) {
                                    minesweeper.initChallenge(opponent.get().username, 60);
                                    user.setOpponent(opponent.get()).setMinesweeper(minesweeper);
                                    broadcaster.update(opponent.get().setOpponent(user).setMinesweeper(minesweeper));
                                    out.write(("Waiting for " + opponent_name + " to accept.\n").getBytes());
                                    opponent.get().socket.getOutputStream()
                                            .write((username + " challenged you for a DUAL! \nPlease write /accept.\n").getBytes());
                                } else {
                                    out.write(("Server: Could not find " + opponent_name + "\n").getBytes());
                                }

                            } else {
                                user.setOpponent(null);
                                out.write(("/play " + size + " " + bombs + "\n" + minesweeper.toString() + "\n" +
                                        "Game started with size " + size + " and bombs " + bombs + "!\n" +
                                        "Commands: /pick [x] [y]").getBytes());
                            }
                            break;
                        }
                        case "/accept": {
                            Optional<User> user_update = broadcaster.getUsers().stream()
                                    .filter(u -> u.username.equals(username)).findFirst();
                            if (user_update.isPresent()) {
                                user = user_update.get();
                                minesweeper = user.getMinesweeper();

                                int size = minesweeper.getSize();
                                int bombs = minesweeper.getBombs();
                                String text = "/play " + size + " " + bombs + "\n" + minesweeper.toString() + "\n" +
                                        "Game started with size " + size + " and bombs " + bombs + "!\n" +
                                        "Commands: /pick [x] [y]\n";
                                out.write(text.getBytes());
                                user.getOpponent().socket.getOutputStream().write(text.getBytes());
                            }
                            break;
                        }
                        case "/pick": {
                            if (minesweeper.playerTurn(username)) {
                                if (minesweeper.playerTime(username) == 0) {
                                    out.write("YOU LOSE BY TIME.".getBytes());
                                } else {
                                    int x = Integer.parseInt(message.split(" ", 2)[0]);
                                    int y = Integer.parseInt(message.split(" ", 2)[1]);
                                    String board = minesweeper.pick(x - 1, y - 1);
                                    if (board == null) {
                                        out.write("Illegal move! Please try again\n".getBytes());
                                    } else {
                                        String board_text = "/board " + x + " " + y + "\n" + board + "\n";
                                        String pick_text = "Picked position (" + x + ", " + y + ")\n";
                                        out.write(board_text.getBytes());
                                        if (user.hasOpponent()) {
                                            User opponent = user.getOpponent();
                                            OutputStream opp_out = opponent.socket.getOutputStream();
                                            opp_out.write(board_text.getBytes());

                                            out.write(("/time stop " + minesweeper.playerTime(username) + "\n").getBytes());
                                            opp_out.write(("/time start " + minesweeper.playerTime(opponent.username) + "\n").getBytes());
                                            opp_out.write(pick_text.getBytes());
                                            opp_out.flush();
                                        }
                                        out.write(pick_text.getBytes());
                                        out.flush();
                                    }
                                }
                            } else {
                                out.write("Not your turn! Chill ...\n".getBytes());
                                out.flush();
                            }
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
