import java.awt.Color;
import java.awt.Graphics2D;

class MenuPause extends Ecran {
	private Menu menu;

	private ElementMenu reprendre_bouton, quitter_bouton;

	public MenuPause(GestionAffichage fenetre) {
		super(fenetre, new Color(255, 255, 255, 127));
		this.menu = new Menu(20);
		this.reprendre_bouton = new BoutonItem("Reprendre", "Monospaced", 70) {
			public void surClic() {
				reprendre();
			}
		};
		this.quitter_bouton = new BoutonItem("Menu Principal", "Monospaced", 70) {
			public void surClic() {
				quitter();
			}
		};
		this.menu.ajouter(new TexteItem("Pause", "Monospaced", 100));
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(this.reprendre_bouton);
		this.menu.ajouter(this.quitter_bouton);
		this.menu.selectionner(2);
	}

	private void reprendre() {
		this.fenetre.changerEcran(null);
	}
	private void quitter() {
		this.fenetre.modeMenu();
	}

	@Override
	public void mettreAJour() {
		Entree clavier = this.fenetre.getClavier();
		if (clavier.echap(Menu.DUREE_TOUCHE)) this.reprendre();
		this.menu.mettreAJour(clavier);
	}

	protected void dessiner(int opacite, Graphics2D graphics)
	{
		this.menu.afficher(this.fenetre.surface.milieu, opacite, graphics);
	}
}