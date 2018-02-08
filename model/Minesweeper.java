package model;

public class Minesweeper {
    private RealBoard board;

    public Minesweeper(int size, int bombs) {
        board = new RealBoard(size, bombs);
    }

    public String pick(int x, int y) {
        Point p = new Point(x, y);
        if (board.isVisible(p)) {
            return "ILLEGAL MOVE, pick something new pls";
        }
        if (board.isBomb(p)) {
            return board + "\nYOU LOSE #NICETRYBRO";

        }
        board.pick(p);
        
        if (board.gameIsBeat()) {
            return "YOU WIN U ARE AMAAAAZING. WOOOOOOOOW. really. you did it.";
        }
        return board.makeHiddenBoard().toString();

    }

    public int[][] flag(int x, int y) {
        Point p = new Point(x, y);
        board.setFlag(p);
        return board.makeHiddenBoard().readable();
    }

    public int[][] getUserBoard() {
        return board.makeHiddenBoard().readable();
    }

    public String toString() {
        return board.makeHiddenBoard().toString();
    }
}