import MG2D.geometrie.*;
import MG2D.Fenetre;
import MG2D.Couleur;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Score {

    private int secondes;
    private Timer timer;
    private boolean inProgress;
    private Texte timeDisplayed;
    private Fenetre window;

    public Score(Fenetre f) {
        this.window = f;
        this.secondes = 0;
        this.inProgress = false;

        this.timeDisplayed = new Texte(
                Couleur.NOIR,
                "Temps : 00:00",
                new Font("Calibri", Font.TYPE1_FONT, 20),
                new Point(window.getWidth() / 3, window.getHeight() - 120));

        this.window.ajouter(this.timeDisplayed);
    }

    public void begin() {
        if (inProgress)
            return;

        inProgress = true;
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                secondes++;
                update();
            }
        }, 1000, 1000);
    }

    public void stop() {
        if (!inProgress)
            return;

        inProgress = false;
        timer.cancel();
    }

    public int getTime() {
        return secondes;
    }

    private void update() {
        String formattedTime = formatTime(secondes);
        timeDisplayed.setTexte("Time : " + formattedTime);
        window.rafraichir();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static ArrayList<ScoreData> readFile(String file) {
        ArrayList<ScoreData> l = new ArrayList<ScoreData>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                l.add(new ScoreData(currentLine));
            }
            reader.close();
        } catch (Exception e) {
        }

        return l;
    }

    public static void registerFile(String file, ArrayList<ScoreData> scoresData, String name, int score) {
        int position = 0;
        while (position < scoresData.size() && score > scoresData.get(position).getScore()) {
            position++;
        }

        scoresData.add(position, new ScoreData(name, score));
        while (scoresData.size() > 10)
            scoresData.remove(scoresData.size() - 1);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (int i = 0; i < scoresData.size(); i++) {
                writer.write(scoresData.get(i).toString());
                if (i != (scoresData.size() - 1))
                    writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
        }

    }

    public static void registerScore(Fenetre f, ClavierBorneArcade keyboard, Texture t, int s, String HighScorefile) {

        ArrayList<ScoreData> scoresData = readFile(HighScorefile);
        for (ScoreData data : scoresData)
            System.out.println(data);

        int position = 0;
        boolean done = false;
        //
        while (!done) {
            if (position == scoresData.size())
                done = true;
            else if (s <= scoresData.get(position).getScore())
                position++;
            else {
                done = true;
            }
        }

        if (position >= 10)
            System.exit(0);

        String score = s + "";

        char cprec[] = { ' ', ' ', ' ' };
        char c[] = { 'A', ' ', ' ', '#' };
        char csuiv[] = { ' ', ' ', ' ' };
        int indexSelec = 0;

        Font font;
        font = null;
        try {
            File in = new File("../../fonts/PrStart.ttf");
            // File in = new File("E:/Mathilde/BUT_INFO/S6/borne_arcade/fonts/PrStart.ttf");
            // // Ne pas oublier
            font = font.createFont(Font.TRUETYPE_FONT, in);
            font = font.deriveFont(20.0f);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Texte highscore = new Texte(Couleur.NOIR, "H  I  G  H  S  C  O  R  E", font, new Point(640, 950));

        Texte enterYourName = new Texte(Couleur.NOIR, "E n t e r   Y o u r   N a m e", font, new Point(640, 800));
        Texte toRegisterYourScore = new Texte(Couleur.NOIR, "T o  R e g i s t e r  Y o u r  S c o r e", font,
                new Point(640, 700));
        Texte numPos = new Texte(Couleur.NOIR, (position + 1) + "eme", font, new Point(120, 400));

        if (position == 0)
            numPos.setTexte("1er");

        Texte characters[] = new Texte[4];
        characters[0] = new Texte(Couleur.NOIR, c[0] + "", font, new Point(485, 400));
        characters[1] = new Texte(Couleur.NOIR, c[1] + "", font, new Point(585, 400));
        characters[2] = new Texte(Couleur.NOIR, c[2] + "", font, new Point(685, 400));
        characters[3] = new Texte(Couleur.NOIR, c[3] + "", font, new Point(785, 400));

        Rectangle rect1 = new Rectangle(Couleur.NOIR, new Point(450, 350), new Point(520, 480), false);
        Rectangle rect2 = new Rectangle(Couleur.NOIR, new Point(550, 350), new Point(620, 480), false);
        Rectangle rect3 = new Rectangle(Couleur.NOIR, new Point(650, 350), new Point(720, 480), false);
        Rectangle rect4 = new Rectangle(Couleur.NOIR, new Point(750, 350), new Point(820, 480), false);

        Triangle select = new Triangle(Couleur.NOIR, new Point(490, 340), new Point(470, 300), new Point(510, 300),
                true);

        Texture whiteTrans = new Texture("img/blancTransparent.png", new Point(0, 0));

        if (t != null)
            f.ajouter(t);

        f.ajouter(whiteTrans);
        f.ajouter(highscore);

        f.ajouter(enterYourName);
        f.ajouter(toRegisterYourScore);
        f.ajouter(characters[0]);
        f.ajouter(characters[1]);
        f.ajouter(characters[2]);
        f.ajouter(characters[3]);
        f.ajouter(rect1);
        f.ajouter(rect2);
        f.ajouter(rect3);
        f.ajouter(rect4);
        f.ajouter(select);

        done = false;

        while (!done) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }

            if (keyboard.getJoyJ1DroiteTape()) {
                if (indexSelec < 3) {
                    indexSelec++;
                    select.translater(100, 0);
                }
            }

            if (keyboard.getJoyJ1GaucheTape()) {
                if (indexSelec > 0) {
                    indexSelec--;
                    select.translater(-100, 0);
                }
            }

            if (keyboard.getJoyJ1HautTape()) {
                if (indexSelec != 3) {
                    c[indexSelec] = next(c[indexSelec]);
                    characters[indexSelec].setTexte(c[indexSelec] + "");
                }
            }

            if (keyboard.getJoyJ1BasTape()) {
                if (indexSelec != 3) {
                    c[indexSelec] = previous(c[indexSelec]);
                    characters[indexSelec].setTexte(c[indexSelec] + "");
                }
            }

            if (keyboard.getBoutonJ1ATape() && indexSelec == 3)
                done = true;

            f.rafraichir();
        }

        registerFile(HighScorefile, scoresData, "" + c[0] + c[1] + c[2], s);

        System.exit(0);
    }

    public static char next(char c) {
        if (c >= 'A' && c < 'Z')
            return (char) (c + 1);
        if (c == 'Z')
            return '.';
        if (c == '.')
            return ' ';
        return 'A';
    }

    public static char previous(char c) {
        if (c > 'A' && c <= 'Z')
            return (char) (c - 1);
        if (c == 'A')
            return ' ';
        if (c == ' ')
            return '.';
        return 'Z';
    }
}
