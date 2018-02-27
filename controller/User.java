package controller;

import java.net.Socket;

import model.Minesweeper;

public class User {

    public final String username;
    public final Socket socket;
    private Minesweeper minesweeper;
    private User opponent;

    public User(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    public User setOpponent(User opponent) {
        this.opponent = opponent;
        return this;
    }

    public boolean hasOpponent() {
        return opponent != null;
    }

    public User getOpponent() {
        return opponent;
    }

    public User setMinesweeper(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        return this;
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
