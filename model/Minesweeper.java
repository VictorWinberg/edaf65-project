package model;

public class Minesweeper {
    private RealBoard board;

    public Minesweeper(int size, int bombs) {
        board = new RealBoard(size, bombs);
    }

    public String pick(int x, int y) {
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
        return board.makeHiddenBoard().toString();
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
}