import MG2D.*;
import MG2D.geometrie.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Font;

public class Snake_Eater {

    
    // Attributs //

    final static int largeur = 1275;
    final static int hauteur = 1020;
    private static int tailleCarre = 30;
    private static int nbCarreHauteur = hauteur/tailleCarre;
	private static int nbCarreLargeur = largeur/tailleCarre;
    //static Fenetre f = new Fenetre ( "Snake Eater | 0 pomme",largeur, hauteur);
    static FenetrePleinEcran f = new FenetrePleinEcran("fen");

    private static ClavierBorneArcade clavier;
    // Géometrie //
    private static Point a = new Point ( 10*tailleCarre,11*tailleCarre );
    private static Carre joueur = new Carre ( new Couleur ( 255, 200, 200 ), a, tailleCarre,true );
    // Game Over & Statistique //
    private static Font calibri = new Font("Calibri", Font.TYPE1_FONT, 40);
    private static Rectangle bas = new Rectangle(Couleur.NOIR, new Point(0,0), largeur, 60, true);
    private static Rectangle gauche = new Rectangle(Couleur.NOIR, new Point(0,0), 60,hauteur, true);
    private static Rectangle droite = new Rectangle(Couleur.NOIR, new Point(1275,0),60,hauteur, true);
    private static Rectangle haut = new Rectangle(Couleur.NOIR, new Point(0,960),largeur,60, true);
    private static Texte gameover = new Texte (
					       Couleur.ROUGE,
					       new String ("Game Over !"),
					       calibri,
					       f.getMilieu()
					       );

    private static Texte statistique = new Texte (
						  Couleur.ROUGE,
						  new String ("Vous avez manger 0 pomme."),
						  calibri,
						  new Point ( f.getMilieu().getX(), f.getMilieu().getY() + 50 )
						  );

    private static Texte commentaire = new Texte (
						  Couleur.ROUGE,
						  new String (" "),
						  calibri,
						  new Point ( f.getMilieu().getX(), f.getMilieu().getY() + 100 )
						  );

    // Main //
    public static void main ( String [] args ) {

	f.setVisible(true);
  	clavier = new ClavierBorneArcade();
  	f.addKeyListener ( clavier );
	f.getP().addKeyListener ( clavier );
	for(int y =0;y<nbCarreHauteur; y++) {
		for(int x=0; x<nbCarreLargeur; x++) {
			int rest = y % 2;
			if(rest == 1 ) {
				int rest2 = x % 2;
				if(rest2 == 1) {
					f.ajouter(new Carre(new Couleur(0,128,0), new Point(0+(tailleCarre*(x)),0+(tailleCarre*(y))), tailleCarre, true));
				}else {
					f.ajouter(new Carre(new Couleur(34,139,34), new Point(0+(tailleCarre*(x)),0+(tailleCarre*(y))), tailleCarre, true));
				}
			}else {
				int rest2 = x%2;
				if(rest2 == 1) {
					f.ajouter(new Carre(new Couleur (34,139,34), new Point(0+(tailleCarre*(x)),0+(tailleCarre*(y))), tailleCarre, true));
				}else {
					f.ajouter(new Carre(new Couleur (0,128,0), new Point(0+(tailleCarre*(x)),0+(tailleCarre*(y))), tailleCarre, true));
				}
			}	
			
			f.rafraichir();
		}
	}
	f.setVisible(true);
  	f.setBackground ( Couleur.NOIR );
	f.ajouter( haut );
	f.ajouter( gauche );
	f.ajouter( droite );
	f.ajouter( bas );
  	Serpent s = new Serpent ( f, joueur );
  	Nourriture n = new Nourriture ( f );
  	int vitesse = 75;
  	int compteur = 0;
  	while ( s.getJouer() ) {
	    try {
		Thread.sleep ( vitesse );
	    }
	    catch ( Exception e ) {
		System.out.println ( e );
	    }
	    s.mouvement ( clavier );
	    s.intersection ( n.getPomme() );
	    n.jeu();
	    if ( vitesse > 50 && ( s.getNb() - s.getNb() % tailleCarre ) != compteur ) {
    		vitesse -= 5;
    		compteur = s.getNb() - s.getNb() % tailleCarre;
	    }
	    if ( s.getNb() < 2 )
    		f.setTitle ("Snake Eater | " + s.getNb() + " pomme");

	    else
		f.setTitle ("Snake Eater | " + s.getNb() + " pommes");

	    f.rafraichir();
  	}
	s.effacer();
	n.effacer();
	f.rafraichir();
	if ( s.getNb() < 2 )
	    statistique.setTexte ( "Vous avez manger " + s.getNb() + " pomme.");
	else
	    statistique.setTexte ( "Vous avez manger " + s.getNb() + " pommes.");
	f.ajouter ( gameover );
	f.ajouter ( statistique );
	if ( s.getNb() == 0 )
	    commentaire.setTexte ( "Serieusement ?!");

	if ( s.getNb() == 1 )
	    commentaire.setTexte ( "Snake looser ;) !" );

	if ( s.getNb() >= 2 )
	    commentaire.setTexte ( "Pas mal, jeune Snakewan." );

	if ( s.getNb() >= 10 )
	    commentaire.setTexte ( "Tu veux qu'on se tire l'oreille ?" );

	if ( s.getNb() >= 50 )
	    commentaire.setTexte ( "Snake, tu va mourir !" );

	f.ajouter ( commentaire );
	try {
	    Thread.sleep(3000);
	}catch(Exception e){e.getMessage();};
	HighScore.demanderEnregistrerNom(f,clavier,null,s.getNb(),"highscore");
    }
    
    
}
