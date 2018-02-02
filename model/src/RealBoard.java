import java.util.ArrayList;
import java.util.List;

public class RealBoard implements Board {
    Square[][] field;
    int size;
    int bombs;

    public RealBoard(int size, int bombs) {
        field = new Square[size][size];
        this.size = size;
        this.bombs = bombs;
        fillAndAddBombs(bombs);
    }

    private void fillAndAddBombs(int bombs) {
        int x, y;
        while (bombs > 0) {
            x = (int) (Math.random() * size);
            y = (int) (Math.random() * size);
            if (field[x][y] == null) {
                field[x][y] = new Bomb();
                bombs--;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = new Number(countNeighbourBombs(i, j));
            }
        }
    }

    private int countNeighbourBombs(int x, int y) {
        List<Integer> neigbours = new ArrayList<>();

        return 0;
    }


    @Override
    public boolean isBomb(int x, int y) {
        return (field[x][y].check() == -1);
    }

    @Override
    public void setFlag(int x, int y) {

    }

    @Override
    public void pick(int x, int y) {
        field[x][y].pick();
    }

    @Override
    public void turnUp(int x, int y) {
        field[x][y].pick();
    }

}
