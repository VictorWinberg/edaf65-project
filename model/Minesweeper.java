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

    public String flag(int x, int y) {
        Point p = new Point(x, y);
        board.setFlag(p);
        return board.makeHiddenBoard().toString();
    }

    public int[][] getUserBoard() {
        return board.makeHiddenBoard().readable();
    }

    public String toString() {
        return board.makeHiddenBoard().toString();
    }
}