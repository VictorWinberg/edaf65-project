
public class Empty implements Square{
	boolean visible;
	public Empty(){
		visible=false;
	}
	@Override
	public int pick() {
		visible=true;
		return 0;
	}
	@Override
	public int check() {
		return 0;
	}
	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return visible;
	}
}
