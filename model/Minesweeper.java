package model;

public class Minesweeper {
    private RealBoard board;
    private BlitzTimer timer;

    public Minesweeper(int size, int bombs, String host) {
        board = new RealBoard(size, bombs);
        timer = new BlitzTimer(host);
    }

    public String pick(int x, int y) {
        if (!timer.isStarted()) {
            timer.start();
        }

        Point p = new Point(x, y);
        if (board.isVisible(p)) {
            return null;
        } else if (board.isBomb(p)) {
            return board + "\nYOU LOSE #NICETRYBRO";

        }
        board.pick(p);

        if (board.gameIsBeat()) {
            return board + "\nYOU WIN U ARE AMAAAAZING. WOOOOOOOOW. really. you did it.";
        }
        timer.switchPlayer();
        return board.makeHiddenBoard().toString();
    }

    public boolean didILose(String name) {
        if (!playerTurn(name) && board.gameIsBeat()) {
            return true;
        }
        return playerTime(name) == 0;
    }

    public int[][] getUserBoard() {
        return board.makeHiddenBoard().readable();
    }

    public int getSize() {
        return board.getSize();
    }

    public int getBombs() {
        return board.getBombs();
    }

    public String toString() {
        return board.makeHiddenBoard().toString();
    }

    public void initChallenge(String guestPlayer, int time) {
        timer.addPlayer(guestPlayer);
        timer.setTime(time);
    }

    public boolean playerTurn(String username) {
        return timer.isPlayersTurn(username);
    }

    public int playerTime(String username) {
        return timer.timeLeft(username);
    }

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper(10, 10, "JOHAN");
        ms.initChallenge("SVEN", 10);
        if (ms.playerTurn("JOHAN")) {
            ms.pick(1, 1);
        }
        if (ms.playerTurn("JOHAN")) {
            ms.pick(1, 2);
        }
        if (ms.playerTurn("SVEN")) {
            ms.pick(1, 3);
        }
    }
}
