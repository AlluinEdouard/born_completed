import MG2D.*;
import MG2D.geometrie.*;

/***************************************************************************************/
/* Les joysticks et boutons pour la borne d'arcade correspondent :                     */
/* Pour le Joueur 1 : flèches haut/bas/gauche/droite et les touches f, g, h, r, t et y */
/* Pour le joueur 2 : o, k, l et m puis les touches q, s, d, a, z et e                 */
/***************************************************************************************/


class TestClavierBorneArcade{

    public static void main(String[] args){
	Fenetre f = new Fenetre("Test du clavier pour la borne d'arcade",1000,260);
	ClavierBorneArcade clavier = new ClavierBorneArcade();
	f.addKeyListener(clavier);

	Cercle joy1Haut = new Cercle(Couleur.GRIS_CLAIR,new Point(100,200),30,true);
	f.ajouter(joy1Haut);
	Cercle joy1Bas = new Cercle(Couleur.GRIS_CLAIR,new Point(100,80),30,true);
	f.ajouter(joy1Bas);
	Cercle joy1Gauche = new Cercle(Couleur.GRIS_CLAIR,new Point(50,140),30,true);
	f.ajouter(joy1Gauche);
	Cercle joy1Droite = new Cercle(Couleur.GRIS_CLAIR,new Point(150,140),30,true);
	f.ajouter(joy1Droite);

	Cercle bouton1A = new Cercle(Couleur.GRIS_CLAIR,new Point(270,80),30,true);
	f.ajouter(bouton1A);
	Cercle bouton1B = new Cercle(Couleur.GRIS_CLAIR,new Point(350,120),30,true);
	f.ajouter(bouton1B);
	Cercle bouton1C = new Cercle(Couleur.GRIS_CLAIR,new Point(450,100),30,true);
	f.ajouter(bouton1C);

	Cercle bouton1X = new Cercle(Couleur.GRIS_CLAIR,new Point(250,160),30,true);
	f.ajouter(bouton1X);
	Cercle bouton1Y = new Cercle(Couleur.GRIS_CLAIR,new Point(330,200),30,true);
	f.ajouter(bouton1Y);
	Cercle bouton1Z = new Cercle(Couleur.GRIS_CLAIR,new Point(430,180),30,true);
	f.ajouter(bouton1Z);




	Cercle joy2Haut = new Cercle(Couleur.GRIS_CLAIR,new Point(600,200),30,true);
	f.ajouter(joy2Haut);
	Cercle joy2Bas = new Cercle(Couleur.GRIS_CLAIR,new Point(600,80),30,true);
	f.ajouter(joy2Bas);
	Cercle joy2Gauche = new Cercle(Couleur.GRIS_CLAIR,new Point(550,140),30,true);
	f.ajouter(joy2Gauche);
	Cercle joy2Droite = new Cercle(Couleur.GRIS_CLAIR,new Point(650,140),30,true);
	f.ajouter(joy2Droite);

	Cercle bouton2A = new Cercle(Couleur.GRIS_CLAIR,new Point(770,80),30,true);
	f.ajouter(bouton2A);
	Cercle bouton2B = new Cercle(Couleur.GRIS_CLAIR,new Point(850,120),30,true);
	f.ajouter(bouton2B);
	Cercle bouton2C = new Cercle(Couleur.GRIS_CLAIR,new Point(950,100),30,true);
	f.ajouter(bouton2C);

	Cercle bouton2X = new Cercle(Couleur.GRIS_CLAIR,new Point(750,160),30,true);
	f.ajouter(bouton2X);
	Cercle bouton2Y = new Cercle(Couleur.GRIS_CLAIR,new Point(830,200),30,true);
	f.ajouter(bouton2Y);
	Cercle bouton2Z = new Cercle(Couleur.GRIS_CLAIR,new Point(930,180),30,true);
	f.ajouter(bouton2Z);

	f.ajouter(new Ligne(Couleur.NOIR, new Point(500,0), new Point(500,260)));

	while(true){
	    try{
		Thread.sleep(1);
	    }catch(Exception e){}
	    
	    if(clavier.getJoyJ1HautEnfoncee())
		joy1Haut.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy1Haut.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getJoyJ1BasEnfoncee())
		joy1Bas.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy1Bas.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getJoyJ1GaucheEnfoncee())
		joy1Gauche.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy1Gauche.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getJoyJ1DroiteEnfoncee())
		joy1Droite.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy1Droite.setCouleur(Couleur.GRIS_CLAIR);

	    if(clavier.getBoutonJ1AEnfoncee())
		bouton1A.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton1A.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ1BEnfoncee())
		bouton1B.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton1B.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ1CEnfoncee())
		bouton1C.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton1C.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ1XEnfoncee())
		bouton1X.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton1X.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ1YEnfoncee())
		bouton1Y.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton1Y.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ1ZEnfoncee())
		bouton1Z.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton1Z.setCouleur(Couleur.GRIS_CLAIR);








	    if(clavier.getJoyJ2HautEnfoncee())
		joy2Haut.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy2Haut.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getJoyJ2BasEnfoncee())
		joy2Bas.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy2Bas.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getJoyJ2GaucheEnfoncee())
		joy2Gauche.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy2Gauche.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getJoyJ2DroiteEnfoncee())
		joy2Droite.setCouleur(Couleur.GRIS_FONCE);
	    else
		joy2Droite.setCouleur(Couleur.GRIS_CLAIR);

	    if(clavier.getBoutonJ2AEnfoncee())
		bouton2A.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton2A.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ2BEnfoncee())
		bouton2B.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton2B.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ2CEnfoncee())
		bouton2C.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton2C.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ2XEnfoncee())
		bouton2X.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton2X.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ2YEnfoncee())
		bouton2Y.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton2Y.setCouleur(Couleur.GRIS_CLAIR);
	    if(clavier.getBoutonJ2ZEnfoncee())
		bouton2Z.setCouleur(Couleur.GRIS_FONCE);
	    else
		bouton2Z.setCouleur(Couleur.GRIS_CLAIR);

	    
	    f.rafraichir();
	}
    }
    
}
