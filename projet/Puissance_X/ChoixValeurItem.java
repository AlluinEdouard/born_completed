import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Graphics2D;

class ChoixValeurItem extends ElementMenu {
	static public final double ANIMATION_FACTEUR_DEPLACEMENT = 15;
	static public final int ANIMATION_VITESSE = 70;
	static public final int DUREE_TOUCHE = 175;

	private String texte, nomPolice;
	private int tailleTexte, style;
	private Color couleur;

	private int min, valeur, max;

	//
	// CONSTRUCTEURS
	//

	public ChoixValeurItem(String texte, int min, int max, int defaut) {
		this(texte, min, max, defaut, Font.MONOSPACED);
	}
	public ChoixValeurItem(String texte, int min, int max, int defaut, String nomPolice) {
		this(texte, min, max, defaut, nomPolice, 20);
	}
	public ChoixValeurItem(String texte, int min, int max, int defaut, String nomPolice, int taillePolice) {
		this(texte, min, max, defaut, nomPolice, taillePolice, Color.BLACK);
	}
	public ChoixValeurItem(String texte, int min, int max, int defaut, String nomPolice, int taillePolice, Color couleur) {
		this.texte = texte;
		this.min = min;
		this.max = max;
		this.valeur = defaut;
		this.nomPolice = nomPolice;
		this.couleur = couleur;
		this.tailleTexte = taillePolice;
		this.style = Font.PLAIN;
		this.setEstActif(true);
	}

	//
	// ACCESSEURS / MUTATEURS
	//

	public String getTexte() { return this.texte; }
	public int getTailleTexte() { return this.tailleTexte; }
	public String getPolice() { return this.nomPolice; }
	public Color getCouleur() { return this.couleur; }
	public boolean estGras() { return (this.style & Font.BOLD) == Font.BOLD; }
	public boolean estItalique() { return (this.style & Font.ITALIC) == Font.ITALIC; }

	public void setTexte(String texte) {
		this.texte = texte;
	}
	public void setTailleTexte(int tailleTexte) {
		this.tailleTexte = tailleTexte;
	}
	public void setPolice(String nomPolice) {
		this.nomPolice = nomPolice;
	}
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}
	public void setGras(boolean gras) {
		if (gras != this.estGras())
			this.style ^= Font.BOLD;
	}
	public void setItalique(boolean italique) {
		if (italique != this.estItalique())
			this.style ^= Font.ITALIC;
	}

	public int getValeur() {
		return this.valeur;
	}

	//
	// CHANGEMENT D'ETAT
	//

	public void setEstSelectionne(boolean selectionne) {
		super.setEstSelectionne(selectionne);
		this.setGras(selectionne);
	}

	//
	// HERITAGE DE « ElementMenu »
	//

	public void mettreAJour(Entree clavier) {
		boolean droite = clavier.droite(DUREE_TOUCHE), gauche = clavier.gauche(DUREE_TOUCHE);
		if (gauche && valeur > min) valeur--;
		if (droite && valeur < max) valeur++;
	}

	//
	// AFFICHAGE DU TEXTE
	//

	protected Point mesurer(String texte, Graphics2D graphics) {
		graphics.setFont(new Font(this.nomPolice, this.style, this.tailleTexte));
		FontMetrics metrics = graphics.getFontMetrics();
		return new Point(metrics.stringWidth(texte), metrics.getAscent());
	}

	protected void dessiner(String texte, int x, int y, Graphics2D graphics) {
		graphics.setFont(new Font(this.nomPolice, this.style, this.tailleTexte));
		FontMetrics metrics = graphics.getFontMetrics();
		graphics.drawString(texte, x, y + metrics.getAscent());
	}

	private int animationEtape;

	public void afficher(int opacite, Graphics2D graphics) {
		String texte = this.texte + " : " + this.valeur;
		graphics.setFont(new Font(this.nomPolice, this.style, this.tailleTexte));
		FontMetrics metrics = graphics.getFontMetrics();
		int longueur = metrics.stringWidth(texte),
			largeur = metrics.getAscent(),
			x_texte = -longueur / 2,
			y_texte = -largeur;
		graphics.setColor(new Color(this.couleur.getRed(), this.couleur.getGreen(), this.couleur.getBlue(), opacite));
		graphics.drawString(texte, x_texte, y_texte + largeur);
		this.afficher(new Zone(x_texte, y_texte, longueur, largeur), opacite, graphics);
	}

	protected void afficher(Zone zoneTexte, int opacite, Graphics2D graphics) {
		if (this.estSelectionne())
		{
			double etape = animationEtape % (2 * (double)ANIMATION_VITESSE),
				pas = Math.min(etape, (2 * ANIMATION_VITESSE) - etape) / ANIMATION_VITESSE;
			int decallage = (int)(ANIMATION_FACTEUR_DEPLACEMENT * Math.cos(Math.PI * pas));

			Point tailleNombre = this.mesurer(Integer.toString(this.valeur), graphics);

			int styleOriginal = this.style;
			this.style = Font.BOLD;
			Color couleurEtape = Color.RED;
			graphics.setColor(new Color(couleurEtape.getRed(), couleurEtape.getGreen(), couleurEtape.getBlue(), opacite));
			this.dessiner("<", zoneTexte.x + zoneTexte.longueur - tailleNombre.x - this.tailleTexte - decallage, zoneTexte.y, graphics);
			couleurEtape = Color.YELLOW;
			graphics.setColor(new Color(couleurEtape.getRed(), couleurEtape.getGreen(), couleurEtape.getBlue(), opacite));
			this.dessiner(">", zoneTexte.x + zoneTexte.longueur + this.tailleTexte / 2 + decallage, zoneTexte.y, graphics);
			this.style = styleOriginal;

			zoneTexte = new Zone(zoneTexte.x, zoneTexte.y, zoneTexte.longueur + 2 * (decallage + this.tailleTexte), zoneTexte.largeur);

			etape = ((2 * animationEtape) / (BoutonItem.LOGO_VITESSE_ROTATION)) % 2;
			double angle = 2 * Math.PI * ((animationEtape % BoutonItem.LOGO_VITESSE_ROTATION) / (double)BoutonItem.LOGO_VITESSE_ROTATION);

			int x = zoneTexte.x + (int)(zoneTexte.largeur * -Math.abs(Math.sin(angle)) / 2) - zoneTexte.largeur / 2,
				y = zoneTexte.y;

			couleurEtape = (etape == 0 ? Color.RED : Color.YELLOW);
			graphics.setColor(new Color(couleurEtape.getRed(), couleurEtape.getGreen(), couleurEtape.getBlue(), opacite));

			graphics.fillOval(x, y, (int)(zoneTexte.largeur * Math.abs(Math.sin(angle))), zoneTexte.largeur);
			this.animationEtape++;
		}
	}
}
