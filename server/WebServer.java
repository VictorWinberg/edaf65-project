package server;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;

import java.net.*;
import java.util.*;

public class WebServer extends WebSocketServer {

    private Set<User> users;

    public WebServer(int port) {
        super(new InetSocketAddress(port));
        users = new HashSet<>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Server: Welcome! Please enter your username:");
        System.out.println("WebClient connection: " + conn.getRemoteSocketAddress().getAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        users.remove(new User(null, conn));
        System.out.println("WebClient disconnected " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String input) {
        User user = findUser(conn);
        if (user == null) {
            if (input.matches("^[a-zA-Z0-9_]{3,14}$")) {
                conn.send("Server: Hi " + input + " use /help to see the server commands.");
                users.add(new User(input, conn));
            } else {
                conn.send("Server: Please only use alphanumeric values with underscore between 3 and 14 values.");
            }
            return;
        }
        input += " ";
        String command = input.split(" ", 2)[0].trim();
        String message = input.split(" ", 2)[1].trim();
        System.out.println("Client: " + message);
        switch (command) {
            case "/help":
                conn.send("Server: Available commands: /help, /all, /echo, /quit");
                break;
            case "/all":
                for (User other : users) {
                    other.webSocket.send(user.username + ": " + message);
                }
                break;
            case "/echo":
                conn.send("Server: " + message);
                break;
            case "/quit":
                users.remove(new User(null, conn));
                break;
            default:
                conn.send("Server: Incorrect command, use /help to see the server commands.");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception e) {
        e.printStackTrace();
        if (conn != null) {
            users.remove(new User(null, conn));
        }
    }

    private User findUser(WebSocket conn) {
        if (users.contains(new User(null, conn))) {
            for (User user : users) {
                if (user.webSocket == conn) {
                    return user;
                }
            }
        }
        return null;
    }
}