import java.awt.Graphics2D;

abstract class ElementMenu {
	private boolean actif, selectionne;

	//
	// CONSTRUCTEURS
	//

	public ElementMenu() {
		this.actif = true;
		this.selectionne = false;
	}

	//
	// ACCESSEURS / MUTATEURS
	//

	public boolean estActif() { return this.actif; }
	public boolean estSelectionne() { return this.selectionne; }

	public void setEstActif(boolean actif) { this.actif = actif; }
	public void setEstSelectionne(boolean selectionne) { this.selectionne = selectionne; }

	//
	// METHODES ABSTRAITES
	//
	
	public abstract void mettreAJour(Entree clavier);
	public abstract void afficher(int opacite, Graphics2D graphics);
}