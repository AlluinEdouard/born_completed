public class Easy implements Level {

    private int width = ConstanteEasy.widthEasy;
    private int height = ConstanteEasy.heightEasy;
    private int nbBombs = ConstanteEasy.nbBombsEasy;
    private int sizeTile = ConstanteEasy.sizeTileEasy;
    private int widthWindow = ConstanteEasy.widthWindowEasy;
    private int heightWindow = ConstanteEasy.heightWindowEasy;

    public Easy() {
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
