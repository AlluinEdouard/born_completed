import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Graphics2D;

class TexteItem extends ElementMenu {
	private String texte, nomPolice;
	private int tailleTexte, style;
	private Color couleur;

	//
	// CONSTRUCTEURS
	//

	public TexteItem(String texte) {
		this(texte, Font.MONOSPACED);
	}
	public TexteItem(String texte, String nomPolice) {
		this(texte, nomPolice, 20);
	}
	public TexteItem(String texte, String nomPolice, int taillePolice) {
		this(texte, nomPolice, taillePolice, Color.BLACK);
	}
	public TexteItem(String texte, String nomPolice, int taillePolice, Color couleur) {
		this.texte = texte;
		this.nomPolice = nomPolice;
		this.couleur = couleur;
		this.tailleTexte = taillePolice;
		this.style = Font.PLAIN;
		this.setEstActif(false);
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

	//
	// HERITAGE DE « ElementMenu »
	//

	public void mettreAJour(Entree clavier) { /* Cet élément n'est pas interactif. */ }

	//
	// AFFICHAGE DU TEXTE
	//

	public void afficher(int opacite, Graphics2D graphics) {
		graphics.setFont(new Font(this.nomPolice, this.style, this.tailleTexte));
		FontMetrics metrics = graphics.getFontMetrics();
		int longueur = metrics.stringWidth(this.texte),
			largeur = metrics.getAscent(),
			x_texte = -longueur / 2,
			y_texte = -largeur;
		graphics.setColor(new Color(this.couleur.getRed(), this.couleur.getGreen(), this.couleur.getBlue(), opacite));
		graphics.drawString(this.texte, x_texte, y_texte + largeur);
		this.afficher(new Zone(x_texte, y_texte, longueur, largeur), opacite, graphics);
	}

	protected void afficher(Zone zoneTexte, int opacite, Graphics2D graphics) { }
}