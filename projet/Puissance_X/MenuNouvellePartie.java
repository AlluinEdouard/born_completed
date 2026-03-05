import java.awt.Color;
import java.awt.Graphics2D;

class MenuNouvellePartie extends Ecran {
	private Menu menu;

	private ElementMenu suivant_bouton, quitter_bouton;
	private ChoixValeurItem nbLignes_item, nbColonnes_item, nbPuissance_item, nbJoueurs_item;

	public MenuNouvellePartie(GestionAffichage fenetre) {
		super(fenetre, new Color(255, 255, 255, 127));
		this.menu = new Menu(10);
		this.suivant_bouton = new BoutonItem("Continuer", "Monospaced", 70) {
			public void surClic() {
				nouvellePartie();
			}
		};
		this.quitter_bouton = new BoutonItem("Retour", "Monospaced", 70) {
			public void surClic() {
				quitter();
			}
		};
		this.menu.ajouter(new TexteItem("Nouvelle Partie", "Monospaced", 100));
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(nbLignes_item = new ChoixValeurItem("Nombre de lignes", 4, 25, 6, "Monospaced", 70));
		this.menu.ajouter(nbColonnes_item = new ChoixValeurItem("Nombre de colonnes", 4, 25, 7, "Monospaced", 70));
		this.menu.ajouter(nbPuissance_item = new ChoixValeurItem("Puissance", 4, 10, 4, "Monospaced", 70));
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(this.suivant_bouton);
		this.menu.ajouter(this.quitter_bouton);
		this.menu.selectionner(2);
	}

	private void nouvellePartie() {
		this.fenetre.changerEcran(this.fenetre.menuChoixJoueurs);
		this.fenetre.menuChoixJoueurs.setConfig(this.nbLignes_item.getValeur(), this.nbColonnes_item.getValeur(), this.nbPuissance_item.getValeur());
	}
	private void quitter() {
		this.fenetre.changerEcran(this.fenetre.menuPrincipal);
	}

	@Override
	public void mettreAJour() {
		Entree clavier = this.fenetre.getClavier();
		if (clavier.echap(Menu.DUREE_TOUCHE)) this.quitter();
		this.menu.mettreAJour(clavier);
	}

	protected void dessiner(int opacite, Graphics2D graphics)
	{
		this.menu.afficher(this.fenetre.surface.milieu, opacite, graphics);
	}
}