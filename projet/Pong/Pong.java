import MG2D.*;

import MG2D.geometrie.Cercle;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texture;
import MG2D.geometrie.Texte;
import MG2D.Couleur;

import java.awt.Color;
import java.awt.Font;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JPanel;
import java.util.ArrayList;


import MG2D.audio.*;




public class Pong {

    //CONSTANTES
    // private int largeur = 1366;  //PC THEO (HACKED BY RACLETTTT)
    // private int hauteur = 768;	 //PC THEO (HACKED BY RACLETTTT)
    private int largeur = 1280;
	private int hauteur = 1024;
    private final int epaisseurLigne =10;
    private final int rayonBalle = 20;
    private final int vitesseRaquette = 6;
    private final int vitesseBalleInitiale = 6;


    //ATTRIBUTS	
    private int vitesseBalle;
    
    private int status;
    
    private int largeurItem = 300, hauteurItem = 50;;
    private int margeSelection = 10;
    
    private int yItems = 200;
    
    private FenetrePleinEcran f;
    
    private ClavierBorneArcade clavier;

    private Point centreBalle;
    
    private Cercle balle;

    // bordure de la map
    private Rectangle limiteGauche, limiteDroite, limiteHaute, limiteBasse, exit, jouer, selection;

    static boolean demarrer;

    private int dx,dy;

    private int scoreG, scoreD;

    private Point posRaqGH, posRaqGB, posRaqDH, posRaqDB;

    private Rectangle raqG,raqD;

    private int tailleRaquette;
    
    private Rectangle[] boutonMenu = new Rectangle[2];

    private Texture textureScoreG, textureScoreD;
    private Texture[] scoreImg;

    static int nbRebond;
    

    
    private int pointeur = 0;

    private Musique m;

    //CONSTRUCTEUR
    public Pong(){
    	f = new FenetrePleinEcran ( "ping ping pong");
    	f.setVisible(true);
    	f.setBackground(Color.BLACK);
    	clavier = new ClavierBorneArcade();
    	f.addKeyListener(clavier);
    	f.getP().addKeyListener(clavier);
    	
    	
 	  
    	jouer = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems),largeurItem,hauteurItem, true);
    	exit = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems-2*hauteurItem),largeurItem,hauteurItem, true);
    	selection = new Rectangle(Couleur.ROUGE, new Point((largeur-largeurItem)/2-margeSelection,yItems-margeSelection),largeurItem+2*margeSelection,hauteurItem+2*margeSelection, true);
 		f.ajouter(selection);
    	this.boutonMenu[0]= jouer;
    	this.boutonMenu[1]=exit;
    	f.ajouter(boutonMenu[0]);
    	f.ajouter(boutonMenu[1]);
    	
    	
    	Texte texteJouer=new Texte(Couleur.NOIR,"PLAY",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
    	texteJouer.setA(new Point(((largeur-texteJouer.getLargeur())/2),yItems+(hauteurItem-texteJouer.getHauteur())/2));
 	    
    	f.ajouter(texteJouer);
 	    
 	    Texte texteExit=new Texte(Couleur.NOIR,"EXIT",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
 	   texteExit.setA(new Point(((largeur-texteExit.getLargeur())/2),(yItems+hauteurItem-texteExit.getHauteur())/2));
 	    f.ajouter(texteExit);
 	    
 	    Texte textPong=new Texte(Couleur.BLANC,"PONG",new Font("Arial", Font.TYPE1_FONT, 250),new Point());
 	   textPong.setA(new Point(((largeur-textPong.getLargeur())/2),(((hauteur-textPong.getHauteur())/2)+100 )));
	    f.ajouter(textPong);
    	
 	  /*  if(clavier.getJoyJ1HautEnfoncee()) {
 	    	if(this.Pointeur == 1) {
 	    		selection = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem2)/2,yItems-2*hauteurItem2),largeurItem2,hauteurItem2, true);
 	    		f.ajouter(selection);
 	    	}
 	    }*/
    	
    	
    	
    }
   public void generateMenu() {
	f.effacer();
   	f.setVisible(true);
   	f.setBackground(Color.BLACK);
   	clavier = new ClavierBorneArcade();
   	f.addKeyListener(clavier);
   	f.getP().addKeyListener(clavier);
   	
   	
	  
   	jouer = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems),largeurItem,hauteurItem, true);
	
   	exit = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems-2*hauteurItem),largeurItem,hauteurItem, true);
	
   	selection = new Rectangle(Couleur.ROUGE, new Point((largeur-largeurItem)/2-margeSelection,yItems-margeSelection),largeurItem+2*margeSelection,hauteurItem+2*margeSelection, true);
	
	f.ajouter(selection);
	
   	this.boutonMenu[0]= jouer;
   	this.boutonMenu[1]=exit;
	
   	f.ajouter(boutonMenu[0]);
   	f.ajouter(boutonMenu[1]);
   	
   	
   	Texte texteJouer=new Texte(Couleur.NOIR,"PLAY",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
	f.ajouter(texteJouer);
   	texteJouer.setA(new Point(((largeur-texteJouer.getLargeur())/2),yItems+(hauteurItem-texteJouer.getHauteur())/2));
	f.supprimer(texteJouer);
   	f.ajouter(texteJouer);
	    
	Texte texteExit=new Texte(Couleur.NOIR,"EXIT",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
	f.ajouter(texteExit);
	texteExit.setA(new Point(((largeur-texteExit.getLargeur())/2),(yItems+hauteurItem-texteExit.getHauteur())/2));
	f.supprimer(texteExit);
	f.ajouter(texteExit);
	    
	Texte textPong=new Texte(Couleur.BLANC,"PONG",new Font("Arial", Font.TYPE1_FONT, 250),new Point());
	f.ajouter(textPong);
	textPong.setA(new Point(((largeur-textPong.getLargeur())/2),(((hauteur-textPong.getHauteur())/2)+100 )));
   f.supprimer(textPong);
	f.ajouter(textPong);
	f.rafraichir();
   }

    public int majMenu() {
    	int status = 0;
    
    	 if(clavier.getJoyJ1HautTape() && this.pointeur >0) {
    	
  	    	this.pointeur--;
  	    	selection.translater(0, hauteurItem*2);
  	    	
  	    	
  	    }
    	 if(clavier.getJoyJ1BasTape() && this.pointeur < (this.boutonMenu.length-1)) {
     		
   	    	this.pointeur++;
   	    	selection.translater(0, -hauteurItem*2);
   	    
   	    	
   	    }
    	 if(clavier.getBoutonJ1ATape()){
    		if(pointeur==1) {
	    	    System.exit(5);
    		}
    		else {
    			GeneratejeuPong();
	    		this.status = 1;
    		}
	    }
    	f.rafraichir();
    	return status;
    	
    }

    //UN PAS DU JEU
    public void maj(){
    	
    	if(nbRebond == 1){
	    tailleRaquette = tailleRaquette - (tailleRaquette /10);

	    raqG.setHauteur(tailleRaquette);
	    raqD.setHauteur(tailleRaquette);

	    nbRebond = 0;
	    System.out.println(tailleRaquette);
	}	   

	if(!demarrer && clavier.getBoutonJ1ATape() && scoreG<9 && scoreD<9){
	    demarrer=true;
	    dx=1;dy=1;
	}
		

	if(clavier.getJoyJ1BasEnfoncee()){
	    if(!raqG.intersectionRapide(limiteBasse)){
		raqG.translater(0, -vitesseRaquette);
	    }
	}

	if(clavier.getJoyJ1HautEnfoncee()){
	    if(!raqG.intersectionRapide(limiteHaute)){
		raqG.translater(0, +vitesseRaquette);
	    }
	}

	if(clavier.getJoyJ2BasEnfoncee()){
	    if(!raqD.intersectionRapide(limiteBasse)){
		raqD.translater(0, -vitesseRaquette);
	    }
	}

	if(clavier.getJoyJ2HautEnfoncee()){
	    if(!raqD.intersectionRapide(limiteHaute)){
		raqD.translater(0, +vitesseRaquette);
	    }
	}

	if(clavier.getBoutonJ1ZTape()){
	    System.exit(5);
	}
	    
	if(balle.intersectionRapide(limiteGauche)){
	    scoreD++;
	    f.supprimer(raqG);
	    f.supprimer(raqD);
	    tailleRaquette=hauteur/5;
	    init();
	}
	if(balle.intersectionRapide(limiteDroite)){
	    scoreG++;
	    f.supprimer(raqG);
	    f.supprimer(raqD);
	    tailleRaquette=hauteur/5;
	    init();
	}
	if( (balle.intersectionRapide(raqG) && dx<0 )|| (balle.intersectionRapide(raqD) && dx>0)){
		dx*=-1;
	    nbRebond++;
	    Bruitage b=new Bruitage("bip.mp3");
	    b.lecture();
	}
	if(balle.intersectionRapide(limiteBasse))
	    dy=1;
	if(balle.intersectionRapide(limiteHaute))
	    dy=-1;
	
	

	balle.translater(dx*vitesseBalle,dy*vitesseBalle);
		
	f.rafraichir();
	

    	
    }	

    public void reloadScore(int scoreG, int scoreD) {
    	f.supprimer(textureScoreG);
    	f.supprimer(textureScoreD);
    	textureScoreG = new Texture(scoreImg[scoreG]);
    	textureScoreD = new Texture(scoreImg[scoreD]);

		textureScoreG.setA(new Point(largeur/4, hauteur-epaisseurLigne*2-scoreImg[0].getHauteur()-50));
		textureScoreD.setA(new Point(3*largeur/4, hauteur-epaisseurLigne*2-scoreImg[0].getHauteur()-50));
		f.ajouter(textureScoreG);
		f.ajouter(textureScoreD);
		
    
    }
    
    public void GeneratejeuPong() {
    	f.effacer();
    	clavier = new ClavierBorneArcade();
    	f.addKeyListener(clavier);
    	f.getP().addKeyListener(clavier);
    	       
    	f.ajouter( new Rectangle(Couleur.NOIR, new Point(0,0), new Point(largeur, hauteur), true) );

    	limiteGauche = new Rectangle (Couleur.ROUGE, new Point(0,0), new Point(epaisseurLigne,hauteur), true);
    	f.ajouter(limiteGauche);
    	limiteDroite = new Rectangle (Couleur.ROUGE, new Point(largeur-epaisseurLigne,0), new Point(largeur,hauteur), true);
    	f.ajouter(limiteDroite);

    	limiteBasse = new Rectangle (Couleur.BLANC, new Point(0,0), new Point(largeur,epaisseurLigne), true);
    	f.ajouter(limiteBasse);
    	limiteHaute = new Rectangle (Couleur.BLANC, new Point(0,hauteur-epaisseurLigne), new Point(largeur,hauteur), true);
    	f.ajouter(limiteHaute);
    		
    	Rectangle filet = new Rectangle (Couleur.BLANC, new Point(largeur/2-epaisseurLigne/2,0), new Point(largeur/2+epaisseurLigne/2,hauteur), true);
    	f.ajouter(filet);

    	centreBalle = new Point(largeur/2,hauteur/2);
    	balle = new Cercle (Couleur.JAUNE, centreBalle, rayonBalle , true);
    	f.ajouter(balle);

    	tailleRaquette=hauteur/5;

    	scoreImg = new Texture[10];
    	for (int i=0;i<scoreImg.length;i++) {
    		scoreImg[i] = new Texture("projet/Pong/img/"+i+".png", new Point(-500,-500));
    	}

    	scoreG=0;
    	scoreD=0;

    	init();

    	m=new Musique("Tied_Up.mp3");
    	m.lecture();

        }

        //INITIALISATION D'UNE PARTIE
    public void init(){
    	demarrer=false;
    	balle.setO(new Point(largeur/2,(int)((Math.random()*(hauteur*0.5))+hauteur/4)));

    	posRaqGH = new Point(epaisseurLigne*4,hauteur/2+tailleRaquette/2);
            posRaqGB = new Point(epaisseurLigne*2,hauteur/2-tailleRaquette/2);
            raqG=new Rectangle(Couleur.BLANC,posRaqGB,posRaqGH, true);
            f.ajouter(raqG);

    	
    	posRaqDH = new Point(largeur-2*epaisseurLigne,hauteur/2+tailleRaquette/2);
    	posRaqDB = new Point(largeur-4*epaisseurLigne,hauteur/2-tailleRaquette/2);
    	raqD=new Rectangle(Couleur.BLANC, posRaqDB, posRaqDH, true);
    	f.ajouter(raqD);
    		
    	dx=0;
    	dy=0;
    	nbRebond=0;
    	vitesseBalle=vitesseBalleInitiale;
    	reloadScore(scoreG, scoreD);
    		
	if(scoreG==9){
	    enregistrerHighScore("J1", scoreG);
	    Texte t=new Texte(Couleur.ROUGE,"Le joueur de gauche a gagné !!!",new Font("Calibri", Font.TYPE1_FONT, 40),new Point(largeur/2,hauteur/2));
	    f.ajouter(t);
			f.rafraichir();
	    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    generateMenu();
    	    this.status=0;
    	    
    	}
    	if(scoreD==9){
	    enregistrerHighScore("J2", scoreD);
    	    Texte t=new Texte(Couleur.ROUGE,"Le joueur de droite a gagné !!!",new Font("Calibri", Font.TYPE1_FONT, 40),new Point(largeur/2,hauteur/2));
    	    f.ajouter(t);
			f.rafraichir();
    	    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    generateMenu();
    	    this.status=0;
    	}
    	f.rafraichir();
		
		
	}

    private void enregistrerHighScore(String nom, int score) {
	ArrayList<LigneHighScore> list = HighScore.lireFichier("highscore");
	HighScore.enregistrerFichier("highscore", list, nom, score);
    }
        
    public int getStatus() {
    	return this.status;
    }

}

	 
