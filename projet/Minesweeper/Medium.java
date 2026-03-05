public class Medium implements Level {

    private int width = 16;
    private int height = 16;
    private int nbBombs = 40;
    private int sizeTile = 30;
    private int widthWindow = width * sizeTile;
    private int heightWindow = height * sizeTile + 3 * sizeTile;

    public Medium() {
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

/**
 * créer une classe cosnstante avec toutes les valeurs
 * 
 * widthEasy = 7
 * heightEasy = 7
 * nbBombsEasy = 10
 * ....
 */