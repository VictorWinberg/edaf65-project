public interface Board {
    public boolean isBomb(int x, int y);
    public void setFlag(int x, int y);
    void turnUp(int x, int y);
}
