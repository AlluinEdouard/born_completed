
import MG2D.FenetrePleinEcran;
import MG2D.Couleur;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texte;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

public class DinoRail {

    // Attributs //
    final static int largeur = 1275;
    final static int hauteur = 1020;

    //static Fenetre f = new Fenetre("DinoRail", largeur, hauteur);
    static FenetrePleinEcran f = new FenetrePleinEcran("fen");

    private static ClavierBorneArcade clavier;
    private static final Font calibri = new Font("Calibri", Font.TYPE1_FONT, 40);
    private static Texte gameover = new Texte(
            Couleur.ROUGE,
            "",
            calibri,
            f.getMilieu()
    );
    private static Texte stats = new Texte(
            Couleur.NOIR,
            "",
            calibri,
            new Point(f.getMilieu().getX(), f.getMilieu().getY() + 50)
    );

    private static boolean gameFinished = false;

    public static void main(String[] args) {
        int score = 0;

        f.setVisible(true);
        clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);
        f.getP().addKeyListener(clavier);

        long lastObstacleTime = System.currentTimeMillis();
        int minDelayObstacle = 850;
        long now;

        Rectangle sol = new Rectangle(Couleur.NOIR, new Point(0, 0), new Point(largeur, 150));
        sol.setPlein(true);
        Rectangle player = new Rectangle(Couleur.VERT, new Point(100, 150), new Point(200, 300));
        player.setPlein(true);
        //Animation player = new Animation("./assets/img/player-", "1", "4", "png", new Point(100, 150));

        boolean hasJump = false;
        boolean isAscended = false;
        int limitHeight = 550;

        ArrayList<Obstacle> listObstacle = new ArrayList<>();
        Iterator<Obstacle> it;

        listObstacle.add(new Obstacle(new Point(largeur, 150), new Point(largeur + 40, 200), "./assets/img/cactus.png"));

        f.ajouter(listObstacle.get(0));
        f.ajouter(sol);
        f.ajouter(player);
        f.ajouter(gameover);
        f.ajouter(stats);

        while (!gameFinished) {

            now = System.currentTimeMillis();

            if (now - lastObstacleTime >= minDelayObstacle + Math.random() * (3500 - minDelayObstacle)) {
                if (Math.random() > 0.3) {
                    listObstacle.add(new Obstacle(new Point(largeur, 150), new Point(largeur + 40, 200), "./assets/img/cactus.png"));
                } else {
                    listObstacle.add(new Obstacle(new Point(largeur, 250), new Point(largeur + 40, 300), "./assets/img/bird.png"));
                }

                f.ajouter(listObstacle.get(listObstacle.size() - 1));
                lastObstacleTime = now;
            }

            // Le joueur saute
            if (clavier.getJoyJ1HautEnfoncee() && !hasJump) {
                // Bruitage jumpBruitage = new Bruitage(
                //         "./assets/sound/jump.mp3");
                // jumpBruitage.lecture();
                hasJump = true;
                isAscended = true;
            }

            // Le joueur se baisse
            if (clavier.getJoyJ1BasEnfoncee() && !hasJump) {
                player.setTaille(100, 70);
            } else {
                player.setTaille(100, 150);
            }

            // Gérer le saut du joueur
            if (hasJump) {
                if (isAscended && player.getB().getY() < limitHeight) {
                    player.translater(0, 15);
                } else {
                    isAscended = false;
                    if (player.getA().getY() > sol.getB().getY()) {
                        player.translater(0, -15);
                    } else {
                        hasJump = false;
                    }
                }
            }

            it = listObstacle.iterator();
            while (it.hasNext()) {
                Obstacle obstacle = it.next();

                // Collision ?
                if (obstacle.intersectionRapide(player)) {
                    gameFinished = true;
                }

                if (obstacle.isOffScreen()) {
                    f.supprimer(obstacle);
                    it.remove();
                } // Gérer le déplacement des obstacles 
                else {
                    obstacle.translater(-10, 0);
                }
            }
            score += 1;
            stats.setTexte("Score: " + score);
            f.rafraichir();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gameover.setTexte("Game over !");
        f.rafraichir();
        try {
            Thread.sleep(1000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
