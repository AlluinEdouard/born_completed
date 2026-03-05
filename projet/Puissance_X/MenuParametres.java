import java.awt.Color;
import java.awt.Graphics2D;

class MenuParametres extends Ecran {
	private Menu menu;

	private TexteItem hd_bouton;
	private ChoixValeurItem fps_item, vitesseChute_item;

	private boolean hd;

	public MenuParametres(GestionAffichage fenetre) {
		super(fenetre, new Color(255, 255, 255, 127));
		this.hd = false;

		this.menu = new Menu(10);
		this.menu.ajouter(new TexteItem("Paramètres", "Monospaced", 100));
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(this.hd_bouton = new BoutonItem("Basse définition", "Monospaced", 70) {
			public void surClic() {
				basculerHD();
			}
		});
		this.menu.ajouter(this.fps_item = new ChoixValeurItem("Images par seconde", 1, 120, Main.FPS, "Monospaced", 70));
		this.menu.ajouter(this.vitesseChute_item = new ChoixValeurItem("Vitesse chute pions", 0, 100, Rendu.VITESSE_CHUTE_PION_DEFAUT, "Monospaced", 70));
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(new BoutonItem("Retour", "Monospaced", 70) {
			public void surClic() {
				quitter();
			}
		});
		this.menu.selectionner(2);
	}

	private void basculerHD() {
		this.hd = !this.hd;
		this.hd_bouton.setTexte(this.hd ? "Haute définition" : "Basse définition");
		this.fenetre.getPartie().getRendu().setHauteDefinition(this.hd);
	}

	private void quitter() {
		this.fenetre.changerEcran(this.fenetre.menuPrincipal);
	}

	@Override
	public void mettreAJour() {
		Entree clavier = this.fenetre.getClavier();
		if (clavier.echap(Menu.DUREE_TOUCHE)) this.quitter();
		this.menu.mettreAJour(clavier);
		Main.setFPS(this.fps_item.getValeur());
		this.fenetre.getPartie().getRendu().setVitesseChutePion(this.vitesseChute_item.getValeur() == 0 ? 0 : 101 - this.vitesseChute_item.getValeur());
	}

	protected void dessiner(int opacite, Graphics2D graphics)
	{
		this.menu.afficher(this.fenetre.surface.milieu, opacite, graphics);
	}
}