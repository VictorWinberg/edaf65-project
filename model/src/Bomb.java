
public class Bomb implements Square {
	boolean visible;
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

}
