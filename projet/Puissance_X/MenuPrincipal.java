import java.awt.Color;
import java.awt.Graphics2D;

class MenuPrincipal extends Ecran {
	private Menu menu;

	public MenuPrincipal(GestionAffichage fenetre) {
		super(fenetre, new Color(255, 255, 255, 127));
		this.menu = new Menu(25);
		this.menu.ajouter(new LogoItem("Puissance X", "Monospaced", 120));
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(new BoutonItem("Nouvelle Partie", "Monospaced", 70) {
			public void surClic() {
				nouvellePartie();
			}
		});
		this.menu.ajouter(new BoutonItem("Paramètres", "Monospaced", 70) {
			public void surClic() {
				parametres();
			}
		});
		this.menu.ajouter(new BoutonItem("Quitter", "Monospaced", 70) {
			public void surClic() {
				quitter();
			}
		});
		this.menu.selectionner(2);
	}

	private void nouvellePartie() {
		this.fenetre.changerEcran(this.fenetre.menuNouvellePartie);
	}
	private void parametres() {
		this.fenetre.changerEcran(this.fenetre.menuParametres);
	}
	private void quitter() {
		this.fenetre.quitter();
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