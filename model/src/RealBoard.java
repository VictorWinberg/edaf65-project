public class RealBoard implements Board {
	Square[][] field;
	int size;

	public RealBoard(int size) {
		field = new Square[size][size];
		this.size = size;
	}

	public Board makeBoard(Square[][] squares) {
		RealBoard newBoard = new RealBoard(size);
		newBoard.field=squares;
		return newBoard;
	}

	@Override
	public boolean isBomb(int x, int y) {
		return (field[x][y].check() == -1);
	}

	@Override
	public void setFlag(int x, int y) {

	}

	@Override
	public void turnUp(int x, int y) {
		field[x][y].pick();
	}

	public Board makeHiddenBoard() {
		RealBoard hidden = new RealBoard(size);
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
		return hidden.makeBoard(list);
	}
}
