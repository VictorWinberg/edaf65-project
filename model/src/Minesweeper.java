public class Minesweeper {
    private RealBoard board;

    public Minesweeper() {
        board = new RealBoard(5);
    }

    public Board pick(int x, int y) {
        if (board.isBomb(x, y)) {
            return new LoserBoard(x,y);
        }
        board.turnUp(x,y);
        return board.makeHiddenBoard();
    }

    public Board flag(int x, int y) {
        board.setFlag(x, y);
        return board.makeHiddenBoard();
    }

    public Board getInitialBoard(){
        return board.makeHiddenBoard();
    }
}
