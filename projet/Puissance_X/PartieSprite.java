import java.awt.Graphics;
import java.awt.Graphics2D;

import MG2D.*;
import MG2D.geometrie.*;

class PartieSprite extends Dessin {
	private Plateau plateau;
	private Rendu rendu;
	private int nbTours;

	public PartieSprite(int longueur, int largeur) {
		this.rendu = new Rendu(longueur, largeur);
	}

	public int getNbTours() {
		return this.nbTours;
	}

	public boolean estTerminee() {
		return this.plateau != null && (this.plateau.gagne() != -1);
	}
	public int getGagnant() {
		return this.plateau == null ? -1 : this.plateau.gagne();
	}

	public Rendu getRendu() {
		return this.rendu;
	}

	//
	// CONFIGURATION
	//

	private ConfigurationPartie config;

	public void nouvellePartie() {
		if (this.config == null)
			throw new IllegalStateException("Ce object PartieSprite n'a pas encore de configuration. Pour appeler cette méthode, vous devez d'abord appeler « PartieSprite.nouvellePartie(ConfigurationPartie configuration) ».");
		this.nouvellePartie(this.config);
	}
	public void nouvellePartie(ConfigurationPartie config) {
		if (!config.estValide())
			throw new IllegalArgumentException("La configuration de la nouvelle partie à créer doit être valide. Appelez d'abord « ConfigurationPartie.estValide() » pour savoir si la configuration est valide.");
		synchronized (this.rendu)
		{
			this.config = new ConfigurationPartie(config);
			this.rendu.nouvellePartie(config.getNbColonnes(), config.getNbLignes());
			this.plateau = new Plateau(config.getNbColonnes(), config.getNbLignes(), config.getNbPuissance());
			for (int i = 1; i <= this.config.getNbJoueurs(); i++)
				if (this.config.getJoueur(i) != null)
					this.config.getJoueur(i).nouvellePartie(i, this.config);
			this.noJoueurActuel = 1;
			while (this.config.getJoueur(this.noJoueurActuel) == null)
				this.noJoueurActuel = (this.noJoueurActuel % this.config.getNbJoueurs()) + 1;
			this.nbTours = 0;
			this.rendu.setJoueurActuel(this.noJoueurActuel);
		}
	}

	public ConfigurationPartie getConfiguration() { return this.config; }

	//
	// DEROULEMENT DU JEU
	//

	private int noJoueurActuel, colonneChoisie, ligneAjout;

	public void mettreAJour() {
		if (this.plateau == null) return;
		synchronized (this.rendu)
		{
			if (this.plateau.gagne() == -1)
			{
				this.colonneChoisie = this.config.getJoueur(this.noJoueurActuel).choisirColonne(this.plateau);
				if (this.colonneChoisie != 0)
				{
					this.rendu.setColonneActuelle(Math.abs(this.colonneChoisie) - 1);
					if (this.colonneChoisie > 0)
					{
						this.ligneAjout = this.plateau.ajoutPion(this.colonneChoisie - 1, this.noJoueurActuel);
						if (this.ligneAjout != -1)
						{
							this.nbTours++;
							this.rendu.ajoutPionReussi(this.ligneAjout);
							do
							{
								this.noJoueurActuel = (this.noJoueurActuel % this.config.getNbJoueurs()) + 1;
							} while (this.config.getJoueur(this.noJoueurActuel) == null);
							this.rendu.setJoueurActuel(this.noJoueurActuel);
						}
					}
				}
			}
		}
	}

	//
	// MÉTHODES HÉRITÉS DE « MG2D.geometrie.Dessin » 
	// NOTE : Seule la méthode « afficher(Graphics g) » est nécessaire, mais comme ces 4 méthodes sont abstraites ont a pas le choix de les écrires (et on ne parle pas de « toString() »)...
	//

	public BoiteEnglobante getBoiteEnglobante() { return new BoiteEnglobante(new MG2D.geometrie.Point(0, 0), new MG2D.geometrie.Point(this.rendu.getLongueur(), this.rendu.getLargeur())); }
	public void translater(int dx, int dy) { /* Désolé, ce objet est trop grand pour être pour être déplacé... */ }
	public void afficher(Graphics g) { if (this.plateau == null) return; synchronized (this.rendu) { if (this.config != null) this.rendu.mettreAJour(this.plateau, (Graphics2D)g); } }
	public String toString() { return this.plateau.toString(); }
}