import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

class Rendu {

	//
	// CONSTANTES GLOBALES
	//

	static public final Color TRANSPARENT = new Color(0, 0, 0, 0);

	//
	// CONSTRUCTEURS
	//

	public Rendu(int longueur, int largeur) {

		// CONFIGURATION GENERALE
		this.hd = HD_DEFAUT;
		this.vitesseChutePion = 101 - VITESSE_CHUTE_PION_DEFAUT;
		this.couleurGrille = COULEUR_GRILLE_DEFAUT;
		this.couleursJoueurs = COULEURS_PIONS_DEFAUT;

		// ANIMATION
		this.longueur = longueur;
		this.largeur = largeur;

		// this.setHauteDefinition(true);
	}

	//
	// CONFIGURATION GENERALE
	//

	static public final boolean HD_DEFAUT = false;
	static public final int VITESSE_CHUTE_PION_DEFAUT = 10;
	static public final Color COULEUR_GRILLE_DEFAUT = Color.BLUE;
	static public final Color[] COULEURS_PIONS_DEFAUT = new Color[] {
		Color.RED,
		Color.YELLOW,
		Color.GREEN,
		Color.ORANGE,
	};

	private boolean hd;
	private int vitesseChutePion;
	private Color couleurGrille;
	private Color[] couleursJoueurs;

	public boolean estHauteDefinition() { return this.hd; }
	public int getVitesseChutePion() { return this.vitesseChutePion; }
	public Color getCouleurGrille() { return this.couleurGrille; }
	public Color[] getCouleursJoueurs() { return this.couleursJoueurs; }

	public void setHauteDefinition(boolean hd) { this.hd = hd; synchronized (this) { this.grilleTrous = null; } }
	public void setVitesseChutePion(int vitesseChutePion) { this.vitesseChutePion = Math.abs(vitesseChutePion); }
	public void setCouleurGrille(Color couleurGrille) { this.couleurGrille = couleurGrille; synchronized (this) { this.grilleTrous = null; } }
	public void setCouleursJoueurs(int noJoueur, Color couleur) { this.couleursJoueurs[noJoueur - 1] = couleur; synchronized (this) { this.grilleTrous = null; } }
	public void setCouleursJoueurs(Color[] couleursJoueurs) { this.couleursJoueurs = couleursJoueurs; synchronized (this) { this.grilleTrous = null; } }

	//
	// CONFIGURATION PARTIE
	//

	public int nbColonnes, nbLignes;

	public void nouvellePartie(int nbColonnes, int nbLignes) {
		if (this.couleursJoueurs == null)
			throw new IllegalStateException("Les couleurs des pions des joueurs n'ont pas été configuré.");
		this.nbColonnes = nbColonnes;
		this.nbLignes = nbLignes;
		this.dateFinChutePion = new long[nbLignes][nbColonnes];
		this.dateFinAnimation = 0;
		this.nbImagesDessinees = 0;
		this.grilleTrous = null;
		this.setJoueurActuel(1);
		this.calculerTailleGrille();
	}

	//
	// EVENEMENTS
	//

	public void setJoueurActuel(int noJoueur) { this.noJoueurActuel = noJoueur - 1; }
	public void setColonneActuelle(int noColonne) { this.noColonneActuelle = noColonne; }
	// public void ajoutPionEchoue() { /* this.setMessage("Pion ajouté."); */ }
	public void ajoutPionReussi(int noLigne) {
		synchronized (this)
		{
			if ((this.dateFinChutePion[noLigne][this.noColonneActuelle] = (long)(this.nbImagesDessinees + (this.vitesseChutePion == 0 ? 0 : this.getY(noLigne) / this.vitesseChutePion) + 1)) > this.dateFinAnimation) 
				this.dateFinAnimation = this.dateFinChutePion[noLigne][this.noColonneActuelle];
		}
	}

	//
	// ANIMATION
	//

	private int noJoueurActuel, noColonneActuelle;

	public boolean estPret() { return this.nbImagesDessinees != 0; }

	/********************/

	private long nbImagesDessinees, dateFinAnimation;
	private long[][] dateFinChutePion;

	public void mettreAJour(Plateau plateau, Graphics2D graphics) {
		synchronized (this)
		{
			if (this.grilleTrous == null)
				this.redessinerGrille(plateau);

			graphics.clearRect(0, 0, this.longueur, this.largeur);
			if (plateau.gagne() == -1 || dateFinAnimation > nbImagesDessinees)
			{
				if (plateau.gagne() == -1)
				{
					Color couleur = this.couleursJoueurs[this.noJoueurActuel];
					graphics.setColor(plateau.getXY(nbLignes - 1, noColonneActuelle).getContenu() != 0 ? couleur.darker() : couleur);
					graphics.fillOval(this.getX(noColonneActuelle), 0, (int)this.longueurPion, (int)this.largeurPion);	
				}

				Graphics2D graphicsImage = (Graphics2D)this.grilleTrous.getGraphics(), gAUtiliser;

				// this.appliquerHD(graphics);
				this.appliquerHD(graphicsImage);
				
				int caseActuelle; int X, Y;
				for (int noColonne = 0; noColonne < this.nbColonnes; noColonne++)
					for (int noLigne = 0; noLigne < this.nbLignes; noLigne++)
					{
						caseActuelle = plateau.getXY(noLigne, noColonne).getContenu();
						if (this.dateFinChutePion[noLigne][noColonne] > nbImagesDessinees)
						{
							if (caseActuelle == 0)
								System.out.println("GROS PROBLÈME EN [" + noColonne + ", " + noLigne + "] : " + this.dateFinChutePion[noLigne][noColonne]);
							X = this.getX(noColonne); Y = this.getY(noLigne);
							if (this.dateFinChutePion[noLigne][noColonne] == nbImagesDessinees + 1)
								gAUtiliser = graphicsImage;
							else
							{
								Y -= (this.dateFinChutePion[noLigne][noColonne] - nbImagesDessinees) * this.vitesseChutePion;
								gAUtiliser = graphics;
							}
							gAUtiliser.setColor(this.couleursJoueurs[caseActuelle - 1]);
							gAUtiliser.fillOval(X, Y, (int)this.longueurPion, (int)this.largeurPion);
						}
					}
				graphicsImage.dispose();
			}
			else if (dateFinAnimation == nbImagesDessinees)
				this.redessinerGrille(plateau);

			graphics.drawImage(this.grilleTrous, 0, 0, this.longueur, this.largeur, null);

			nbImagesDessinees++;
		}
		this.appliquerHD(graphics);
	}

	/********************/

	private int longueur, largeur;
	private int margeSuperieure;

	private double origineGrilleX, origineGrilleY;
	private double longueurPion, largeurPion;

	private void calculerTailleGrille() {
		double diametrePion = Math.min(this.longueur / (double)this.nbColonnes, this.largeur / (double)(this.nbLignes + 1));

		this.origineGrilleX = (this.longueur / 2) - ((diametrePion * this.nbColonnes) / 2);
		this.origineGrilleY = (this.largeur / 2) - ((diametrePion * (this.nbLignes + 1)) / 2);

		this.longueurPion = (this.largeurPion = diametrePion);
	}

	private int getX(int noColonne) { return (int)(origineGrilleX + longueurPion * noColonne); }
	private int getY(int noLigne) { return (int)(this.largeur - (origineGrilleY + largeurPion * (noLigne + 1))); }

	public int getLongueur() { return this.longueur; }
	public int getLargeur() { return this.largeur; }

	/********************/

	private BufferedImage grilleTrous;

	private void redessinerGrille(Plateau plateau) {
		this.grilleTrous = new BufferedImage(this.longueur, this.largeur, BufferedImage.TYPE_INT_ARGB); //TYPE_INT_ARGB
		Graphics2D graphics = (Graphics2D)this.grilleTrous.getGraphics();

		this.appliquerHD(graphics);

		graphics.setColor(this.couleurGrille);
		graphics.fillRect(0, (int)this.largeurPion, this.longueur, this.largeur - (int)this.largeurPion);

		graphics.setColor(new Color(0, 0, 0, 0));
		graphics.setComposite(AlphaComposite.Src);

		int X, Y; Case caseActuelle; Color couleur; boolean partieTerminee = (plateau.gagne() != -1);
		for (int noColonne = 0; noColonne < this.nbColonnes; noColonne++)
			for (int noLigne = 0; noLigne < this.nbLignes; noLigne++)
			{
				X = this.getX(noColonne);
				Y = this.getY(noLigne);
				// System.out.println("[" + noColonne + ", " + noLigne + "] = (" + X + ", " + Y + ")");
				if (this.dateFinChutePion[noLigne][noColonne] != 0 && this.dateFinChutePion[noLigne][noColonne] <= this.nbImagesDessinees)
				{
					caseActuelle = plateau.getXY(noLigne, noColonne);
					if (partieTerminee && (caseActuelle.estGagante() || plateau.gagne() == 0)) // && (caseActuelle.estGagante() || plateau.gagne() == 0)
						// couleur = (caseActuelle.estGagante() || plateau.gagne() == 0 ? this.couleursJoueurs[caseActuelle.getContenu() - 1].darker() : couleurGrille);
						couleur = this.couleursJoueurs[caseActuelle.getContenu() - 1].darker();
					else
						couleur = this.couleursJoueurs[caseActuelle.getContenu() - 1];
					graphics.setColor(couleur);
					graphics.fillOval(X, Y, (int)this.longueurPion, (int)this.largeurPion);
				}
				else
				{
					couleur = TRANSPARENT;
					graphics.setColor(couleur);
					graphics.fillOval(X + 1, Y + 1, (int)this.longueurPion - 2, (int)this.largeurPion - 2);
				}
			}

		graphics.dispose();
	}
	
	/********************/

	private String message;
	private Color couleurMessage;
	private long dateChangementMessage;

	public void setMessage(String message, Color couleur) {
		this.message = message;
		this.couleurMessage = couleur;
		this.dateChangementMessage = this.nbImagesDessinees;
	}

	/********************/

	private void appliquerHD(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.hd ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF); // Meilleur rendu
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, this.hd ? RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY : RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, this.hd ? RenderingHints.VALUE_COLOR_RENDER_QUALITY : RenderingHints.VALUE_COLOR_RENDER_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_DITHERING, this.hd ? RenderingHints.VALUE_DITHER_ENABLE : RenderingHints.VALUE_DITHER_DISABLE);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.hd ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.hd ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, this.hd ? RenderingHints.VALUE_RENDER_QUALITY : RenderingHints.VALUE_RENDER_SPEED);
	}
}