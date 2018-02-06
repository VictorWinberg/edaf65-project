package model;

import java.awt.*;

public class NumberSquare implements Square {
    private int number;
    private boolean visible;

    public NumberSquare(int number) {
        visible = false;
        this.number = number;
    }

    @Override
    public void makeVisible() {
        visible = true;
    }

    @Override
    public int check() {
        return number;
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
