import java.awt.*;

public class Hidden implements Square{
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
	public Color getColor() {
		return null;
	}

}
