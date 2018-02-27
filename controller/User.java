package controller;

import java.net.Socket;

public class User {

    public final String username;
    public final Socket socket;

    public User(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            if (socket != null) {
                return socket == other.socket;
            }
            return username.equals(other.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (socket != null) {
            return socket.hashCode();
        }
        return username.hashCode();
    }

    @Override
    public String toString() {
        return username;
    }
}
