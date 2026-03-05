import MG2D.FenetrePleinEcran;
import MG2D.Souris;
import MG2D.audio.Musique;

public class MinesweeperView {

    public Level level;
    public FenetrePleinEcran window;
    public Board board;
    public Button button;
    public Souris mouse;
    public MainGraphic mg;
    public boolean end;
    public boolean menu;
    public Cursor cursor;
    public static KeyboardArcade keyboard;

    public MinesweeperView() {
        MinesweeperView.keyboard = new KeyboardArcade();
        this.level = new Basic();
        this.window = new FenetrePleinEcran("Minesweeper");
        this.window.addKeyListener(keyboard);
        // this.board = new Board(level.getWidth(), level.getHeight(), level.getNbBombs());
	this.board = new Board(0, 0, 0);
        this.button = new Dig(true);
        this.mouse = window.getSouris();
        this.cursor = new Cursor(level.getSizeTile());
        this.mg = new MainGraphic(window, board, button, level.getSizeTile(), level.getWidthWindow(),
                level.getHeightWindow());
        this.end = false;
        this.menu = false;

    }

    public MinesweeperView(Level level) {
        MinesweeperView.keyboard = new KeyboardArcade();
        this.level = level;
        this.window = new FenetrePleinEcran("Minesweeper");
        this.window.addKeyListener(keyboard);
        this.board = new Board(level.getWidth(), level.getHeight(), level.getNbBombs());
        this.button = new Dig(true);
        this.mouse = window.getSouris();
        this.cursor = new Cursor(level.getSizeTile());
        this.mg = new MainGraphic(window, board, button, level.getSizeTile(), level.getWidthWindow(),
                level.getHeightWindow());
        this.end = false;
        this.menu = false;
    }

    public void newGame() {
        // Minesweeper.keyboard = new KeyboardArcade();
        this.level = new Basic();
        this.window = new FenetrePleinEcran("Minesweeper");
        this.window.addKeyListener(keyboard);
        this.board = new Board(level.getWidth(), level.getHeight(), level.getNbBombs());
        this.button = new Dig(true);
        this.mouse = window.getSouris();
        this.cursor = new Cursor(level.getSizeTile());
        this.mg = new MainGraphic(window, board, button, level.getSizeTile(), level.getWidthWindow(),
                level.getHeightWindow());
        this.end = false;
        this.menu = false;
    }
}