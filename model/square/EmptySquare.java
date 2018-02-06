package model.square;

import java.awt.*;

public class EmptySquare implements Square {
    private boolean visible;

    public EmptySquare() {
        visible = false;
    }

    @Override
    public void makeVisible() {
        visible = true;
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
