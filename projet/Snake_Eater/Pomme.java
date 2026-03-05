import MG2D.geometrie.Carre;
import MG2D.geometrie.Point;
import MG2D.Couleur;


// crée une pomme
public class Pomme {
// Attributs //
	private Carre c;
	private int taillecarre = 30;
	private boolean etat;
// Constructeur //
	public Pomme ( Point a ) {
		c = new Carre ( Couleur.ROUGE, a, taillecarre, true );
		etat = false;
	}
// Accesseurs //
	// Getter //
	public Carre getC () {
		return c;
	}
	public boolean getEtat () {
		return etat;
	}
	// Setter //
	public void setEtat ( boolean etat ) {
		this.etat = etat;
	}
}
