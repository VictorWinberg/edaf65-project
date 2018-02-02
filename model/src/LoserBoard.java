public class LoserBoard implements Board {
    @Override
    public boolean isBomb(Point p) {
        return false;
    }

    @Override
    public void setFlag(Point p) {

    }

    @Override
    public void pick(Point p) {

    }

    @Override
    public Board makeHiddenBoard() {
        return null;
    }

    @Override
    public void turnUp(Point p) {

    }
}
