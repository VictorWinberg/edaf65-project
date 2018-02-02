
public class Bomb implements Square {
	boolean visible;
	int x;
	int y;
	public Bomb(){
		
		visible=false;
	}
	
	
	@Override
	public int pick() {
		visible=true;
		return -1;
	}

	@Override
	public int check() {
		return -1;
	}


	@Override
	public boolean isVisible() {
		return visible;
	}


	@Override
	public int getXPosition() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getYPosition() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setXY(int x, int y) {
		this.x=x;
		this.y=y;
		
	}

}
