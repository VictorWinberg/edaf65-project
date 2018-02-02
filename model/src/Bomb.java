import java.awt.*;

public class Bomb implements Square {
    private boolean visible;
    public Bomb() {
        visible = false;
    }

    @Override
    public int pick() {
        visible = true;
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

    public Color getColor() {
        return null;
    }
}
