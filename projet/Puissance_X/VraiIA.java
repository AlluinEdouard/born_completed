import java.util.Random;
import java.util.ArrayList;

class VraiIA implements Joueur {

	private int noJoueur, nbJoueurs, nbPuissance;
	private int nbColonnes, nbLignes;
	private int joueurAvantage, niveauMax;
	private Random rand;

	private Plateau grille;
	private ArrayList<Integer> joueurs;

	private int dureeTourMin, dureeTourMax, tempEntreDeplacement, colonneRandom, colonneCible, compteurDemande;
	private long dateDebutTour;

	private Thread rechercheArrierePlan;

	private boolean debug;

	public VraiIA(int dureeTourMin, int dureeTourMax, int tempEntreDeplacement, int niveauMax) {
		this.rand = new Random();
		this.dateDebutTour = 0;
		this.dureeTourMin = Math.min(dureeTourMin, dureeTourMax);
		this.dureeTourMax = Math.max(dureeTourMin, dureeTourMax);
		this.tempEntreDeplacement = tempEntreDeplacement;

		this.niveauMax = niveauMax;
	}
	public VraiIA(int dureeTourMin, int dureeTourMax, int tempEntreDeplacement, int niveauMax, boolean debug) {
		this(dureeTourMin, dureeTourMax, tempEntreDeplacement, niveauMax);

		this.debug = debug;
	}

	public void nouvellePartie(int noJoueur, ConfigurationPartie config) {
		this.noJoueur = noJoueur;
		this.joueurs = new ArrayList<>();
		for (int i = 1; i <= config.getNbJoueurs(); i++)
		{
			if (i == this.noJoueur)
				this.noJoueur = this.joueurs.size();
			if (config.getJoueur(i) != null)
				this.joueurs.add(Integer.valueOf(i));
		}
		this.nbJoueurs = this.joueurs.size();
		this.nbPuissance = config.getNbPuissance();
		this.nbColonnes = config.getNbColonnes();
		this.nbLignes = config.getNbLignes();
	}

	public int choisirColonne(Plateau plateau) {
		if (this.compteurDemande > tempEntreDeplacement)
			if (this.dateDebutTour == 0)
			{
				this.grille = plateau;
				this.colonneCible = -1;
				this.dateDebutTour = System.currentTimeMillis() + (int)((dureeTourMax - dureeTourMin) * Math.random() + dureeTourMin);
				if (tempEntreDeplacement == 0)
					this.dateDebutTour = 0;
				this.commencerRecherche();
				// this.colonneRandom = (int)(config.getNbColonnes() * Math.random());
			}
			else if (this.dateDebutTour - System.currentTimeMillis() < 0 && this.colonneCible != -1)
			{
				if (this.compteurDemande % tempEntreDeplacement == 0)
					if (this.colonneRandom < this.colonneCible && tempEntreDeplacement != 0)
						this.colonneRandom++;
					else if (this.colonneRandom > this.colonneCible && tempEntreDeplacement != 0)
						this.colonneRandom--;
					else
					{
						this.dateDebutTour = 0;
						this.compteurDemande = 0;
						return this.colonneCible + 1;
					}
			}
			else if (this.compteurDemande % tempEntreDeplacement == 0)
			{
				double dir = rand.nextDouble();
				if (dir < 0.33 && this.colonneRandom > 0)
					this.colonneRandom--;
				else if (dir < 0.66 && this.colonneRandom < this.nbColonnes - 1)
					this.colonneRandom++;
			}
		this.compteurDemande++;
		return -this.colonneRandom - 1;
	}

	private void log() {
		if (debug)
			System.out.println();
	}
	private void log(String message) {
		if (debug)
			System.out.println(message);
	}

	private void commencerRecherche() {
		if (this.rechercheArrierePlan == null)
		{
			this.rechercheArrierePlan = new Thread() {
				@Override
				public void run() {
					colonneCible = tourIA();
					rechercheArrierePlan = null;
				}
			};
			this.rechercheArrierePlan.setDaemon(true);
			this.rechercheArrierePlan.setPriority(Thread.MIN_PRIORITY);
			this.rechercheArrierePlan.start();
		}
	}

	//
	// RÉUTILISATION D'UNE CLASSE DANS MES RECHERCHE D'OPTIMISATION DE L'IA
	// CREDIT : Florentin Magniez
	//

	private int[][] grilleTemporaire;

	private int tourIA() {
		grilleTemporaire = new int[nbLignes][nbColonnes];
		for (int i = 0; i < nbLignes; i++)
			for (int j = 0; j < nbColonnes; j++)
				grilleTemporaire[i][j] = this.grille.getXY(i, j).getContenu();

		int meilleurColonne = -1, numLigne, colonneDepart = rand.nextInt(nbColonnes), numColonne;
		double poidsMeilleurColonne = 0, poids;
		log("J" + this.joueurs.get(noJoueur));
		for (int i = 0; i < nbColonnes; i++)
		{
			numColonne = (i + colonneDepart) % nbColonnes;
			for (numLigne = 0; numLigne < nbLignes && grilleTemporaire[numLigne][numColonne] != 0; numLigne++);
			if (numLigne != nbLignes)
			{
				poids = -calculerPoidsRecursif(0, numColonne, numLigne);
				log(" + C" + numColonne + " = " + poids);
				if (meilleurColonne == -1 || poidsMeilleurColonne < poids)
				{
					meilleurColonne = numColonne;
					poidsMeilleurColonne = poids;
					if (poidsMeilleurColonne == 1) // On ne peut trouver mieux...
						return meilleurColonne;
				}
			}
			else
				log(" + C" + numColonne);
		}
		log();
		log("   C" + meilleurColonne + " = " + poidsMeilleurColonne);
		log();
		return meilleurColonne;
	}

	private double calculerPoidsRecursif(int niveau, int numColonne, int numLigne) {
		int joueurActuel = this.joueurs.get((noJoueur + niveau) % nbJoueurs);

		grilleTemporaire[numLigne][numColonne] = joueurActuel;

		double poids = estCaseGagnante(numLigne, numColonne, joueurActuel);

		if (poids == 0 && (niveau < niveauMax || niveauMax == -1))
		{
			poids = 1;
			double poidsEnfant;
			for (int i = 0, ligne; i < nbColonnes; i++)
			{
				for (ligne = 0; ligne < nbLignes && grilleTemporaire[ligne][i] != 0; ligne++);
				if (ligne != nbLignes)
					// poids = Math.min(calculerPoidsRecursif(niveau + 1, i, ligne), poids);
					poids += calculerPoidsRecursif(niveau + 1, i, ligne);
			}
			poids /= nbColonnes;
			// poids /= 2;
		}
		// else if (niveau == 1 && poids != 0)
			// log("FATAL EN (" + numLigne + ", " + numColonne + ") !");

		grilleTemporaire[numLigne][numColonne] = 0;

		return -poids;// * (joueurActuel > 1 && joueurActuel < nbJoueurs ? 1 : -1);
	}

	private double estCaseGagnante(int numLigne, int numColonne, int joueurActuel) {
		int x, y, numJoueur = 0, joueurAligne = 0;
		boolean alignes;
		for (int a = 0; a < Plateau.MATRICE_ALIGNEMENTS.length; a++)
		{
			alignes = false;
			for (int i = 0; i < nbPuissance && !alignes; i++)
			{
				alignes = true;
				numJoueur = -1;
				for (int j = 0; j < nbPuissance && alignes; j++)
				{
					x = numColonne + j * Plateau.MATRICE_ALIGNEMENTS[a][0] - i * Plateau.MATRICE_ALIGNEMENTS[a][1];
					y = numLigne + j * Plateau.MATRICE_ALIGNEMENTS[a][2] - i * Plateau.MATRICE_ALIGNEMENTS[a][3];
					if (!grille.existe(y, x) || grilleTemporaire[y][x] != joueurActuel)
						alignes = false;
				}
			}
			if (alignes) return 1;
		}
		return 0;
	}
}
