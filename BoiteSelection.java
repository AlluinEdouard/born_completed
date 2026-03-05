import java.awt.Font;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import MG2D.geometrie.Rectangle;
import MG2D.Clavier;
import MG2D.audio.*;
import java.io.File;
import MG2D.geometrie.Texte;
import MG2D.Couleur;
import MG2D.audio.Bruitage;

/**
 * Gère la logique de navigation dans le carrousel/menu des jeux.
 * Intercepte les actions du joystick pour faire défiler la liste vers le haut ou vers le bas,
 * et anime la translation des éléments graphiques associés.
 */
public class BoiteSelection extends Boite{
    Pointeur pointeur;
    Font font;

    public BoiteSelection(Rectangle rectangle, Pointeur pointeur) {
	super(rectangle);
	this.pointeur = pointeur;
    }

	/**
     * Écoute les entrées du clavier arcade pour déplacer le curseur de sélection.
     * Gère la boucle de navigation (retour au début si fin de liste atteinte).
     * @param clavier L'instance du clavier de la borne d'arcade.
     * @return false si le joueur appuie sur le bouton pour quitter (Z), true sinon.
     */
    public boolean selection(ClavierBorneArcade clavier){
	Bruitage selection = new Bruitage("sound/bip.mp3");
	font = null;
	try{
	    File in = new File("fonts/PrStart.ttf");
	    font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(26.0f);
	}catch (Exception e) {
	    System.out.println(e.getMessage());
	    font = new Font("SansSerif", Font.PLAIN, 26);
	}
	
	//Modifier le 07/11/2019 pour améliorer la navigation
	/*
	*	BACK:
	*	repasse au premier élément du tableau lorsque la valeur du pointeur est égale à la 	*	taille du tableau-1
	*
	*	FRONT:
	*	descend au dernier jeux de la liste afficher sur le menu  
	*/
	if(clavier.getJoyJ1HautTape() &&( pointeur.getValue() <= Graphique.tableau.length - 1)){
		if(Graphique.textesAffiches[pointeur.getValue()]==false){
			Graphique.afficherTexte(pointeur.getValue());
			Graphique.textesAffiches[pointeur.getValue()]=true;
		}
	    selection.lecture();
		if(pointeur.getValue() == Graphique.tableau.length -1){
			pointeur.setValue(0);
				for(int i = 0 ; i < Graphique.tableau.length ; i++){
					Graphique.tableau[i].getTexte().translater(0, 110*(Graphique.tableau.length -1));
					Graphique.tableau[i].getTexture().translater(0, 110*(Graphique.tableau.length -1));
					Graphique.tableau[i].getTexte().setPolice(font);
					Graphique.tableau[i].getTexte().setCouleur(Couleur.BLANC);
				}
		}else{
			for(int i = 0 ; i < Graphique.tableau.length ; i++){
				Graphique.tableau[i].getTexte().translater(0, -110);
				Graphique.tableau[i].getTexture().translater(0, -110);
				Graphique.tableau[i].getTexte().setPolice(font);
				Graphique.tableau[i].getTexte().setCouleur(Couleur.BLANC);
			}
			pointeur.setValue(pointeur.getValue() + 1);
		}	
	}
	//Modifier le 07/11/2019 pour améliorer la navigation
	/*
	*	BACK:
	*	repasse au dernier élément du tableau lorsque la valeur du pointeur est égale à 0
	*
	*	FRONT:
	*	Remonte au premier jeux de la liste afficher sur le menu 
	*/
	if(clavier.getJoyJ1BasTape() && pointeur.getValue() >= 0){
		if(Graphique.textesAffiches[pointeur.getValue()]==false){
			Graphique.afficherTexte(pointeur.getValue());
			Graphique.textesAffiches[pointeur.getValue()]=true;
		}
	    try{
			selection.lecture();
	}catch(Exception e){}
			if(pointeur.getValue() == 0){
				pointeur.setValue(Graphique.tableau.length-1);	
				for(int i = 0 ; i < Graphique.tableau.length ; i++){
					Graphique.tableau[i].getTexte().translater(0, -110*(Graphique.tableau.length-1));
					Graphique.tableau[i].getTexture().translater(0, -110*(Graphique.tableau.length-1));
					Graphique.tableau[i].getTexte().setPolice(font);
					Graphique.tableau[i].getTexte().setCouleur(Couleur.BLANC);
						
				}
			}else{
				for(int i = 0 ; i < Graphique.tableau.length ; i++){
					Graphique.tableau[i].getTexte().translater(0, 110);
					Graphique.tableau[i].getTexture().translater(0, 110);
					Graphique.tableau[i].getTexte().setPolice(font);
					Graphique.tableau[i].getTexte().setCouleur(Couleur.BLANC);
						
				}
			
				pointeur.setValue(pointeur.getValue() -1);	
				System.out.println(pointeur.getValue());		
			}
	}
	

	if(clavier.getBoutonJ1ZTape()){
	    return false;
	}
	return true;
    }

    public Pointeur getPointeur() {
	return pointeur;
    }

    public void setPointeur(Pointeur pointeur) {
	this.pointeur = pointeur;
    }

}
