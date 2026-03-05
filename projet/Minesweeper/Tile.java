import MG2D.geometrie.Rectangle;

public interface Tile {
    public boolean masked = true;
    public boolean flag = false;

    public int getX();

    public int getY();

    public boolean getMasked();

    public boolean getFlag();

    public void setMasked(boolean masked);

    public void setFlag(boolean flag);

    public void discover(Board board);

    public void switchFlag(Board board);

    public Rectangle displayGraphic(int sizeTile);

    public void display();

    public int neighbour(Board board);

    public int addNeighbour();

    public boolean endGameMine();

    public boolean endGameWin();
}
