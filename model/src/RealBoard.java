import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealBoard implements Board {
    private HashMap<Point, Square> field;
    private int size;
    private int bombs;

    public RealBoard(int size, int bombs) {
        field = new HashMap<>(size * size);
        this.size = size;
        this.bombs = bombs;
        fillAndAddBombs(bombs);
    }

    private void fillAndAddBombs(int bombs) {
        Point p;
        while (bombs > 0) {
            p = new Point((int) (Math.random() * size), (int) (Math.random() * size));
            if (field.get(p) == null) {
                field.put(p, new Bomb());
                bombs--;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                p = new Point(i, j);
                field.put(p, new Number(countNeighbourBombs(p)));
            }
        }
    }

    private int countNeighbourBombs(Point p) {
        List<Point> neigbours = getNeighbourPoints(p);
        int bombCount = 0;
        for (Point nei : neigbours) {
            if (field.get(nei).check() == -1) {
                bombCount++;
            }
        }
        return bombCount;
    }


    @Override
    public boolean isBomb(Point p) {
        return (field.get(p).check() == -1);
    }

    @Override
    public void setFlag(Point p) {

    }

    @Override
    public void pick(Point p) {
        field.get(p).pick();
        List<Point> neighbours = getNeighbourPoints(p);
        for (Point nei : neighbours) {
            if (field.get(nei).check() == 0) {
                pick(nei);
            }
        }
    }

    private List<Point> getNeighbourPoints(Point p) {
        List<Point> allNeighbours = p.getNeighbours();
        List<Point> realNeighBours = new ArrayList<>();
        for(Point nei : allNeighbours){
            if(p.insideSquare(size)){
                realNeighBours.add(p);
            }
        }
        return realNeighBours;
    }

    @Override
    public void turnUp(Point p) {
        field.get(p).pick();
    }

    public Board makeHiddenBoard() {
        RealBoard hidden = new RealBoard(size, bombs);
        HashMap<Point, Square> list = new HashMap<>();
        Point p;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                p = new Point(x, y);
                if (field.get(p).isVisible()) {
                    list.put(p, field.get(p));
                } else {
                    list.put(p, new Hidden());
                }
            }
        }
        hidden.field = list;
        return hidden;
    }
}
