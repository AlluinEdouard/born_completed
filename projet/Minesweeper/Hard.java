public class Hard implements Level {

    private int width = 22;
    private int height = 22;
    private int nbBombs = 100;
    private int sizeTile = 30;
    private int widthWindow = width * sizeTile;
    private int heightWindow = height * sizeTile + 3 * sizeTile;

    public Hard() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNbBombs() {
        return nbBombs;
    }

    public int getSizeTile() {
        return sizeTile;
    }

    public int getWidthWindow() {
        return widthWindow;
    }

    public int getHeightWindow() {
        return heightWindow;
    }

    public MinesweeperView onClick() {
        return new MinesweeperView(this);
    }
}
