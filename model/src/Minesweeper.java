import javax.swing.text.html.MinimalHTMLWriter;

public class Minesweeper {
    private RealBoard board;

    public Minesweeper() {
        board = new RealBoard(9);
        board.placeRandomBombs(10);
    }

    public Board pick(int x, int y) {
        if (board.isBomb(x, y)) {
            return new LoserBoard(x,y);
        }
        board.pick(x,y);
        return board.makeHiddenBoard();
    }

    public Board flag(int x, int y) {
        board.setFlag(x, y);
        return board.makeHiddenBoard();
    }

    public Board getInitialBoard(){
        return board.makeHiddenBoard();
    }

    public static void main(String[] args){
        Minesweeper m = new Minesweeper();
    }
}