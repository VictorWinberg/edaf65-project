
public class Hidden implements Square{
	int x;
	int y;
	@Override
	public int pick() {
		return -2;
	}

	@Override
	public int check() {
		return -2;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void setXY(int x, int y) {
		this.x=x;
		this.y=y;
		
	}

	@Override
	public int getXPosition() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int getYPosition() {
		// TODO Auto-generated method stub
		return y;
	}

}
