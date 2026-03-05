import java.awt.Color;
import java.util.ArrayList;

final class GenerateurCouleur {
	private final ArrayList<Color> couleurs;

	public GenerateurCouleur() {
		this.couleurs = new ArrayList<>();
	}

	public void ajouter() {
		this.ajouter(25);
	}
	public void ajouter(int contrate) {
		this.ajouter(this.genererCouleur(contrate));
	}
	public void ajouter(Color couleur) {
		this.couleurs.add(couleur);
	}

	public Color[] getCouleurs() {
		return this.couleurs.toArray(new Color[0]);
	}

	private Color genererCouleur(int contraste) {
		double x; int r, g, b; boolean trouve = false; Color couleurTrouvee;
		do
		{
			// x = Math.random();
			r =  (int)(1 * Math.random() * 256) % 256;
			g =  (int)(1 * Math.random() * 256) % 256;
			b =  (int)(1 * Math.random() * 256) % 256;
			couleurTrouvee = new Color(r, g, b);
			if (Math.min(differenceMax(new Color(0, 0, 0), couleurTrouvee), differenceMax(new Color(255, 255, 255), couleurTrouvee)) > 25)
			{
				trouve = true;
				for (Color couleur : couleurs)
					if (differenceMax(couleur, couleurTrouvee) <= contraste)
						trouve = false;
			}
		} while (!trouve);
		return couleurTrouvee;
	}

	private int differenceMax(Color c1, Color c2) {
		return Math.min(Math.abs(c1.getRed() - c2.getRed()), Math.min(Math.abs(c1.getGreen() - c2.getGreen()), Math.abs(c1.getBlue() - c2.getBlue())));
	}

	public static Color[] genererCouleurs(int nbCouleurs) {
		double contraste = 255 / nbCouleurs;
		Color[] resultat = new Color[nbCouleurs];
		for (int i = 0; i < nbCouleurs; i++)
			resultat[i] = new Color(
				(int)((int)(Math.random() * (nbCouleurs + 0.5)) * contraste),
				(int)((int)(Math.random() * (nbCouleurs + 0.5)) * contraste),
				(int)((int)(Math.random() * (nbCouleurs + 0.5)) * contraste)
			);
		return resultat;
	}
}