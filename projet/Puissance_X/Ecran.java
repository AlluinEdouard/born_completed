import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

import MG2D.geometrie.Dessin;
import MG2D.geometrie.BoiteEnglobante;

abstract class Ecran extends Dessin {
	protected final GestionAffichage fenetre;
	protected Color couleurFond;

	//
	// CONSTRUCTEUR
	//

	public Ecran(GestionAffichage fenetre) {
		this(fenetre, new Color(255, 255, 255, 224));
	}
	public Ecran(GestionAffichage fenetre, Color couleurFond) {
		this.fenetre = fenetre;
		this.couleurFond = couleurFond;
	}

	//
	// GESTION DU FONDU
	//

	private int nbImages, dateDebutFondu, dureeFondu;

	public boolean estVisible() {
		return this.calculerOpacite() != 0;
	}
	public boolean estEnTransition() {
		return (nbImages - dateDebutFondu) < Math.abs(this.dureeFondu);
	}

	public void afficher(int dureeFondu) {
		this.dateDebutFondu = nbImages;
		this.dureeFondu = (dureeFondu > 0 ? dureeFondu : 0);
	}
	public void cacher(int dureeFondu) {
		this.dateDebutFondu = nbImages;
		this.dureeFondu = (dureeFondu > 0 ? -dureeFondu : 0);
	}

	private int calculerOpacite() {
		int nbImageDepuisDebutFondu = nbImages - dateDebutFondu;
		if (this.dureeFondu > 0 && nbImageDepuisDebutFondu < this.dureeFondu)
			return (int)(255 * (nbImageDepuisDebutFondu / (double)this.dureeFondu));
		else if (this.dureeFondu < 0 && nbImageDepuisDebutFondu < -this.dureeFondu)
			return 255 + (int)(255 * (nbImageDepuisDebutFondu / (double)this.dureeFondu));
		return this.dureeFondu > 0 ? 255 : 0;
	}
	
	//
	// MÉTHODES ABSTRAITES
	//

	public void mettreAJour() { }

	protected abstract void dessiner(int opacite, Graphics2D graphics);
	
	private void dessinerInterne(int opacite, Graphics2D graphics) {
		if (this.couleurFond != null)
		{
			// System.out.println((int)((opacite / (double)255) * this.couleurFond.getAlpha()));
			graphics.setColor(new Color(this.couleurFond.getRed(), this.couleurFond.getGreen(), this.couleurFond.getBlue(), (int)((opacite / (double)255) * this.couleurFond.getAlpha())));
			graphics.setComposite(AlphaComposite.SrcOver);
			graphics.fillRect(this.fenetre.surface.x, this.fenetre.surface.y, this.fenetre.surface.longueur, this.fenetre.surface.largeur);	
		}
		// graphics.translate(this.fenetre.surface.x, this.fenetre.surface.y);
		this.dessiner(opacite, graphics);
		// graphics.translate(-this.fenetre.surface.x, -this.fenetre.surface.y);
		this.nbImages++;
	}

	//
	// MÉTHODES HÉRITÉS DE « MG2D.geometrie.Dessin » 
	// NOTE : Seule la méthode « afficher(Graphics g) » est nécessaire, mais comme ces 4 méthodes sont abstraites ont a pas le choix de les écrires (et on ne parle pas de « toString() »)...
	//

	public BoiteEnglobante getBoiteEnglobante() { return new BoiteEnglobante(new MG2D.geometrie.Point(0, 0), new MG2D.geometrie.Point(this.fenetre.surface.longueur, this.fenetre.surface.largeur)); }
	public void translater(int dx, int dy) { /* Désolé, cet objet est trop grand pour être pour être déplacé... */ }
	public void afficher(Graphics g) { this.dessinerInterne(this.calculerOpacite(), (Graphics2D)g); }
	public String toString() { return ""; }

	public boolean equals(Object obj) {
		return (obj instanceof Ecran && this == (Ecran)obj);
	}
}