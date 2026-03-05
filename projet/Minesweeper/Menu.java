import MG2D.Couleur;
import MG2D.Fenetre;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texture;

public class Menu {

    private int x;
    private int y;
    private int pos = 0;

    Menu() {
        // Constructor

    }

    public void display(Fenetre window, int sizeTile, int width, int height, Rectangle cursorMenuTexture) {
        // window.ajouter(new Rectangle(Couleur.BLEU, new Point(0, 0), width, height,
        // true));
        window.ajouter(new Texture("./img/menu_background.png", new Point(0, 0), width, height));
        window.ajouter(new Rectangle(Couleur.GRIS_CLAIR, new Point(337, 765), 600, 100,
                true));
        window.ajouter(new Rectangle(Couleur.GRIS_CLAIR, new Point(437, 550), 400, 100,
                true));
        window.ajouter(new Rectangle(Couleur.GRIS_CLAIR, new Point(437, 400), 400, 100,
                true));
        window.ajouter(new Rectangle(Couleur.GRIS_CLAIR, new Point(437, 250), 400, 100,
                true));
        window.ajouter(new Rectangle(Couleur.GRIS_CLAIR, new Point(437, 100), 400, 100,
                true));

        window.ajouter(new Texture("./img/Minesweeper.png", new Point(337, 765), 600, 100));
        window.ajouter(cursorMenuTexture);
        window.ajouter(new Texture("./img/menu_play.png", new Point(437, 550), 400, 100));
        window.ajouter(new Texture("./img/menu_scores.png", new Point(437, 400), 400, 100));
        window.ajouter(new Texture("./img/menu_rules.png", new Point(437, 250), 400, 100));
        window.ajouter(new Texture("./img/menu_quit.png", new Point(437, 100), 400, 100));

        // window.ajouter(new Rectangle(Couleur.BLANC, new Point(437, 550), 400, 100,
        // false));
    }

    public int getPos() {
        return this.pos;
    }

    public void moveUpMenuCursor(MainGraphic mg) {

        if (this.pos > 0) {
            this.pos--;
            mg.cursorMenuTexture.translater(0, 150);
        }
    }

    public void moveDownMenuCursor(MainGraphic mg) {
        if (this.pos < 3) {
            this.pos++;
            mg.cursorMenuTexture.translater(0, -150);
        }
    }
}
