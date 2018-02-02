
public class Empty implements Square {
	boolean visible;
	int x;
	int y;

	public Empty() {
		visible = false;
	}

	@Override
	public int pick() {
		visible = true;
		return 0;
	}

	@Override
	public int check() {
		return 0;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public int getXPosition() {

		return 0;
	}

	@Override
	public int getYPosition() {

		return 0;
	}

	@Override
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
