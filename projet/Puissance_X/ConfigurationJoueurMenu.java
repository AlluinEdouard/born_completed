import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

class ConfigurationJoueurMenu extends Menu {
	static public final int DUREE_TOUCHE = 400;
	static public final String[] NOM_TYPE_JOUEURS = new String[] {
		"Aucun",
		"Joueur normal",
		"IA - Débutante",
		"IA - Normale",
		"IA - Avancée",
		"IA - Experte",
		// "IA - Invinsible", // DÉSACTIVÉ : Demande trop de ressources !
	};

	private TexteItem nomJoueurActuel;
	private Color couleurJoueur;

	private int type;
	private boolean selectionne;

	public ConfigurationJoueurMenu(Color couleur) {
		super(50);
		this.couleurJoueur = couleur;
		this.ajouterEspaceVide();
		this.ajouterEspaceVide();
		this.ajouterEspaceVide();
		this.ajouter(this.nomJoueurActuel = new TexteItem("Aucun", "Monospaced", 25));
		this.selectionner(1);
	}

	public int getType() {
		return this.type;
	}

	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
		this.nomJoueurActuel.setGras(selectionne);
	}

	@Override
	public void mettreAJour(Entree clavier) {
		boolean hautEnfonce = clavier.haut(DUREE_TOUCHE), basEnfonce = clavier.bas(DUREE_TOUCHE);
		if (hautEnfonce || basEnfonce)
		{
			this.type = (this.type + (hautEnfonce ? (NOM_TYPE_JOUEURS.length - 1) : 1)) % NOM_TYPE_JOUEURS.length;
			this.nomJoueurActuel.setTexte(NOM_TYPE_JOUEURS[this.type]);
		}
	}

	protected void dessiner(String texte, int x, int y, Graphics2D graphics) {
		graphics.setFont(new Font("Monospaced", Font.BOLD, 100));
		FontMetrics metrics = graphics.getFontMetrics();
		graphics.drawString(texte, x, y + metrics.getAscent());
	}

	@Override
	public void afficher(Point centre, int opacite, Graphics2D graphics)
	{
		graphics.setColor(new Color(this.couleurJoueur.getRed(), this.couleurJoueur.getGreen(), this.couleurJoueur.getBlue(), opacite));
		graphics.fillOval(centre.x - 75, centre.y - 32, 150, 150);
		super.afficher(centre, opacite, graphics);

		if (this.selectionne)
		{
			graphics.translate(centre.x, centre.y);
			graphics.rotate(Math.PI / 2);

			graphics.setColor(new Color(this.couleurJoueur.getRed(), this.couleurJoueur.getGreen(), this.couleurJoueur.getBlue(), opacite));
			this.dessiner("<", -100, 30, graphics);
			this.dessiner(">", 215, 30, graphics);

			graphics.rotate(-Math.PI / 2);
			graphics.translate(-centre.x, -centre.y);
		}
	}
}