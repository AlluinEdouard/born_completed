class Plateau {

	// L'ULTIME !!!!!!!!!!!!
	// 2018 © Florentin Magniez - Je l'ai trouvé solo ! (après j'ai pas vérifié sur Internet si elle existait déjà...)
	static public final int[][] MATRICE_ALIGNEMENTS = new int[][] {
		{  0,  0,  1,  1 },
		{  1,  1,  0,  0 },
		{  1,  1,  1,  1 },
		{  1,  1, -1, -1 }
	};

	private Case[][] cases;
	private int nbColonnes, nbLignes,
				tailleAlignements; // Permet de changer la taille de la puissance (puissance 4 <=> 4 pions alignés; 6 pions alignés <=> puissance 6) !
	private int nbCasesLibresRestantes, gagnant;

	public Plateau() {
		this(1, 1, 4);
	}
	public Plateau(int nbColonnes, int nbLignes, int tailleAlignements) {
		this.nbColonnes = nbColonnes;
		this.nbLignes = nbLignes;
		this.nbCasesLibresRestantes = nbColonnes * nbLignes;
		this.tailleAlignements = tailleAlignements;
		this.gagnant = -1;
		this.cases = new Case[nbLignes][nbColonnes];
		for (int noLigne = 0; noLigne < nbLignes; noLigne++)
			for (int noColonne = 0; noColonne < nbColonnes; noColonne++)
				this.cases[noLigne][noColonne] = new Case();
	}

	public boolean existe(int noLigne, int noColonne) {
		return (noColonne >= 0 && noColonne < nbColonnes) && (noLigne >= 0 && noLigne < nbLignes);
	}

	public Case getXY(int noLigne, int noColonne) {
		return this.cases[noLigne][noColonne];
	}

	public int getNbColonnes() {
		return this.nbColonnes;
	}
	public int getNbLignes() {
		return this.nbLignes;
	}

	public int getNbCoupRestants() {
		return nbCasesLibresRestantes;
	}

	public int ajoutPion(int numColonne, int numJoueur) {
		int numLigne;

		// Recherche la case vide disponnible dans la colonne numColonne
		for (numLigne = 0; numLigne < this.nbLignes && this.getXY(numLigne, numColonne).getContenu() != 0; numLigne++);

		if (numLigne < this.nbLignes) // Au cas où la colonne est remplie entièrement
		{
			this.getXY(numLigne, numColonne).setContenu(numJoueur);

			nbCasesLibresRestantes--; // Pour détecter les matchs nuls
			
			// Recherche d'alignements
			int noColonne, noLigne;
			boolean alignes = false;
			for (int a = 0; a < MATRICE_ALIGNEMENTS.length && !alignes; a++)
			{
				for (int i = 0; i < tailleAlignements && !alignes; i++)
				{
					alignes = true;
					for (int j = 0; j < tailleAlignements && alignes; j++)
					{
						if (alignes)
						{
							noColonne = numColonne + j * MATRICE_ALIGNEMENTS[a][0] - i * MATRICE_ALIGNEMENTS[a][1];
							noLigne = numLigne + j * MATRICE_ALIGNEMENTS[a][2] - i * MATRICE_ALIGNEMENTS[a][3];
							if (!this.existe(noLigne, noColonne) || this.getXY(noLigne, noColonne).getContenu() != numJoueur)
								alignes = false;
						}
					}
					if (alignes)
					{
						for (int j = 0; j < tailleAlignements; j++)
						{
							noColonne = numColonne + j * MATRICE_ALIGNEMENTS[a][0] - i * MATRICE_ALIGNEMENTS[a][1];
							noLigne = numLigne + j * MATRICE_ALIGNEMENTS[a][2] - i * MATRICE_ALIGNEMENTS[a][3];
							this.getXY(noLigne, noColonne).setGagnante();
							if (this.gagnant < 0)
								this.gagnant = numJoueur;
						}
					}
				}
			}
		 	if (this.gagnant < 0 && this.nbCasesLibresRestantes == 0) // Match nul
				this.gagnant = 0;
		}
		return (numLigne == this.nbLignes ? -1 : numLigne);
	}

	public int gagne() {
		return this.gagnant;
	}

	@Override
	public String toString() {
		return "Puissance" + this.tailleAlignements + " - " + (this.gagnant == -1 ? this.nbCasesLibresRestantes + " case(s) restante(s)" : (this.gagnant == 0 ? "Match nul" : "Joueur n°" + this.gagnant + " gagant"));
	}
}