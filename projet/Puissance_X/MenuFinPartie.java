import java.awt.Color;
import java.awt.Graphics2D;

class MenuFinPartie extends Ecran {
	private Menu menu;

	private ElementMenu rejouer_bouton, quitter_bouton;
	private TexteItem titre_texte;

	public MenuFinPartie(GestionAffichage fenetre) {
		super(fenetre, new Color(255, 255, 255, 127));
		this.menu = new Menu(20);
		this.titre_texte = new TexteItem("Partie Terminée !", "Monospaced", 100);
		this.rejouer_bouton = new BoutonItem("Rejouer", "Monospaced", 70) {
			public void surClic() {
				rejouer();
			}
		};
		this.quitter_bouton = new BoutonItem("Menu Principal", "Monospaced", 70) {
			public void surClic() {
				quitter();
			}
		};
		this.menu.ajouter(this.titre_texte);
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(this.rejouer_bouton);
		this.menu.ajouter(this.quitter_bouton);
		this.menu.selectionner(2);
	}

	private void rejouer() {
		this.fenetre.nouvellePartie(this.fenetre.getPartie().getConfiguration());
		this.fenetre.modeJeu();
	}
	private void quitter() {
		this.fenetre.modeMenu();
	}

	@Override
	public void afficher(int dureeFondu) {
		super.afficher(dureeFondu);
		Color couleurGagnant = Color.BLACK;
		PartieSprite partie = this.fenetre.getPartie();
		if (partie.getGagnant() > 0)
			couleurGagnant = partie.getRendu().getCouleursJoueurs()[partie.getGagnant() - 1];
		this.titre_texte.setTexte(partie.getGagnant() == 0 ? "Match nul !" : "Victoire !");
		this.titre_texte.setCouleur(couleurGagnant);
	}

	@Override
	public void mettreAJour() {
		this.menu.mettreAJour(this.fenetre.getClavier());
	}

	protected void dessiner(int opacite, Graphics2D graphics)
	{
		this.menu.afficher(this.fenetre.surface.milieu, opacite, graphics);
	}
}