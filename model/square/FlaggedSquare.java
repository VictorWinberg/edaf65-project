package model.square;

import java.awt.*;

public class FlaggedSquare implements Square {
    private boolean visible;

    public FlaggedSquare() {
        visible = false;
    }

    @Override
    public void makeVisible() {
        visible = true;
    }

    @Override
    public int check() {
        return -3;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public Color getColor() {
        return Color.darkGray;
    }
}
