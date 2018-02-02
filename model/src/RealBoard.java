public class RealBoard implements Board {
    Square[][] field;

    public RealBoard(int size) {
        field = new Square[size][size];
    }

    @Override
    public boolean isBomb(int x, int y) {
        return false;
    }

    @Override
    public void setFlag(int x, int y) {

    }

    @Override
    public void turnUp(int x, int y) {

    }

    public Board makeHiddenBoard(){
        return null;
    }
}
