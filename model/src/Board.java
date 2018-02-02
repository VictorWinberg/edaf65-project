public interface Board {
    public boolean isBomb(int x, int y);
    public void setFlag(int x, int y);
    public void pick(int x, int y);
    public Board makeHiddenBoard();
}
