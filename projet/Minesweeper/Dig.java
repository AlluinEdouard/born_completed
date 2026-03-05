import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;

public class Dig implements Button {

    /* Attributes */
    private boolean state = false;

    /* Builders */
    public Dig() {
        this.state = false;
    }

    public Dig(boolean state) {
        this.state = state;
    }

    /* Getters */
    public boolean getState() {
        return this.state;
    }

    /* Setters */
    public void setState(boolean state) {
        this.state = state;
    }

    /* Methods */
    @Override
    public void display() {
        /**
         * If the button is activated, we display a "C" character, else we display a
         * space
         */
        if (this.state) {
            System.out.print("C");
        } else {
            System.out.print(" ");
        }
    }

    @Override
    public void actionButton(Tile c, Board board) {
        /**
         * If the button is activated, we discover the tile
         */
        if (this.state) {
            c.discover(board);
        }
    }

    @Override
    public Texture selection(int sizeTile, int width, int height) {
        /**
         * We create a square with the color blue, the position and the size of the tile
         */
        if (this.state) {
            return new Texture("./img/Minesweeper_questionmark_true.png",
                    new Point(2 * sizeTile, height - 2 * sizeTile), sizeTile, sizeTile);
        } else {
            return new Texture("./img/Minesweeper_questionmark.png", new Point(2 * sizeTile, height - 2 * sizeTile),
                    sizeTile, sizeTile);
        }
    }
}
