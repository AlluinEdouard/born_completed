import java.util.ArrayList;

final class ConfigurationPartie {
	static public final int PUISSANCE_NB_LIGNES_DEFAUT = 6;
	static public final int PUISSANCE_NB_COLONNES_DEFAUT = 7;
	static public final int PUISSANCE_NB_PUISSANCE_DEFAUT = 4;

	private final int nbColonnes, nbLignes, nbPuissance;
	private final ArrayList<Joueur> joueurs;

	public ConfigurationPartie() {
		this(PUISSANCE_NB_LIGNES_DEFAUT, PUISSANCE_NB_COLONNES_DEFAUT);
	}
	public ConfigurationPartie(int nbLignes, int nbColonnes) {
		this(nbColonnes, nbLignes, PUISSANCE_NB_PUISSANCE_DEFAUT);
	}
	public ConfigurationPartie(int nbLignes, int nbColonnes, int nbPuissance) {
		this.nbColonnes = nbColonnes;
		this.nbLignes = nbLignes;
		this.nbPuissance = nbPuissance;
		this.joueurs = new ArrayList<>();
	}
	public ConfigurationPartie(int nbLignes, int nbColonnes, int nbPuissance, Joueur[] joueurs) {
		this.nbColonnes = nbColonnes;
		this.nbLignes = nbLignes;
		this.nbPuissance = nbPuissance;
		this.joueurs = new ArrayList<>();
		for (int i = 0; i < joueurs.length; i++)
			this.joueurs.add(joueurs[i]);
	}
	public ConfigurationPartie(ConfigurationPartie original) {
		this.nbColonnes = original.nbColonnes;
		this.nbLignes = original.nbLignes;
		this.nbPuissance = original.nbPuissance;
		this.joueurs = new ArrayList<>(original.joueurs);
	}

	public boolean estValide() { return (this.getNbJoueurs() > 1); }

	public int getNbColonnes() { return this.nbColonnes; }
	public int getNbLignes() { return this.nbLignes; }
	public int getNbPuissance() { return this.nbPuissance; }
	public int getNbJoueurs() { return this.joueurs.size(); }
	public Joueur getJoueur(int noJoueur) { return this.joueurs.get(noJoueur - 1); }
	public Joueur[] getJoueurs() { return (Joueur[])this.joueurs.toArray(); }

	public void ajouterJoueur(Joueur joueur) { this.joueurs.add(joueur); }
	public void retirerJoueur(Joueur joueur) { this.joueurs.remove(joueur); }
}