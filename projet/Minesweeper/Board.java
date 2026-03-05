import java.util.ArrayList;

public class Board {
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private int width;
    private int height;
    private int nbBombs;
    private ArrayList<Tile> discoveredTiles = new ArrayList<Tile>();

    public Board(int width, int height, int nbBombs) {
        // if (width >= 6 && height >= 6 && nbBombs >= 1 && nbBombs < width * height) {
            this.width = width;
            this.height = height;
            this.nbBombs = nbBombs;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    this.tiles.add(new Empty(i, j));
                }
            }
            for (int i = 0; i < nbBombs; i++) {
                int x = (int) (Math.random() * width);
                int y = (int) (Math.random() * height);
                Tile c = this.getCase(x, y);
                this.tiles.remove(c);
                this.tiles.add(new Bomb(x, y));
            }
            this.neighbourhood();
        // } else {
            // throw new IllegalArgumentException("Invalid input values for the plateau.");
        // }
    }

    /* Getters */
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getNbBombs() {
        return this.nbBombs;
    }

    public ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    public ArrayList<Tile> getDiscoveredTiles() {
        return this.discoveredTiles;
    }

    public void addDiscoveredTile(Tile c) {
        if (!this.discoveredTiles.contains(c)) {
            this.discoveredTiles.add(c);
        }
    }

    public void clearDiscoveredTiles() {
        this.discoveredTiles.clear();
    }

    public Tile getCase(int x, int y) {
        for (Tile c : this.tiles) {
            if (c.getX() == x && c.getY() == y) {
                return c;
            }
        }
        return null;
    }

    public void display() {
        for (int j = this.height; j > -1; j--) {
            for (int i = 0; i < this.width; i++) {
                for (Tile c : this.tiles) {
                    if (c.getX() == i && c.getY() == j) {
                        c.display();
                    }
                }
            }
            // System.out.println();
        }
    }

    public void action(int x, int y, Button b, int sizeTile) {
        int i = x / sizeTile;
        int j = y / sizeTile;
        System.out.println("Action on tile: " + i + ", " + j);
        System.out.println("Action on tile: " + x + ", " + y);
        for (Tile c : this.tiles) {
            if (c.getX() == i && c.getY() == j) {
                b.actionButton(c, this);
                this.addDiscoveredTile(c);
            }
        }
    }

    public void neighbourhood() {
        for (Tile c : this.tiles) {
            c.neighbour(this);
        }
    }

    public boolean endGameMine() {
        boolean end = false;
        int i = 0;
        while (end == false && i < this.tiles.size()) {
            Tile c = this.tiles.get(i);
            end = c.endGameMine();
            i++;
        }
        return end;
    }

    public boolean endGameWin() {
        boolean end = true;
        int i = 0;
        while (end == true && i < this.tiles.size()) {
            Tile c = this.tiles.get(i);
            end = c.endGameWin();
            i++;
        }
        return end;
    }
}
