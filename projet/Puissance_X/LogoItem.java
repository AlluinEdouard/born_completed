import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Graphics2D;

class LogoItem extends TexteItem {
	static public final double LOGO_FACTEUR_ROTATION = 0.025;
	static public final int LOGO_VITESSE_ROTATION = 140;
	static public final int LOGO_VITESSE_ZOOM = 120;
	
	//
	// CONSTRUCTEURS
	//

	public LogoItem(String texte) {
		this(texte, Font.MONOSPACED);
	}
	public LogoItem(String texte, String nomPolice) {
		this(texte, nomPolice, 20);
	}
	public LogoItem(String texte, String nomPolice, int taillePolice) {
		this(texte, nomPolice, taillePolice, Color.BLACK);
	}
	public LogoItem(String texte, String nomPolice, int taillePolice, Color couleur) {
		super(texte, nomPolice, taillePolice, couleur);
		this.setGras(true);
	}

	//
	// AFFICHAGE DU LOGO
	//

	private int logoEtape;

	public void afficher(int opacite, Graphics2D graphics) {
		double pasAngle = Math.min(logoEtape % (2 * (double)LOGO_VITESSE_ROTATION), (2 * LOGO_VITESSE_ROTATION) - (logoEtape % (2 * (double)LOGO_VITESSE_ROTATION))) / LOGO_VITESSE_ROTATION,
			angle = LOGO_FACTEUR_ROTATION * Math.PI * (pasAngle - 0.5),

			pasZoom = Math.min(logoEtape % (2 * (double)LOGO_VITESSE_ZOOM), (2 * LOGO_VITESSE_ZOOM) - (logoEtape % (2 * (double)LOGO_VITESSE_ZOOM))) / LOGO_VITESSE_ZOOM,
			echelle = Math.sin(0.15 * Math.PI * (pasZoom - 0.5)) + 1;

		graphics.rotate(angle);
		graphics.scale(echelle, echelle);
		super.afficher(opacite, graphics);
		graphics.scale(1 / echelle, 1 / echelle);
		graphics.rotate(-angle);

		this.logoEtape++;
	}
}