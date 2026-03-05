import MG2D.Fenetre;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

class GestionAffichage {
    static public final int DUREE_TOUCHE = 250;
    static public final int INTERVAL_ENTRE_2_PARTIES = 180; // en nombre d'images (dépend des FPS)
    static public final int VITESSE_TRANSITION_ECRAN = 20;

    private Fenetre fenetre;
    private Entree clavier;

    private Ecran[] ecranTransition;
    private int noEcranActuel, noEcranSuivant;
    private boolean enTransition;

    private PartieSprite partie;
    private int nbImages, dateFinPartie;

    private boolean enJeu, continuer;
    private boolean highscoreEnregistre;

    public final Zone surface;
    public final MenuPrincipal menuPrincipal;
    public final MenuParametres menuParametres;
    public final MenuNouvellePartie menuNouvellePartie;
    public final MenuChoixJoueurs menuChoixJoueurs;
    public final MenuPause menuPause;
    public final MenuFinPartie menuFinPartie;

    public GestionAffichage(String titre) {
	//int longueur = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getWidth(),
	//largeur = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getHeight();
	int longueur=1280,largeur=1024;
	this.continuer = true;
	this.surface = new Zone(0, 0, longueur, largeur);
	this.fenetre = new Fenetre(titre, longueur, largeur);
	this.fenetre.dispose();
	this.fenetre.setUndecorated(true);
	GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(this.fenetre);
	this.fenetre.setVisible(true);
	this.partie = new PartieSprite(longueur, largeur);
	this.dateFinPartie = 0;
	this.nbImages = 0;
	this.highscoreEnregistre = false;
	this.fenetre.ajouter(this.partie);
	this.clavier = new Entree();
	this.fenetre.addKeyListener(this.clavier);
	this.ecranTransition = new Ecran[2];
	this.noEcranActuel = -1;
	this.noEcranSuivant = 0;
	this.menuNouvellePartie = new MenuNouvellePartie(this);
	this.menuPause = new MenuPause(this);
	this.menuFinPartie = new MenuFinPartie(this);
	this.menuChoixJoueurs = new MenuChoixJoueurs(this);
	this.menuParametres = new MenuParametres(this);
	this.changerEcran(this.menuPrincipal = new MenuPrincipal(this));
    }

    public boolean pretAQuitter() {
	return !this.continuer;
    }

    public Entree getClavier() {
	return this.clavier;
    }

    public PartieSprite getPartie() {
	return this.partie;
    }

    public void quitter() {
	this.continuer = false;
	this.fenetre.fermer();
    }

    public boolean estMenuVisible() {
	return this.ecranTransition[0] != null && !this.enTransition;
    }

    public void changerEcran(Ecran nouveau) {
	this.verifierFinTransition();
	if (this.ecranTransition[0] != null)
	    this.ecranTransition[0].cacher(VITESSE_TRANSITION_ECRAN);
	this.ecranTransition[1] = nouveau;
	if (this.ecranTransition[1] != null)
	    {
		this.ecranTransition[1].afficher(VITESSE_TRANSITION_ECRAN);
		this.fenetre.ajouter(this.ecranTransition[1]);
	    }
	this.enTransition = true;
    }
    private void terminerTransition() {
	this.fenetre.effacer();
	this.fenetre.ajouter(this.partie);
	if (this.ecranTransition[1] != null)
	    this.fenetre.ajouter(this.ecranTransition[1]);
	this.ecranTransition[0] = this.ecranTransition[1];
	this.ecranTransition[1] = null;
	this.enTransition = false;
    }
    private void verifierFinTransition() {
	if (this.enTransition)
	    {
		Ecran transition = this.ecranTransition[1];
		if (transition == null) transition = this.ecranTransition[0];
		if (!transition.estEnTransition())
		    this.terminerTransition();
	    }
    }

    public void nouvellePartie() {
	this.nouvellePartie(this.genererConfig());
    }
    public void nouvellePartie(ConfigurationPartie config) {
	this.dateFinPartie = 0;
	this.highscoreEnregistre = false;
	this.partie.nouvellePartie(config);
    }

    public void modeMenu() {
	this.enJeu = false;
	this.changerEcran(this.menuPrincipal);
	this.nouvellePartie();
	this.dateFinPartie = 0;
    }
    public void modeJeu() {
	this.changerEcran(null);
	this.enJeu = true;
    }

    private ConfigurationPartie genererConfig() {
	return new ConfigurationPartie(
				       (int)(5 * Math.random() + 5),
				       (int)(5 * Math.random() + 6),
				       4,
				       new Joueur[] {
					   new VraiIA(500, 1500, 14, (int)(3 * Math.random()) + 1, false),
					   new VraiIA(500, 1500, 14, (int)(3 * Math.random()) + 1, false),
				       }
				       );
    }

    public void mettreAJour() {
	this.partie.mettreAJour();
	if (this.partie.estTerminee())
	    if (!this.enJeu)
		{
		    if (this.dateFinPartie == 0)
			this.dateFinPartie = this.nbImages + INTERVAL_ENTRE_2_PARTIES;
		    else if (this.dateFinPartie < this.nbImages)
			{
			    this.nouvellePartie();
			}
		}
	    else if (this.dateFinPartie == 0)
		{
		    this.dateFinPartie = -1;
		    if (!this.highscoreEnregistre) {
			enregistrerHighScore();
			this.highscoreEnregistre = true;
		    }
		    this.changerEcran(this.menuFinPartie);
		}
	if (this.ecranTransition[0] != null)
	    this.ecranTransition[0].mettreAJour();
	if (this.enJeu && !this.estMenuVisible() && this.clavier.echap(DUREE_TOUCHE))
	    this.changerEcran(this.menuPause);
	// System.out.println(this.noEcranActuel);
	this.verifierFinTransition();
    }

    public void rafraichir() {
	this.verifierFinTransition();
	this.fenetre.rafraichir();
	this.nbImages++;
    }

    private void enregistrerHighScore() {
	int gagnant = this.partie.getGagnant();
	String nom = (gagnant == 0) ? "NUL" : ("J" + gagnant);
	int score = this.partie.getNbTours();
	ArrayList<LigneHighScore> list = HighScore.lireFichier("highscore");
	HighScore.enregistrerFichier("highscore", list, nom, score);
    }
}
