import MG2D.Fenetre;
import MG2D.geometrie.Point;
import java.util.ArrayList;
public class Nourriture {
// Attribut //
	private ArrayList < Pomme > pomme;
	private Fenetre f;
// Constructeur //
	public Nourriture ( Fenetre f ) {
		this.f = f;
		pomme = new ArrayList < Pomme > ();
		nouvelle_pomme();
	}
// Accesseur //
	// Getter //
	public ArrayList < Pomme > getPomme () {
		return pomme;
	}
// Méthode //
	// Private //
	private void nouvelle_pomme () {
		int x = ( int ) ( Math.random() * 960 );
		int y = ( int ) ( Math.random() * 700 );
		x -= x % 30;
		y -= y % 30;
		if ( x <60 )
			x = 160;
		if ( y <60 )
			y = 160;
		pomme.add ( new Pomme ( new Point ( x, y ) ) );
		f.ajouter ( pomme.get( pomme.size() - 1 ).getC() );
	}
	// Public //
	public void jeu () {
		if ( pomme.get( pomme.size() - 1 ).getEtat() )
			nouvelle_pomme();
	}
	public void effacer () {
		f.supprimer ( pomme.get( pomme.size() - 1 ).getC() );
	}
}
