package model;

public class Minesweeper {
    private RealBoard board;

    public Minesweeper() {
        board = new RealBoard(9, 10);
    }

    public Board pick(int x, int y) {
        Point p = new Point(x,y);
        if (board.isBomb(p)) {
            return board;
        }
        board.pick(p);
        return board.makeHiddenBoard();
    }

    public Board flag(int x, int y) {
        Point p = new Point(x,y);
        board.setFlag(p);
        return board.makeHiddenBoard();
    }

    public Board getInitialBoard(){
        return board.makeHiddenBoard();
    }

    public static void main(String[] args){
        Minesweeper m = new Minesweeper();
    }
}