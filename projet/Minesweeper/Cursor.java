import java.awt.Rectangle;

import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;

public class Cursor {
    private int x;
    private int y;

    private int sizeTile;

    public Cursor(int sizeTile) {
        this.x = 0;
        this.y = 0;
        this.sizeTile = sizeTile;
    }

    public Cursor(int x, int y, int sizeTile) {
        this.x = x;
        this.y = y;
        this.sizeTile = sizeTile;
    }

    public void moveUp() {
        if (y < Constants.sizeTile * (Constants.height - 1)) {
            y += sizeTile;
        }
    }

    public void moveDown() {
        if (y > 0) {
            y -= sizeTile;
        }
    }

    public void moveLeft() {
        if (x > 0) {
            x -= sizeTile;
        }
    }

    public void moveRight() {
        if (x < Constants.sizeTile * (Constants.width - 1)) {
            x += sizeTile;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSizeTile(int sizeTile) {
        this.sizeTile = sizeTile;
    }

    public int getSizeTile() {
        return sizeTile;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void resetPosition() {
        x = 0;
        y = 0;
    }

    public void resetPosition(Point p) {
        x = p.getX();
        y = p.getY();
    }

    public Rectangle cursorPlace() {
        return new Rectangle(x, y, sizeTile, sizeTile);
    }

    public static Texture cursorTexture() {
        return new Texture("./img/Minesweeper_cursor.png",
                new Point(0, 0), Constants.sizeTile, Constants.sizeTile);
    }

    public static Texture cursorTexture(int x, int y) {
        return new Texture("./img/Minesweeper_cursor.png",
                new Point(x, y), Constants.sizeTile, Constants.sizeTile);
    }

}
