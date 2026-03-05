import java.util.Random;

class JoueurNormal implements Joueur {
	static public final int DUREE_TOUCHE = 200;

	private GestionAffichage affichage;

	private int noJoueur, nbJoueurs, nbPuissance, nbColonnes, nbLignes;

	private int colonneActuelle;

	public JoueurNormal(GestionAffichage affichage) {
		this.affichage = affichage;
	}

	public void nouvellePartie(int noJoueur, ConfigurationPartie config) {
		this.noJoueur = noJoueur;
		this.nbJoueurs = config.getNbJoueurs();
		this.nbPuissance = config.getNbPuissance();
		this.nbColonnes = config.getNbColonnes();
		this.nbLignes = config.getNbLignes();

		this.colonneActuelle = (int)(this.nbColonnes * Math.random()) + 1;
	}

	public int choisirColonne(Plateau plateau) {
		if (!this.affichage.estMenuVisible())
		{
			Entree clavier = this.affichage.getClavier();
			boolean entree = clavier.entree(DUREE_TOUCHE),
				droite = clavier.droite(DUREE_TOUCHE),
				gauche = clavier.gauche(DUREE_TOUCHE);

			if (gauche && this.colonneActuelle > 1) this.colonneActuelle--;
			if (droite && this.colonneActuelle < this.nbColonnes) this.colonneActuelle++;

			return (entree ? 1 : -1) * this.colonneActuelle;
		}
		return 0;
	}
}