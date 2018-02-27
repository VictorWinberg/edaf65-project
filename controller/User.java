package controller;

import java.net.Socket;

import model.Minesweeper;

public class User {

    public final String username;
    public final Socket socket;
    private Minesweeper minesweeper;

    public User(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    public void setMinesweeper(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
    }

    public Minesweeper getMinesweeper() {
        return minesweeper;
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
