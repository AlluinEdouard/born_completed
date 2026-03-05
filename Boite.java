import MG2D.geometrie.Rectangle;

/**
 * Classe abstraite représentant une zone (boîte) graphique de base dans le menu.
 * Elle encapsule un objet Rectangle de la librairie MG2D servant de conteneur.
 */
public abstract class Boite {
    private Rectangle rectangle;
	
    Boite(Rectangle rectangle){
	this.rectangle = rectangle;
    }

    /**
     * Retourne le rectangle associé à cette boîte.
     * @return le rectangle MG2D de la boîte
     */
    public Rectangle getRectangle() {
	return rectangle;
    }

    /**
     * Modifie le rectangle associé à cette boîte.
     * @param rectangle Le nouveau rectangle MG2D.
     */
    public void setRectangle(Rectangle rectangle) {
	this.rectangle = rectangle;
    }
}
