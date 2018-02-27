package model;

public interface Board {
    boolean isBomb(Point p);

    void pick(Point p);

    Board makeHiddenBoard();

    String toString();

    boolean isVisible(Point p);

    int[][] readable();
}
