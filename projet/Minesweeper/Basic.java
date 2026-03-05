public class Basic implements Level {

    private int width = Constants.width;
    private int height = Constants.height;
    private int nbBombs = Constants.nbBombs;
    private int sizeTile = Constants.sizeTile;
    private int widthWindow = Constants.screenWidth;
    private int heightWindow = Constants.screenHeight;

    public Basic() {
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
