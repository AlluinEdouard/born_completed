import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texture;

public class Bomb implements Tile {
    /* Attributes */
    private boolean masked;
    private boolean flag;
    private int x;
    private int y;

    /* Builders */
    public Bomb() {
        this.masked = true;
        this.flag = false;
        this.x = 0;
        this.y = 0;
    }

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        this.masked = true;
        this.flag = false;
    }

    public Bomb(int x, int y, boolean masked, boolean flag) {
        this.x = x;
        this.y = y;
        this.masked = masked;
        this.flag = flag;
    }

    /* Getters */
    public boolean getMasked() {
        return this.masked;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /* Setters */
    public void setMasked(boolean masked) {
        this.masked = masked;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /* Methods */
    @Override
    public void discover(Board board) {
        this.masked = false;
    }

    @Override
    public void switchFlag(Board board) {
        this.flag = !this.flag;
    }

    @Override
    public Rectangle displayGraphic(int sizeTile) {
        Point p = new Point(this.x * sizeTile, this.y * sizeTile);
        if (this.masked) {
            if (this.flag) {
                return new Texture("./img/Minesweeper_flag.png", p, sizeTile, sizeTile);
            } else {
                return new Texture("./img/Minesweeper_unopened_square.png", p, sizeTile, sizeTile);
            }
        } else {
            return new Texture("./img/Minesweeper_bomb.png", p, sizeTile, sizeTile);
        }
    }

    @Override
    public void display() {
        if (this.masked) {
            if (this.flag) {
                System.out.print("P ");
            } else {
                System.out.print("X ");
            }
        } else {
            System.out.print("B ");
        }
    }

    @Override
    public int neighbour(Board board) {
        // a bomb has no neighbours
        return 0;
    }

    @Override
    public int addNeighbour() {
        return 1;
    }

    @Override
    public boolean endGameMine() {
        if (!this.masked) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean endGameWin() {
        if (this.masked) {
            return true;
        } else {
            return false;
        }
    }
}
