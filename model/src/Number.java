import java.awt.*;

public class Number implements Square {
    private int number;
    private boolean visible;

    public Number(int number) {
        visible = false;
        this.number = number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int pick() {
        visible = true;
        return number;
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
