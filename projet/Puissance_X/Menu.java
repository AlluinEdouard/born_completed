import java.awt.Graphics2D;
import java.util.ArrayList;

class Menu {
	static public final int DUREE_TOUCHE = 250;

	private int interligne, elementSelectionne;
	private final ArrayList<ElementMenu> elements;

	//
	// CONSTRUCTEURS
	//

	public Menu(int interligne) {
		this.interligne = interligne;
		this.elements = new ArrayList<>();
	}

	//
	// GESTION DES ELEMENTS + ACCESSEURS / MUTATEURS
	//

	public int getInterligne() { return this.interligne; }
	public int getNbElements() { return this.elements.size(); }

	public void setInterligne(int interligne) { this.interligne = interligne; }

	public void ajouter(ElementMenu element) { this.elements.add(element); this.selectionner(0); }
	public ElementMenu recuperer(int indice) { return this.elements.get(indice); }
	public void retirer(ElementMenu element) { this.elements.remove(element); }

	public void ajouterEspaceVide() {
		ElementMenu element = new ElementMenu() {
			public void mettreAJour(Entree clavier) { }
			public void afficher(int opacite, Graphics2D graphics) { }
		};
		element.setEstActif(false);
		this.ajouter(element);
	}

	public boolean selectionner(int indice) {
		if (this.recuperer(indice).estActif())
		{
			if (this.elementSelectionne != -1) this.recuperer(this.elementSelectionne).setEstSelectionne(false);
			this.recuperer(indice).setEstSelectionne(true);
			this.elementSelectionne = indice;
		}
		return this.recuperer(indice).estActif();
	}
	public void deselectionner() {
		this.recuperer(this.elementSelectionne).setEstSelectionne(false);
		this.elementSelectionne = -1;
	}

	//
	// INTERACTION UTILISATEUR
	//

	public void mettreAJour(Entree clavier) {
		if (this.getNbElements() == 0)
			return;
		boolean hautEnfonce = clavier.haut(DUREE_TOUCHE), basEnfonce = clavier.bas(DUREE_TOUCHE);
		if (hautEnfonce || basEnfonce || this.elementSelectionne == -1)
		{
			int nouveauSelectionne = this.elementSelectionne;
			do
			{
				nouveauSelectionne = (nouveauSelectionne + (hautEnfonce ? (this.getNbElements() - 1) : 1)) % this.getNbElements();
			} while (!this.selectionner(nouveauSelectionne));
		}
		else
			this.recuperer(this.elementSelectionne).mettreAJour(clavier);
	}

	//
	// AFFICHAGE DES ELEMENTS DU MENU
	//

	public void afficher(Point centre, int opacite, Graphics2D graphics) {
		int hauteur = this.interligne * this.elements.size();
		Point centreElement;
		for (int i = 0; i < this.elements.size(); i++)
		{
			centreElement = new Point(centre.x, (int)((double)hauteur * (i - this.elements.size() / 2) + centre.y));
			graphics.translate(centreElement.x, centreElement.y);
			this.elements.get(i).afficher(
				// new Point(centre.x, (int)((double)hauteur * (i - this.elements.size() / 2) + centre.y)),
				opacite,
				graphics
			);
			graphics.translate(-centreElement.x, -centreElement.y);
		}
	}
}