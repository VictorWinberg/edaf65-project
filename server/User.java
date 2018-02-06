package server;

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
            User other = (User)obj;
            return username == other.username;
        }
        return false;
    }
}
