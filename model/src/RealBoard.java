import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealBoard implements Board {
    HashMap<Point, Square> field;
    int size;
    int bombs;

    public RealBoard(int size, int bombs) {
        field = new HashMap<>(size*size);
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
                field.put(new Point(i,j), new Number(countNeighbourBombs(i, j)));
            }
        }
    }

    private int countNeighbourBombs(int x, int y) {
        List<Point> neigbours = getNeighbourPoints();


        return 0;
    }


    @Override
    public boolean isBomb(Point p) {
        return (field.get(Point p).check() == -1);
    }

    @Override
    public void setFlag(Point p) {

    }

    @Override
    public void pick(Point p) {
        field[x][y].pick();
        List<Point> neighbours = getNeighbourPoints(x,y);
        for(Point nei : neighbours){
            if(field[nei] == 0){

            }
        }
    }

    @Override
    public void turnUp(int x, int y) {
        field[x][y].pick();
    }

    public Board makeHiddenBoard() {
        RealBoard hidden = new RealBoard(size, bombs);
        Square[][] list = new Square[size][size];
        Square temp;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (field[x][y].isVisible()) {
                    temp = field[x][y];
                } else {
                    temp = new Hidden();
                    temp.setXY(x, y);
                }
                list[x][y] = temp;
            }
        }
        hidden.field = list;
        return hidden;
    }
}
