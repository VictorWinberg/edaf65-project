public interface Board {
    public boolean isBomb(Point p);
    public void setFlag(Point p);
    public void pick(Point p);
    public Board makeHiddenBoard();
    public void turnUp(Point p);

}
