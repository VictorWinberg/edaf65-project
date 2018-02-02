import java.awt.*;

public class BombSquare implements Square {
    private boolean visible;

    public BombSquare() {
        visible = false;
    }

    @Override
    public void makeVisible() {
        visible = true;
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
