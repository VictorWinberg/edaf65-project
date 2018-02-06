package model;

import java.awt.*;

public class HiddenSquare implements Square {
    @Override
    public void makeVisible() {
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
