import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texture;

public class Empty implements Tile {
    /* Attributes */
    private boolean masked;
    private boolean flag;
    private int x;
    private int y;
    private int nbNeighbours;

    /* Builders */
    public Empty() {
        this.masked = true;
        this.flag = false;
        this.x = 0;
        this.y = 0;
    }

    public Empty(int x, int y) {
        this.masked = true;
        this.flag = false;
        this.x = x;
        this.y = y;
    }

    public Empty(int x, int y, boolean masked, boolean flag) {
        this.x = x;
        this.y = y;
        this.masked = masked;
        this.flag = flag;
    }

    /*
     * Getters
     */
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

    public int getNbNeighbours() {
        return this.nbNeighbours;
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

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Methods */
    public void discover(Board board) {
        this.masked = false;
        board.addDiscoveredTile(this);
        if (this.nbNeighbours == 0) {
            int x = this.x;
            int y = this.y;
            // up right
            if ((-1 < x + 1) && (x + 1 < board.getWidth()) && (-1 < y + 1) && (y + 1 < board.getHeight())) {
                Tile neighbour = board.getCase(x + 1, y + 1);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // right
            if ((-1 < x + 1) && (x + 1 < board.getWidth()) && (-1 < y) && (y < board.getHeight())) {
                Tile neighbour = board.getCase(x + 1, y);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // down right
            if ((-1 < x + 1) && (x + 1 < board.getWidth()) && (-1 < y - 1) && (y - 1 < board.getHeight())) {
                Tile neighbour = board.getCase(x + 1, y - 1);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // down
            if ((-1 < x) && (x < board.getWidth()) && (-1 < y - 1) && (y - 1 < board.getHeight())) {
                Tile neighbour = board.getCase(x, y - 1);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // down left
            if ((-1 < x - 1) && (x - 1 < board.getWidth()) && (-1 < y - 1) && (y - 1 < board.getHeight())) {
                Tile neighbour = board.getCase(x - 1, y - 1);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // left
            if ((-1 < x - 1) && (x - 1 < board.getWidth()) && (-1 < y) && (y < board.getHeight())) {
                Tile neighbour = board.getCase(x - 1, y);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // up left
            if ((-1 < x - 1) && (x - 1 < board.getWidth()) && (-1 < y + 1) && (y + 1 < board.getHeight())) {
                Tile neighbour = board.getCase(x - 1, y + 1);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }

            // up
            if ((-1 < x) && (x < board.getWidth()) && (-1 < y + 1) && (y + 1 < board.getHeight())) {
                Tile neighbour = board.getCase(x, y + 1);
                if (neighbour.getMasked() && !neighbour.getFlag()) {
                    neighbour.discover(board);
                }
            }
        }
    }

    public void switchFlag(Board board) {
        this.flag = !this.flag;
        board.addDiscoveredTile(this);
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
            String path = "./img/Minesweeper_";
            path += this.nbNeighbours;
            path += ".png";
            return new Texture(path, p, sizeTile, sizeTile);
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
            System.out.print("  ");
        }
    }

    @Override
    public int neighbour(Board plato) {
        int nbN = 0;
        int x = this.x;
        int y = this.y;
        // up right
        if ((-1 < x + 1) && (x + 1 < plato.getWidth()) && (-1 < y + 1) && (y + 1 < plato.getHeight())) {
            Tile neighbour = plato.getCase(x + 1, y + 1);
            nbN += neighbour.addNeighbour();
        }

        // right
        if ((-1 < x + 1) && (x + 1 < plato.getWidth()) && (-1 < y) && (y < plato.getHeight())) {
            Tile neighbour = plato.getCase(x + 1, y);
            nbN += neighbour.addNeighbour();
        }

        // down right
        if ((-1 < x + 1) && (x + 1 < plato.getWidth()) && (-1 < y - 1) && (y - 1 < plato.getHeight())) {
            Tile neighbour = plato.getCase(x + 1, y - 1);
            nbN += neighbour.addNeighbour();
        }

        // down
        if ((-1 < x) && (x < plato.getWidth()) && (-1 < y - 1) && (y - 1 < plato.getHeight())) {
            Tile neighbour = plato.getCase(x, y - 1);
            nbN += neighbour.addNeighbour();
        }

        // down left
        if ((-1 < x - 1) && (x - 1 < plato.getWidth()) && (-1 < y - 1) && (y - 1 < plato.getHeight())) {
            Tile neighbour = plato.getCase(x - 1, y - 1);
            nbN += neighbour.addNeighbour();
        }

        // left
        if ((-1 < x - 1) && (x - 1 < plato.getWidth()) && (-1 < y) && (y < plato.getHeight())) {
            Tile neighbour = plato.getCase(x - 1, y);
            nbN += neighbour.addNeighbour();
        }

        // up left
        if ((-1 < x - 1) && (x - 1 < plato.getWidth()) && (-1 < y + 1) && (y + 1 < plato.getHeight())) {
            Tile neighbour = plato.getCase(x - 1, y + 1);
            nbN += neighbour.addNeighbour();
        }

        // up
        if ((-1 < x) && (x < plato.getWidth()) && (-1 < y + 1) && (y + 1 < plato.getHeight())) {
            Tile neighbour = plato.getCase(x, y + 1);
            nbN += neighbour.addNeighbour();
        }
        this.nbNeighbours = nbN;
        return nbN;
    }

    @Override
    public int addNeighbour() {
        return 0;
    }

    @Override
    public boolean endGameMine() {
        return false;
    }

    @Override
    public boolean endGameWin() {
        if (this.masked) {
            return false;
        } else {
            return true;
        }
    }
}
