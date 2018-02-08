package model;

public class Minesweeper {
    private RealBoard board;

    public Minesweeper(int size, int bombs) {
        board = new RealBoard(size, bombs);
    }

    public int[][] pick(int x, int y) {
        Point p = new Point(x, y);
        if(board.isVisible(p)){
            return null;
        }
        if (board.isBomb(p)) {
            return board.readable();
        }
        board.pick(p);
        return board.makeHiddenBoard().readable();
    }

    public int[][] flag(int x, int y) {
        Point p = new Point(x, y);
        board.setFlag(p);
        return board.makeHiddenBoard().readable();
    }

    public int[][] getUserBoard() {
        return board.makeHiddenBoard().readable();
    }

    public String toString(){
        return board.makeHiddenBoard().toString();
    }
}