import java.awt.Color;
import java.awt.Graphics2D;

class MenuChoixJoueurs extends Ecran {
	static public final int DUREE_TOUCHE = 400;

	private Menu menu;
	private ConfigurationJoueurMenu[] menu_joueurs;
	private int selectionne, nbActifs;
	private int[] types;

	private int nbLignes, nbColonnes, nbPuissance;

	private ElementMenu jouer_bouton, retour_bouton;

	public MenuChoixJoueurs(GestionAffichage fenetre) {
		super(fenetre, new Color(255, 255, 255, 127));

		this.menu = new Menu(12);
		this.menu.ajouter(new TexteItem("Choix des joueurs", "Monospaced", 100));
		TexteItem aide = new TexteItem("Vous devez ajouter au moins 2 joueurs pour lancer la partie.", "Monospaced", 25);
		aide.setItalique(true);
		this.menu.ajouter(aide);
		aide = new TexteItem("X pour passer d'un niveau à l'autre.", "Monospaced", 25);
		aide.setItalique(true);
		this.menu.ajouter(aide);
		this.menu.ajouterEspaceVide();
		this.menu.ajouterEspaceVide();
		this.menu.ajouterEspaceVide();
		this.menu.ajouter(this.jouer_bouton = new BoutonItem("Jouer !", "Monospaced", 70) {
			public void surClic() {
				jouer();
			}
		});
		this.menu.ajouter(this.retour_bouton = new BoutonItem("Retour", "Monospaced", 70) {
			public void surClic() {
				retour();
			}
		});

		this.menu_joueurs = new ConfigurationJoueurMenu[Rendu.COULEURS_PIONS_DEFAUT.length];
		this.types = new int[this.menu_joueurs.length];
		for (int i = 0; i < this.menu_joueurs.length; i++)
			this.menu_joueurs[i] = new ConfigurationJoueurMenu(Rendu.COULEURS_PIONS_DEFAUT[i]);
		this.selectionne = 0;
		this.menu_joueurs[this.selectionne].setSelectionne(true);
		this.jouer_bouton.setEstActif(false);
	}

	public void setConfig(int nbLignes, int nbColonnes, int nbPuissance) {
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		this.nbPuissance = nbPuissance;
		this.selectionne = 0;
		this.menu_joueurs[this.selectionne].setSelectionne(true);
		this.menu.deselectionner();
	}

	private void jouer() {
		Joueur[] joueurs = new Joueur[this.menu_joueurs.length];
		for (int i = 0; i < this.menu_joueurs.length; i++)
			switch (this.menu_joueurs[i].getType())
			{
				default:
					joueurs[i] = null;
					break;

				case 1:
					joueurs[i] = new JoueurNormal(this.fenetre);
					break;

				case 2:
				case 3:
				case 4:
				case 5:
					joueurs[i] = new VraiIA(500, 1000, 10, this.menu_joueurs[i].getType() - 1, false);
					break;

				case 6: // Inutilisable : demande trop de ressources...
					joueurs[i] = new VraiIA(500, 1000, 10, -1, false);
					break;
			}
		this.fenetre.nouvellePartie(new ConfigurationPartie(
			this.nbLignes,
			this.nbColonnes,
			this.nbPuissance,
			joueurs
		));
		this.fenetre.modeJeu();
	}
	private void retour() {
		this.fenetre.changerEcran(this.fenetre.menuNouvellePartie);
	}

	@Override
	public void mettreAJour() {
		Entree clavier = this.fenetre.getClavier();
		if (this.selectionne != -1)
		{
			this.menu_joueurs[this.selectionne].mettreAJour(clavier);
			if (this.types[this.selectionne] != this.menu_joueurs[this.selectionne].getType())
			{
				if (this.types[this.selectionne] == 0)
					this.nbActifs++;
				else if (this.menu_joueurs[this.selectionne].getType() == 0)
					this.nbActifs--;
				this.jouer_bouton.setEstActif(this.nbActifs > 1);
				this.types[this.selectionne] = this.menu_joueurs[this.selectionne].getType();
			}
			boolean entreeEnfonce = clavier.entree(DUREE_TOUCHE);
			//boolean echapEnfonce = clavier.echap(DUREE_TOUCHE);
			boolean droiteEnfonce = clavier.droite(DUREE_TOUCHE),
					gaucheEnfonce = clavier.gauche(DUREE_TOUCHE);
			if (droiteEnfonce || gaucheEnfonce)
			{
				this.menu_joueurs[this.selectionne].setSelectionne(false);
				this.selectionne = (this.selectionne + (gaucheEnfonce ? (this.menu_joueurs.length - 1) : 1)) % this.menu_joueurs.length;
				this.menu_joueurs[this.selectionne].setSelectionne(true);
			}
			else if (entreeEnfonce)
			    //else if (echapEnfonce)
			{
				this.menu_joueurs[this.selectionne].setSelectionne(false);
				this.selectionne = -1;
				this.menu.selectionner(this.jouer_bouton.estActif() ? 6 : 7);
			}
		}
		else
		{
			this.menu.mettreAJour(clavier);
			if (clavier.gauche(DUREE_TOUCHE) || clavier.echap(DUREE_TOUCHE))
			    //if (clavier.echap(DUREE_TOUCHE))
			{
				this.selectionne = 0;
				this.menu_joueurs[this.selectionne].setSelectionne(true);
				this.menu.deselectionner();
			}
		}
	}

	protected void dessiner(int opacite, Graphics2D graphics)
	{
		for (int i = 0; i < this.menu_joueurs.length; i++)
			this.menu_joueurs[i].afficher(new Point(this.fenetre.surface.milieu.x + (int)(this.fenetre.surface.longueur * (((i + 0.5) - this.menu_joueurs.length / 2) / this.menu_joueurs.length)), this.fenetre.surface.milieu.y - 50), opacite, graphics);
		this.menu.afficher(new Point(this.fenetre.surface.milieu.x, this.fenetre.surface.milieu.y + 50), opacite, graphics);
	}
}
