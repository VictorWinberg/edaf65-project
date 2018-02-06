package model;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(String.valueOf(x) + String.valueOf(y));
    }

    public List<Point> getNeighbours() {
        List<Point> neighbours = new ArrayList<>(8);
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (!(x == i && y == j)) {
                    neighbours.add(new Point(i, j));
                }
            }
        }
        return neighbours;
    }

    public boolean insideSquare(int size){
        return (x > 0 && x < size) && (y > 0 && y < size);
    }

    @Override
    public String toString() {
        return "X,Y (" + x + "," + y + ")";
    }
}
