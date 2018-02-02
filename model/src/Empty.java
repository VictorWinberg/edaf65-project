import java.awt.*;

public class Empty implements Square {
    boolean visible;

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
    public Color getColor() {
        return null;
    }
}
