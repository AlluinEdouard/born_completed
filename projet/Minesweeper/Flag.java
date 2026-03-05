import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;

public class Flag implements Button{

    /* Attributes */
    private boolean state = false;
    
    /* Builders */
    public Flag() {
        this.state = false;
    }

    public Flag(boolean state) {
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
         * If the button is activated, we display a "D" character, else we display a space
         */
        if (this.state) {
            System.out.print("D");
        } else {
            System.out.print(" ");
        }
    }

    @Override
    public void actionButton(Tile c, Board board) {
        /**
         * If the button is activated, we switch the flag
         */
        if (this.state) {
            c.switchFlag(board);
        }
    }

    @Override
    public Texture selection(int sizeTile, int width, int height) {
        /**
         * We create a square with the color blue, the position and the size of the tile
         */
        if (this.state) {
            return new Texture("./img/Minesweeper_flag.png", new Point(width - 3*sizeTile,height-2*sizeTile), sizeTile, sizeTile);
        } else {
            return new Texture("./img/Minesweeper_flag.png", new Point(width - 3*sizeTile,height-2*sizeTile), sizeTile, sizeTile);
        }    
    }
}
