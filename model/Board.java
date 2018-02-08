package model;

public interface Board {
    boolean isBomb(Point p);

    void setFlag(Point p);

    void pick(Point p);

    Board makeHiddenBoard();

    void print();
}
