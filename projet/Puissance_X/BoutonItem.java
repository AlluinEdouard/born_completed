import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Graphics2D;

abstract class BoutonItem extends TexteItem {
	static public final int LOGO_VITESSE_ROTATION = 60;

	//
	// CONSTRUCTEURS
	//

	public BoutonItem(String texte) {
		this(texte, Font.MONOSPACED);
	}
	public BoutonItem(String texte, String nomPolice) {
		this(texte, nomPolice, 20);
	}
	public BoutonItem(String texte, String nomPolice, int taillePolice) {
		this(texte, nomPolice, taillePolice, Color.BLACK);
	}
	public BoutonItem(String texte, String nomPolice, int taillePolice, Color couleur) {
		super(texte, nomPolice, taillePolice, couleur);
		this.setEstActif(true);
	}

	//
	// CLIQUER SUR LE BOUTON
	//

	public void mettreAJour(Entree clavier)
	{
		boolean entreeEnfonce = clavier.entree(Menu.DUREE_TOUCHE);
		if (entreeEnfonce)
			this.surClic();
	}

	//
	// CHANGEMENT D'ETAT
	//

	public void setEstSelectionne(boolean selectionne) {
		super.setEstSelectionne(selectionne);
		this.setGras(selectionne);
	}
	public void setEstActif(boolean actif) {
		super.setEstActif(actif);
		this.setItalique(!actif);
	}

	//
	// AFFICHAGE DU BOUTON
	//

	private int logoPionEtape;

	protected void afficher(Zone zoneTexte, int opacite, Graphics2D graphics) {
		if (this.estSelectionne())
		{
			int etape = ((2 * logoPionEtape) / (LOGO_VITESSE_ROTATION)) % 2;
			double angle = 2 * Math.PI * ((logoPionEtape % LOGO_VITESSE_ROTATION) / (double)LOGO_VITESSE_ROTATION);

			int x = zoneTexte.x + (int)(zoneTexte.largeur * -Math.abs(Math.sin(angle)) / 2) - zoneTexte.largeur / 2,
				y = zoneTexte.y;

			Color couleurEtape = (etape == 0 ? Color.RED : Color.YELLOW);
			graphics.setColor(new Color(couleurEtape.getRed(), couleurEtape.getGreen(), couleurEtape.getBlue(), opacite));

			graphics.fillOval(x, y, (int)(zoneTexte.largeur * Math.abs(Math.sin(angle))), zoneTexte.largeur);
			logoPionEtape++;
		}
	}

	//
	// DETECTION DE CLICS
	//

	public abstract void surClic();
}