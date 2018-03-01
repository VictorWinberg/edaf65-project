package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class BlitzTimer {
    private String host;
    private String guest;
    private String currentPlayer;
    private int hostTime;
    private int guestTime;
    private boolean isStarted;
    private String winner;

    public BlitzTimer(String host) {
        isStarted = false;
        this.host = host;
        currentPlayer = host;
        winner = null;
    }

    public void start() {
        isStarted = true;
        run();
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals(host) ? guest : host;
    }

    public void setTime(int initalTime) {
        guestTime = initalTime;
        hostTime = initalTime;
    }

    public void addPlayer(String challengedPlayer) {
        guest = challengedPlayer;
    }

    public boolean isPlayersTurn(String username) {
        return currentPlayer.equals(username);
    }

    public int timeLeft(String username) {
        return username.equals(host) ? hostTime : guestTime;
    }

    private void run() {
        Timer timer = new Timer(1000, (e -> {
            if (hostTime == 0) {
                winner = guest;
                return;
            }
            if (guestTime == 0) {
                winner = host;
                return;
            }
            if (host.equals(currentPlayer)) {
                hostTime--;
            } else {
                guestTime--;
            }
            System.out.println(host + " time is: " + hostTime);
            System.out.println(guest + " time is: " + guestTime);
        }));
        timer.start();
    }

    public Optional<String> getWinnerIfExists() {
        return Optional.of(winner);
    }

    public void currentPlayerLost() {
        winner = currentPlayer.equalsIgnoreCase(host) ? guest : host;
    }

    public void currentPlayerWon() {
        winner = currentPlayer.equalsIgnoreCase(host) ? host : guest;
    }
}