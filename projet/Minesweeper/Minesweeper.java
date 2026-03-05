import MG2D.audio.*;

public class Minesweeper {

    private static ClavierBorneArcade keyboard;

    // private Musique music;
    // Bruitage music;

    public static void main(String[] args) {
        MinesweeperView m = new MinesweeperView();
        keyboard = new ClavierBorneArcade();
        m.window.addKeyListener(keyboard);
        m.window.getP().addKeyListener(keyboard);

        m.mg.menu(m.window, m.level.getSizeTile(),
                m.level.getWidthWindow(), m.level.getHeightWindow());
        m.window.rafraichir();

        int menu = 1;

        Musique music2 = new Musique("./sounds/loading.mp3");
        music2.lecture();

        while (true) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO : Menu
            if (menu == 1) {
                // exit
                if (keyboard.getBoutonJ1XTape()) {
                    System.exit(0);
                }
                if (keyboard.getJoyJ1HautTape()) {
                    m.mg.moveUpMenuCursor();
                    m.window.rafraichir();
                }
                if (keyboard.getJoyJ1BasTape()) {
                    m.mg.moveDownMenuCursor();
                    m.window.rafraichir();
                }
                if (keyboard.getBoutonJ1CTape()) {
                    System.out.println("Menu : " + m.mg.menu.getPos());
                    if (m.mg.menu.getPos() == 0) {
                        menu = 2; // change to game
                        m.board = new Board(m.level.getWidth(), m.level.getHeight(),
                                m.level.getNbBombs());
                        m.board.neighbourhood();
                        m.button = new Dig(true);
                        m.cursor = new Cursor(Constants.sizeTile);
                        m.mg = new MainGraphic(m.window, m.board, m.button, m.level.getSizeTile(),
                                m.level.getWidthWindow(), m.level.getHeightWindow());
                        m.end = false;
                        m.window.rafraichir();

                        // m.mg.openAfterMenu(m.window, m.board, m.button, m.level.getSizeTile(),
                        // m.level.getWidthWindow(), m.level.getHeightWindow());
                        // m.window.rafraichir();
                    } else if (m.mg.menu.getPos() == 1) {
                        menu = 3; // change to scores
                        m.mg.openScore(m.window, m.level.getWidthWindow(), m.level.getHeightWindow());
                        m.window.rafraichir();
                    } else if (m.mg.menu.getPos() == 2) {
                        menu = 4; // change to rules
                        m.mg.openRules(m.window, m.level.getWidthWindow(), m.level.getHeightWindow());
                        m.window.rafraichir();
                    } else if (m.mg.menu.getPos() == 3) {
                        System.exit(0);
                    }
                }
            } else if (menu == 2) {
                // Game
                if (!m.end) {
                    if (keyboard.getBoutonJ1XTape()) {
                        menu = 1;
                        m.mg.menu(m.window, m.level.getSizeTile(),
                                m.level.getWidthWindow(), m.level.getHeightWindow());
                        m.window.rafraichir();
                    }
                    // change to dig button
                    if (keyboard.getBoutonJ1ATape()) {
                        if (m.button instanceof Flag) {
                            m.button = new Dig(true);
                            m.mg.changeButton(m.button);
                            m.window.rafraichir();
                        }
                    }
                    // change to flag button
                    if (keyboard.getBoutonJ1BTape()) {
                        if (m.button instanceof Dig) {
                            m.button = new Flag(true);
                            m.mg.changeButton(m.button);
                            m.window.rafraichir();
                        }
                    }
                    // move
                    if (keyboard.getJoyJ1HautTape()) {
                        if (m.cursor.getY() < Constants.sizeTile * (Constants.height - 1)) {
                            m.cursor.moveUp();
                            m.mg.moveCursor(0, Constants.sizeTile);
                        }
                        m.window.rafraichir();
                    }
                    if (keyboard.getJoyJ2BasTape()) {
                        if (m.cursor.getY() > 0) {
                            m.cursor.moveDown();
                            m.mg.moveCursor(0, -Constants.sizeTile);
                        }
                        m.window.rafraichir();
                    }
                    if (keyboard.getJoyJ1GaucheTape()) {
                        if (m.cursor.getX() > 0) {
                            m.cursor.moveLeft();
                            m.mg.moveCursor(-Constants.sizeTile, 0);
                        }
                        m.window.rafraichir();
                    }
                    if (keyboard.getJoyJ1DroiteTape()) {
                        if (m.cursor.getX() < Constants.sizeTile * (Constants.width - 1)) {
                            m.cursor.moveRight();
                            m.mg.moveCursor(Constants.sizeTile, 0);
                        }
                        m.window.rafraichir();
                    }
                    // Dig or flag
                    if (keyboard.getBoutonJ1CTape()) {
                        m.board.action(m.cursor.getX(), m.cursor.getY(), m.button, m.level.getSizeTile());
                        m.mg.update(m.window, m.board);
                        m.window.rafraichir();
                        m.end = m.board.endGameMine();
                        Bruitage b = new Bruitage("./sounds/select.mp3");
                        b.lecture();
                        if (m.end) {
                            m.mg.endOfTheGameMine(m.window, m.level.getSizeTile(),
                                    m.level.getWidthWindow(),
                                    m.level.getHeightWindow(), keyboard);
                        } else if (!m.end) {
                            m.end = m.board.endGameWin();
                            if (m.end) {
                                m.mg.endOfTheGameWin(m.window, m.level.getSizeTile(),
                                        m.level.getWidthWindow(),
                                        m.level.getHeightWindow(), keyboard);
                            }
                        }
                        m.window.rafraichir();
                    }
                } else {
                    // End of the game
                    // TODO score / replay
                    if (keyboard.getBoutonJ1CTape()) {
                        m.board = new Board(m.level.getWidth(), m.level.getHeight(),
                                m.level.getNbBombs());
                        m.board.neighbourhood();
                        m.button = new Dig(true);
                        m.cursor = new Cursor(Constants.sizeTile);
                        m.mg = new MainGraphic(m.window, m.board, m.button, m.level.getSizeTile(),
                                m.level.getWidthWindow(), m.level.getHeightWindow());
                        m.end = false;
                        m.window.rafraichir();
                    }
                    if (keyboard.getBoutonJ1XTape()) {
                        System.exit(0);
                    }
                }
            } else if (menu == 3) {
                // Back to menu
                if (keyboard.getBoutonJ1XTape()) {
                    menu = 1;
                    m.mg.menu(m.window, m.level.getSizeTile(),
                            m.level.getWidthWindow(), m.level.getHeightWindow());
                    m.window.rafraichir();
                }
            } else if (menu == 4) {
                // Back to menu
                if (keyboard.getBoutonJ1XTape()) {
                    menu = 1;
                    m.mg.menu(m.window, m.level.getSizeTile(),
                            m.level.getWidthWindow(), m.level.getHeightWindow());
                    m.window.rafraichir();
                }
            }
        }
    }
}