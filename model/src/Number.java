
public class Number implements Square {
	int number;
	boolean visible;
	int x;
	int y;

	public Number(int number) {
		visible = false;
		this.number=number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int pick() {
		visible = true;
		return number;
	}

	@Override
	public int check() {
		return number;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public int getXPosition() {
	
		return x;
	}

	@Override
	public int getYPosition() {
		return y;
	}

	@Override
	public void setXY(int x, int y) {
		this.x=x;
		this.y=y;
		
	}

}
