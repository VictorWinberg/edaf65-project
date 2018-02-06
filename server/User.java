package server;

import java.net.Socket;
import org.java_websocket.WebSocket;

public class User {

    public final String username;
    public final Socket socket;
    public final WebSocket webSocket;

    public User(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
        this.webSocket = null;
    }

    public User(String username, WebSocket webSocket) {
        this.username = username;
        this.socket = null;
        this.webSocket = webSocket;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User)obj;
            if (socket != null) {
                return socket == other.socket;
            } else if (webSocket != null) {
                return webSocket == other.webSocket;
            }
            return username == other.username;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (socket != null) {
            return socket.hashCode();
        } else if (webSocket != null) {
            return webSocket.hashCode();
        }
        return username.hashCode();
    }

    @Override
    public String toString() {
        return username;
    }
}
