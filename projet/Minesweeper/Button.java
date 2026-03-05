import MG2D.geometrie.Texture;

public interface Button {
    public void display();
    public void actionButton(Tile c, Board board);
    public Texture selection(int sizeTile, int width, int height);
}
