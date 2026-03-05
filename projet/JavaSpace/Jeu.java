import MG2D.*;
import MG2D.geometrie.*;
import MG2D.Clavier;
import java.util.ArrayList;
import java.util.Random; 
import java.awt.Font;
import java.util.Date;
import java.io.File;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


class Jeu{

	public static final int LARGEUR=1280;
	public static final int HAUTEUR=1024;


    public static final int PHASESHOOT=1;
    public static final int PHASEBOSS=2;

    public static final int VIEBOSS=50;

    //TODO : ajuster temps phase!!!
    public static final int TEMPSPHASE=1500;


    //CONSTANTES DE TEXTURES
    public static final Texture TEXTIRBOSS1 = new Texture("./img/laser/boss/1.png",new Point());
    public static final Texture TEXTIRBOSS2 = new Texture("./img/laser/boss/2.png",new Point());

    public static final Texture TEXTIRENN = new Texture("./img/laser/ennemie1/1.png",new Point());

    public static final Texture TEXTIR[] = new Texture[4];

    public static final Texture TEXVAISSEAU[] = new Texture[4];

    public static final Texture TEXBOSS = new Texture("./img/ennemie/boss/0.png",new Point(LARGEUR,HAUTEUR));

    public static final Texture TEXENN[] = new Texture[4];

    public static final int APPARITION=0;
    public static final int COMBAT=1;
    public static final int DESTRUCTION=2;




    /* Attribut */
    private Joueur jou;
    private Texture fond1;
    private Texture fond2;
    private Rectangle barreNoir;
    private Font font;
	
	private int largeur = 1366;  
    private int hauteur = 768;
    private int yItems = 120;
    private int largeurItem = 300, hauteurItem = 50;
    private int margeSelection = 10;
	private Texture menubg;

	private int phase;
	private int compteur;

	private int phaseBoss;
	
	
	//Permet de faire clignoter le sprite du joueur
	private boolean joueurClignote;


    private ArrayList<Ennemi> tabEnn;//Tableau pour les ennemis
    private ArrayList<Tir> tabTirJou;//Tableau pour les tirs ennemis
    private ArrayList<Tir> tabTirEnn;//Tableau pour les tirs joueurs
    private ArrayList<Bonus> tabBonus;//Tableau pour les bonus
    private ArrayList<Texture> tabAnimationIntersection;//Tableau pour les intersections tir joueur/ennemi
	private Boss boss;

    private Fenetre fen;
    //private FenetrePleinEcran fen;
    private ClavierBorneArcade cla;
    private int score, pointeur = 0;
    //private Texture scoreAffichage[];
    private Texte scoreAffichage;
    private Texture nombreVie[];
    private Texte affichageNombreVieJoueur;
    private Random r;
	private Rectangle[] boutonMenu = new Rectangle[2];
    private Rectangle exit, jouer, selection;
	
	private int status;
	

public Jeu(){
		font=null;
		try{
			File in = new File("./fonts/PrStart.ttf");
			font = font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(32.0f);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		for(int i=0;i<4;i++){
			TEXTIR[i]=new Texture("./img/laser/player1/"+i+".png",new Point());
			TEXENN[i]=new Texture("./img/ennemie/ennemie1/"+i+".png",new Point());
			TEXVAISSEAU[i]=new Texture("./img/life/damage/"+i+".png",new Point());
		}
      	fen = new FenetrePleinEcran ( "JavaSpace");
      	// fen = new Fenetre ( "JavaSpace",LARGEUR,HAUTEUR);
    	fen.setVisible(true);
		fen.setAffichageFPS(true);
		fen.setAffichageNbPrimitives(true);
    	//fen.setBackground(Color.BLACK);
    	cla = new ClavierBorneArcade();
    	fen.addKeyListener(cla);
    	fen.getP().addKeyListener(cla);

    	this.phase=PHASESHOOT;

		generateMenu();
 	    
 	   
    	
 	  /*  if(clavier.getJoyJ1HautEnfoncee()) {
 	    	if(this.Pointeur == 1) {
 	    		selection = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem2)/2,yItems-2*hauteurItem2),largeurItem2,hauteurItem2, true);
 	    		f.ajouter(selection);
 	    	}
 	    }*/
    	
    	
    	
    
    }

    //AJOUT D'UN MENU PAR THEO DUBOIS (2020)
    public void generateMenu() {
    fen.effacer();
	fen.setVisible(true);
	menubg = new Texture("./img/title/background.JPG",new Point(0,0));
	fen.ajouter(menubg);
	cla = new ClavierBorneArcade();
	fen.addKeyListener(cla);
	fen.getP().addKeyListener(cla);
	
	  
	 jouer = new Rectangle(Couleur.GRIS, new Point((largeur-largeurItem)/2,yItems),largeurItem,hauteurItem, true);
	 exit = new Rectangle(Couleur.GRIS, new Point((largeur-largeurItem)/2,yItems-2*hauteurItem),largeurItem,hauteurItem, true);
	 selection = new Rectangle(Couleur.VERT, new Point((largeur-largeurItem)/2-margeSelection,yItems-margeSelection),largeurItem+2*margeSelection,hauteurItem+2*margeSelection, true);
	fen.ajouter(selection);
	this.boutonMenu[0]= jouer;
	this.boutonMenu[1]=exit;
	fen.ajouter(boutonMenu[0]);
	fen.ajouter(boutonMenu[1]);
	

	Texte texteJouer = new Texte(Couleur.NOIR,"PLAY",font,new Point());
	texteJouer.setA(new Point((largeur/2),yItems+hauteurItem/2));
	    
	fen.ajouter(texteJouer);
	    
	    Texte texteExit=new Texte(Couleur.NOIR,"EXIT",font,new Point());
	   texteExit.setA(new Point(largeur/2,yItems-2*hauteurItem+hauteurItem/2));
	    fen.ajouter(texteExit);
    
    
	
	  /*  if(clavier.getJoyJ1HautEnfoncee()) {
	    	if(this.Pointeur == 1) {
	    		selection = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem2)/2,yItems-2*hauteurItem2),largeurItem2,hauteurItem2, true);
	    		f.ajouter(selection);
	    	}
	    }*/
	
	
	

}

    public int majMenu() {
    	int status = 0;
        
   	 if(cla.getJoyJ1HautTape() && this.pointeur >0) {
   	
 	    	this.pointeur--;
 	    	selection.translater(0, hauteurItem*2);
 	    	
 	    	
 	    }
   	 if(cla.getJoyJ1BasTape() && this.pointeur < (this.boutonMenu.length-1)) {
    		
  	    	this.pointeur++;
  	    	selection.translater(0, -hauteurItem*2);
  	    
  	    	
  	    }
   	 if(cla.getBoutonJ1ATape()){
   		if(pointeur==1) {
	    	    System.exit(5);
   		}
   		else {
   			genererJeu();
	    		this.status = 1;
   		}
	    }
   	fen.rafraichir();
   	return status;
    	
    }

    
  
    public void genererJeu(){
    fen.effacer();
	font = null;
	try{
	    File in = new File("font.ttf");
	    font = font.createFont(Font.TRUETYPE_FONT, in);
	    font = font.deriveFont(32.0f);
	}catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	this.phase=PHASESHOOT;
	this.compteur=0;
	
	
	jou = new Joueur(new Texture(TEXVAISSEAU[3]),3,0,0,0,0,0);
	jou.getTex().setA(new Point(0,360));
	tabEnn = new ArrayList<Ennemi>();
	tabTirJou = new ArrayList<Tir>();
	tabTirEnn = new ArrayList<Tir>();
	tabBonus = new ArrayList<Bonus>();
	tabAnimationIntersection = new ArrayList<Texture>();

	//fen = new Fenetre("JAVA SPACE", 1280, 1024);
	//fen = new FenetrePleinEcran("JAVA SPACE");

	//cla = new ClavierBorneArcade();
	//fen.addKeyListener(cla);
	//fen.getP().addKeyListener(cla);
	
	//fen.setVisible(true);
    

	
	fond1 = new Texture("./img/background/1.png", new Point(0, 0), 1280, 1024);
	fond2 = new Texture("./img/background/2.png", new Point(1280, 0), 1280, 1024);
	
	barreNoir = new Rectangle(Couleur.NOIR, new Point(0,0), 1280, 1024, true);
	fen.ajouter(barreNoir);

	fen.ajouter(fond1);
	fen.ajouter(fond2);
	fen.ajouter(jou.getTex());

	scoreAffichage = new Texte(Couleur.BLANC,"0",new Font("", Font.BOLD, 20), new Point(50,900));
	scoreAffichage.setPolice(font);
	fen.ajouter(scoreAffichage);



	Texture hudVaisseau;
	hudVaisseau=new Texture("./img/life/1.png", new Point(1151, 892));
	fen.ajouter(hudVaisseau);//Symbole du vaisseau pour indiquer la vie du joueur
	affichageNombreVieJoueur=new Texte(Couleur.BLANC,"3",new Font("", Font.BOLD, 20), new Point(1210, 900));
	affichageNombreVieJoueur.setPolice(font);
	fen.ajouter(affichageNombreVieJoueur);

	r = new Random();

	fen.rafraichir();
    }






    /* Méthode */
    public void avancerUnPasDeTemps(){
	int randomApparition;
	int randomTirEnn;
	int randomBonus;
	int randomApparitionBonus;
	int randomTrajectoire;
	int randomPositionxyTrajectoire;
	int zoneApparitionEnnemi=0;

	/*------------------------------*/
	/*----------BACKGROUND----------*/
	/*------------------------------*/
	
	/*-----TRANSLATION_DES_FONDS----*/
	if(fond1.getB().getX()<=0){
	    fond1.translater(2560,0);
	}else if(fond2.getB().getX()<=0){
	    fond2.translater(2560,0);
	}
	else{
	    fond1.translater(-1,0);
	    fond2.translater(-1,0);
	}

	/*-------------------------*/
	/*----------BONUS----------*/
	/*-------------------------*/

	/*-----APPARITION_BONUS-----*/
	randomApparitionBonus = r.nextInt(1000)+1;
	if(randomApparitionBonus==1){
	    randomBonus = r.nextInt(4)+1;
	    randomPositionxyTrajectoire = r.nextInt(720)+152;
	    switch(randomBonus)
		{
		case 1: //TIR DOUBLE
		    tabBonus.add(new Bonus(new Texture("./img/bonus/1.png", new Point(1280,randomPositionxyTrajectoire)),1,1000));
		    fen.ajouter(tabBonus.get(tabBonus.size()-1).getTex());
		    break;
		case 2: //TIR TRIPLE
		    tabBonus.add(new Bonus(new Texture("./img/bonus/2.png", new Point(1280,randomPositionxyTrajectoire)),2,1000));
		    fen.ajouter(tabBonus.get(tabBonus.size()-1).getTex());
		    break;
		case 3: //TIR EVENTAIL
		    tabBonus.add(new Bonus(new Texture("./img/bonus/3.png", new Point(1280,randomPositionxyTrajectoire)),3,1000));
		    fen.ajouter(tabBonus.get(tabBonus.size()-1).getTex());
		    break;
		case 4: //VIE SUPPLEMENTAIRE
		    tabBonus.add(new Bonus(new Texture("./img/bonus/4.png", new Point(1280,randomPositionxyTrajectoire)),4,0));
		    fen.ajouter(tabBonus.get(tabBonus.size()-1).getTex());
		    break;

		default: // DEFAUT
		}
	}
	
	/*-----TRANSLATION_BONUS/INTERSECTION_BONUS-----*/
	for(int i=tabBonus.size()-1; i>0; i--){
	    tabBonus.get(i).getTex().translater(-10,0);
	    if(tabBonus.get(i).getTex().getB().getX()<0){//BONUS SORT DE L'ECRAN
		fen.supprimer(tabBonus.get(i).getTex());
		tabBonus.remove(i);
	    }
	    else if(jou.intersection(tabBonus.get(i))==true){//JOUEUR TOUCHE UN BONUS
		if(tabBonus.get(i).getNumBonus()==4){
		    if(jou.getVie()<3){//VIE DU JOUEUR INFERIEUR A TROIS MISE A JOUR DU NOMBRE DE VIE
			jou.setVie(jou.getVie()+1);
			//affichageNombreVieJoueur.setImg("./img/score/"+jou.getVie()+".png");
			affichageNombreVieJoueur.setTexte(""+jou.getVie());
			jou.getTex().setImg("./img/life/damage/"+jou.getVie()+".png");

		    }
		}else{//CAS OU LE BONUS CHANGE LE TIR DU JOUEUR
		    jou.setTir(tabBonus.get(i).getNumBonus());
		    jou.setTempBonus(tabBonus.get(i).getDuree());
		}
		fen.supprimer(tabBonus.get(i).getTex());
		tabBonus.remove(i);
	    }
	}


	/*--------------------------*/
	/*----------JOUEUR----------*/
	/*--------------------------*/
	
	/*-----TEMPORISATION_BONUS_DU_JOUEUR-----*/
	if(jou.getTempBonus()>0){//BONUS DE TIR EN COURS ON ENLEVE UN A LA DUREE 
	    jou.setTempBonus(jou.getTempBonus()-1);
	}
	if(jou.getTempBonus()==0 && jou.getTir()>0){
	    jou.setTir(0);
	}

	/*-----DEPLACEMENT_JOUEUR-----*/
	//VITESSE DE DEPLACEMENT AUGMENTEE!!!	====DORIAN
	jou.bougerJoueur(cla,0,1280,-152,872,20,20,jou.getTex().getLargeur(),jou.getTex().getHauteur());
	//jou.bougerJoueur(cla,0,1280,-152,872,12,12,jou.getTex().getLargeur(),jou.getTex().getHauteur());
	

	/*-----------------------*/
	/*----------TIR----------*/
	/*-----------------------*/

	/*-----EFFACEMENT_ANIMATION_TIR-----*/
	for(int i=tabAnimationIntersection.size()-1; i>=0; i--){//ENELEVE TOUTE LES TEXTURES D'IMPACT 
	    fen.supprimer(tabAnimationIntersection.get(i));
	    tabAnimationIntersection.remove(i);
	}
	
	/*-----TRANSLATION_TIR_ENNEMIE-----*/
	for(int i=tabTirEnn.size()-1; i>=0; i--){
	    switch(tabTirEnn.get(i).getOrientation())//TRANSLATION EN FONCTION DE L'ORIENATION DU TIR
		{
		case "centre"://TIR DROIT
		    tabTirEnn.get(i).getTex().translater(-1*tabTirEnn.get(i).getVit(),0);
		    break;
		case "gauche": //TIR GAUCHE->EVENTAIL
		    tabTirEnn.get(i).getTex().translater(-1*tabTirEnn.get(i).getVit(),2);
		    break;
		case "droite": //TIR DROITE->EVENTAIL
		    tabTirEnn.get(i).getTex().translater(-1*tabTirEnn.get(i).getVit(),-2);
		    break;
		default: // DEFAUT
		    
		}
	    if(tabTirEnn.get(i).getTex().getB().getX()<0){// SUPPRESION SI LE TIR SORT DE L'ECRAN
		fen.supprimer(tabTirEnn.get(i).getTex());
		tabTirEnn.remove(i);
	    }
	}

	/*-----APPARITION_TIR_ENNEMIE-----*/
 	for(int i=tabEnn.size()-1; i>0; i--){
	    randomTirEnn = r.nextInt(100)+1;
	    if(randomTirEnn==1){//RANDOM SI =1 ALORS L'ENNEMIE TIR
		randomTirEnn = r.nextInt(4);//CHOIX DU TIR
		int x = tabEnn.get(i).getTex().getB().getX();
		int y_AY = tabEnn.get(i).getTex().getA().getY();
		int y_BY = tabEnn.get(i).getTex().getB().getY();
		String laserPath = "./img/laser/ennemie1/1.png";
		switch(randomTirEnn)
		    {
		    case 1: //TIR DOUBLE
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA(new Point(x,y_BY-23));


			ajouterDernierTirAfenetre(tabTirEnn);
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN ),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA( new Point(x,y_AY+10)  );
			ajouterDernierTirAfenetre(tabTirEnn);
			break;
		    case 2: //TIR TRIPLE
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN ),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA(new Point(x,y_BY-19));
			ajouterDernierTirAfenetre(tabTirEnn);
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN ),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA( new Point(x,y_AY+10)  );
			ajouterDernierTirAfenetre(tabTirEnn);
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA(  new Point(x,y_BY-(jou.getTex().getHauteur()/2) ));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().translater(0,-tabTirEnn.get(tabTirEnn.size()-1).getTex().getHauteur()/2);
			ajouterDernierTirAfenetre(tabTirEnn);
			break;
		    case 3: //TIR EVENTAIL
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN ),1,20,"gauche"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA( new Point(x,y_BY-39)  );
			ajouterDernierTirAfenetre(tabTirEnn);
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN ),1,20,"droite"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA(  new Point(x,y_AY+30) );
			ajouterDernierTirAfenetre(tabTirEnn);
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN ),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA( new Point(x,y_BY-(jou.getTex().getHauteur()/2))  );
			tabTirEnn.get(tabTirEnn.size()-1).getTex().translater(0,-tabTirEnn.get(tabTirEnn.size()-1).getTex().getHauteur()/2);
			ajouterDernierTirAfenetre(tabTirEnn);
			break;

		    default: //TIR PAR DEFAUT
			tabTirEnn.add(new Tir(new Texture(TEXTIRENN),1,20,"centre"));
			tabTirEnn.get(tabTirEnn.size()-1).getTex().setA(  new Point(x,tabEnn.get(i).getTex().getB().getY()-(jou.getTex().getHauteur()/2)) );
			tabTirEnn.get(tabTirEnn.size()-1).getTex().translater(0,-tabTirEnn.get(tabTirEnn.size()-1).getTex().getHauteur()/2);
			ajouterDernierTirAfenetre(tabTirEnn);
		    }
	    }
	}

	/*-----TRANSLATION_TIR_JOUEUR-----*/
	for(int i=tabTirJou.size()-1; i>=0; i--){
		boolean touche=false;
	    switch(tabTirJou.get(i).getOrientation())//TRANSLATION EN FONCTION DE L'o
		{
		case "centre"://TIR DROIT
		    tabTirJou.get(i).getTex().translater(1*tabTirJou.get(i).getVit(),0);
		    break;
		case "gauche": //TIR GAUCHE->EVENTAIL
		    tabTirJou.get(i).getTex().translater(1*tabTirJou.get(i).getVit(),2);
		    break;
		case "droite": //TIR DROITE->EVENTAIL
		    tabTirJou.get(i).getTex().translater(1*tabTirJou.get(i).getVit(),-2);
		    break;
		default: //BONUS PAR DEFAUT
		    tabTirJou.get(i).getTex().translater(1*tabTirJou.get(i).getVit(),0);
		}
	    if(tabTirJou.get(i).getTex().getB().getX()>1280){ 
		fen.supprimer(tabTirJou.get(i).getTex());
		tabTirJou.remove(i);
		if(i==0){
		    i=0;
		}
		else
		    {
			i=i-1;
		    }
	    }
	    else
		{
		    for(int j=tabEnn.size()-1; j>=0; j--){
			if(tabTirJou.get(i).intersection(tabEnn.get(j))==true){//SI UN TIR DU JOUEUR TOUCHE UN ENNEMIE
			    tabAnimationIntersection.add(new Texture("./img/intersection/touchey.png", new Point(tabTirJou.get(i).getTex().getA().getX()+50,tabTirJou.get(i).getTex().getA().getY())));
			    fen.ajouter(tabAnimationIntersection.get(tabAnimationIntersection.size()-1));
			    fen.supprimer(tabEnn.get(j).getTex());
			    fen.supprimer(tabTirJou.get(i).getTex());
			    tabEnn.remove(j);
			    tabTirJou.remove(i);
			    score=score+25;//INCREMENTATION DU SCORE
				touche=true;
			    break;
			}
		    }
			if(this.phase==PHASEBOSS && touche==false){
				if(tabTirJou.get(i).intersection(boss)==true){//SI UN TIR DU JOUEUR TOUCHE LE BOSS
					tabAnimationIntersection.add(new Texture("./img/intersection/touchey.png", new Point(tabTirJou.get(i).getTex().getA().getX()+50,tabTirJou.get(i).getTex().getA().getY())));
					fen.ajouter(tabAnimationIntersection.get(tabAnimationIntersection.size()-1));
					boss.setVie(boss.getVie()-1);
					System.out.println("Boss touche : "+boss.getVie());
					if(boss.getVie()==0) {
						System.out.println("Boss tue!!!");
						fen.supprimer(boss.getTex());
						boss.getTex().supprimeHitbox();
						score+=500;
						this.phase=PHASESHOOT;
						this.compteur=0;
					}
					fen.supprimer(tabTirJou.get(i).getTex());
					tabTirJou.remove(i);
					//score=score+25;//INCREMENTATION DU SCORE
					break;
				}
			}
		}
	}

	/*-----APPARITION_TIR_JOUEUR-----*/
	if(cla.getBoutonJ1AEnfoncee() && jou.getTempTir()==0){
		int typeTir = jou.getTir();
		int x = jou.getTex().getB().getX();
		int y_AY = jou.getTex().getA().getY();
		int y_BY = jou.getTex().getB().getY();
		String laserPath = "./img/laser/player1/"+typeTir+".png";
	    switch(typeTir)//TIR EN FONCTION DU TIR DEFINI DANS LE JOUEUR
		{
		case 1: //TIR DOUBLE
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_BY-23)),1,20,"centre"));
		    ajouterDernierTirAfenetre(tabTirJou);
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_AY+10)),1,20,"centre"));
		    ajouterDernierTirAfenetre(tabTirJou);
		    jou.setTempTir(10);
		    break;
		case 2: //TIR TRIPLE
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_BY-19)),1,20,"centre"));
		    ajouterDernierTirAfenetre(tabTirJou);
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_AY+10)),1,20,"centre"));
		    ajouterDernierTirAfenetre(tabTirJou);
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_BY-(jou.getTex().getHauteur()/2))),typeTir,20,"centre"));
		    tabTirJou.get(tabTirJou.size()-1).getTex().translater(0,-tabTirJou.get(tabTirJou.size()-1).getTex().getHauteur()/2);
		    ajouterDernierTirAfenetre(tabTirJou);
		    jou.setTempTir(10);
		    break;
		case 3: //TIR EVENTAIL
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_BY-39)),1,20,"gauche"));
		    ajouterDernierTirAfenetre(tabTirJou);
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_AY+30)),1,20,"droite"));
		    ajouterDernierTirAfenetre(tabTirJou);
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_BY-(jou.getTex().getHauteur()/2))),typeTir,20,"centre"));
		    tabTirJou.get(tabTirJou.size()-1).getTex().translater(0,-tabTirJou.get(tabTirJou.size()-1).getTex().getHauteur()/2);
		    ajouterDernierTirAfenetre(tabTirJou);
		    jou.setTempTir(10);
		    break;

		default: //TIR PAR DEFAUT
		    tabTirJou.add(new Tir(new Texture(laserPath, new Point(x,y_BY-(jou.getTex().getHauteur()/2))),typeTir,20,"centre"));
		    tabTirJou.get(tabTirJou.size()-1).getTex().translater(0,-tabTirJou.get(tabTirJou.size()-1).getTex().getHauteur()/2);
		    ajouterDernierTirAfenetre(tabTirJou);
		    jou.setTempTir(10);
		}
	}
	else if(jou.getTempTir()>0){//DECREMENTATION DU COMPTEUR D'ATTENTE POUR TIRER
	    jou.setTempTir(jou.getTempTir()-1);
	}


	/*---------------------------*/
	/*----------ENNEMIE----------*/
	/*---------------------------*/

	/*-----TRANSLATION_ENNEMIE-----*/
	for(int i=tabEnn.size()-1; i>=0; i--){
	    switch(tabEnn.get(i).getTraj())
		{
		case 1: //Translation en ligne, puis virement à gauche/droite
		    if(tabEnn.get(i).getTex().getA().getX()>500){
			tabEnn.get(i).getTex().translater(-tabEnn.get(i).getVit(),0);
		    }
		    else
			{
			    if(tabEnn.get(i).getTex().getA().getY()>(680/2)){
				tabEnn.get(i).getTex().translater(-tabEnn.get(i).getVit(),+(tabEnn.get(i).getVit())/2);
			    }
			    else
				{
				    tabEnn.get(i).getTex().translater(-tabEnn.get(i).getVit(),-(tabEnn.get(i).getVit())/2);
				}
			}
		    break;
		case 2: //Translation haut->bas
		    tabEnn.get(i).getTex().translater(0,-tabEnn.get(i).getVit());
		    break;
		case 3: //Translation bas->haut
		    tabEnn.get(i).getTex().translater(0,+tabEnn.get(i).getVit());
		    break;

		default: //Translation en ligne par défault
		    tabEnn.get(i).getTex().translater(-tabEnn.get(i).getVit(),0);
		}
	    if(tabEnn.get(i).getTex().getB().getX()<0){//SUPPRESION SI UN ENNEMIE ATTEIND LE BORD GAUCHE DE LECRAN
		fen.supprimer(tabEnn.get(i).getTex());
		tabEnn.remove(i);
	    }
	    else if(tabEnn.get(i).getTex().getB().getY()<0-700 && tabEnn.get(i).getTraj()==2){//SUPPRESION SI UN ENNEMIE VENANT DU HAUT DE L'ECRAN ATTEIND LE BAS DE LECRAN
		fen.supprimer(tabEnn.get(i).getTex());
		tabEnn.remove(i);
	    }
	    else if(tabEnn.get(i).getTex().getA().getY()>720+700 && tabEnn.get(i).getTraj()==3){//SUPPRESION SI UN ENNEMIE VENANT DU BAS DE L'ECRAN ATTEIND LE HAUT DE LECRAN
		fen.supprimer(tabEnn.get(i).getTex());
		tabEnn.remove(i);
	    }	
	    else if(tabEnn.get(i).getTex().getB().getX()>1200){//ZONE D'ATTENTE POUR EVITER DE FAIRE APPARAITRE DES ENNEMIES EN BOUCLE
		zoneApparitionEnnemi=1;
	    }
	}


	//TODO: boss!!!
	if(this.phase==PHASESHOOT){
        /*-----APPARITION_ENNEMI-----*/
        randomApparition = r.nextInt(40)+1;
        if(randomApparition==1 && zoneApparitionEnnemi==0){
            randomTrajectoire = r.nextInt(8);
            switch(randomTrajectoire)//RANDOM POUR DEFINIR LA TRAJECTOIRE DE(s) ENNEMIE(s)
            {
                case 1: //Apparition en ligne, puis virement à gauche/droite
                    randomPositionxyTrajectoire = r.nextInt((720/2)-94);
                    for(int i=0; i<2; i++){

                        //tabEnn.add(new Ennemi(new Texture("./img/ennemie/ennemie1/1.png", new Point(1280+i*(84+10),((720/2)+randomPositionxyTrajectoire))),8,1,randomTrajectoire));
                        tabEnn.add(new Ennemi(new Texture(TEXENN[randomTrajectoire]),16,1,randomTrajectoire));
                        tabEnn.get(tabEnn.size()-1).getTex().setA(new Point(1280+i*(84+10),((720/2)+randomPositionxyTrajectoire)));

                        fen.ajouter(tabEnn.get(tabEnn.size()-1).getTex());

                        // tabEnn.add(new Ennemi(new Texture("./img/ennemie/ennemie1/1.png", new Point(1280+i*(84+10),((720/2)-randomPositionxyTrajectoire)-94)),8,1,randomTrajectoire));
                        tabEnn.add(new Ennemi(new Texture(TEXENN[randomTrajectoire] ),14,1,randomTrajectoire));
						tabEnn.get(tabEnn.size()-1).getTex().setA( new Point(1280+i*(84+10),((720/2)-randomPositionxyTrajectoire)-94) );

                        fen.ajouter(tabEnn.get(tabEnn.size()-1).getTex());
                    }
                    break;
                case 2: //Apparition haut->bas
                    randomPositionxyTrajectoire = r.nextInt((1280/2)-100);
                    for(int i=0; i<4; i++){

                        // tabEnn.add(new Ennemi(new Texture("./img/ennemie/ennemie1/2.png", new Point(1280-randomPositionxyTrajectoire,720+i*(93+10))),8,1,randomTrajectoire));
                        tabEnn.add(new Ennemi(new Texture(TEXENN[randomTrajectoire] ),12,1,randomTrajectoire));
						tabEnn.get(tabEnn.size()-1).getTex().setA( new Point(1280-randomPositionxyTrajectoire,720+i*(93+10)) );

                        fen.ajouter(tabEnn.get(tabEnn.size()-1).getTex());
                    }
                    break;
                case 3: //Apparition bas->haut
                    randomPositionxyTrajectoire = r.nextInt((1280/2)-100);
                    for(int i=0; i<4; i++){

                        // tabEnn.add(new Ennemi(new Texture("./img/ennemie/ennemie1/3.png", new Point(1280-randomPositionxyTrajectoire,0-i*(93+10))),8,1,randomTrajectoire));
                        tabEnn.add(new Ennemi(new Texture(TEXENN[randomTrajectoire]),12,1,randomTrajectoire));
						tabEnn.get(tabEnn.size()-1).getTex().setA( new Point(1280-randomPositionxyTrajectoire,0-i*(93+10)) );

                        fen.ajouter(tabEnn.get(tabEnn.size()-1).getTex());
                    }
                    break;
                default: //Apparition en ligne par défault
                    randomPositionxyTrajectoire = r.nextInt(680-94);

                    // tabEnn.add(new Ennemi(new Texture("./img/ennemie/ennemie1/0.png", new Point(1280,randomPositionxyTrajectoire)),8,1,-1));
                    tabEnn.add(new Ennemi(new Texture(TEXENN[0]),14,1,-1));
					tabEnn.get(tabEnn.size()-1).getTex().setA( new Point(1280,randomPositionxyTrajectoire) );

                    fen.ajouter(tabEnn.get(tabEnn.size()-1).getTex());
            }

        }
        this.compteur++;
        System.out.println(this.compteur);
        if(this.compteur==TEMPSPHASE){
        	this.phase=PHASEBOSS;
        	System.out.println("PHASE BOSS!!!");
        	this.boss = new Boss(new Texture(TEXBOSS),1,VIEBOSS,-4);
			fen.ajouter(this.boss.getTex());
			this.phaseBoss=APPARITION;
		}

		//PHASE COMBAT DE BOSS!!!
    }else{
        /*BOSS!!!*/
        //TODO : barre de vie apparaissant (à la megaman) au spawn
		//TODO : animation destruction (kablooie)

		switch(this.phaseBoss){
			case APPARITION:
				if(this.boss.apparition())
					this.phaseBoss=COMBAT;
				break;

			case COMBAT:
				this.boss.deplacement();
				randomTirEnn = r.nextInt(30)+1;
				if(randomTirEnn==1) {//RANDOM SI =1 ALORS L'ENNEMI TIRE
					int x = boss.getTex().getA().getX()+90;
					int y_AY = boss.getTex().getA().getY();
					Texture texTir = new Texture(TEXTIRBOSS1);
					texTir.setA(new Point(x, y_AY +381));
					tabTirEnn.add(new Tir(texTir, 1, 20, "centre"));
					texTir.setA(new Point(x, y_AY + 103));
					ajouterDernierTirAfenetre(tabTirEnn);
					tabTirEnn.add(new Tir(texTir, 1, 20, "centre"));
					ajouterDernierTirAfenetre(tabTirEnn);
				}
				randomTirEnn = r.nextInt(20)+1;
				if(randomTirEnn==1) {	//RANDOM SI =1 ALORS L'ENNEMI TIRE
					Texture texTir = new Texture(TEXTIRBOSS2);
					texTir.setA(new Point(boss.getTex().getA().getX()+88, boss.getTex().getA().getY()+242));
					tabTirEnn.add(new Tir(texTir, 1, 20, "centre"));
					ajouterDernierTirAfenetre(tabTirEnn);
				}
				break;

			case DESTRUCTION:

				break;

		}

		if(jou.intersection(boss)==true){//COLLISION AVEC LE BOSS
			if(jou.getVie()>0){
				jou.setVie(jou.getVie()-1);
			}
			//affichageNombreVieJoueur.setImg("./img/score/"+jou.getVie()+".png");
			affichageNombreVieJoueur.setTexte(""+jou.getVie());
			jou.setTempVie(100);//TIMER ANTI-COLLISION
			jou.getTex().setImg("./img/life/damage/"+jou.getVie()+".png");
		}

    }


	
	/*-------------------------*/
	/*----------SCORE----------*/
	/*-------------------------*/

	/*-----MISE_A_JOUR_AFFICHAGE _SCORE-----*/
	scoreAffichage.setTexte(""+score);
	/*int s=score;
	  int i = 8;
					
	  while(s!=0){				
	  int mod=s%10;
	  s/=10;				
	  scoreAffichage[i].setImg("./img/score/"+mod+".png");
	  i--;
	  }*/


	fen.rafraichir();

    }

    //Ajouter la texture du dernier élément d'une collection de tir à la fenetre
    public void ajouterDernierTirAfenetre(ArrayList<Tir> tirCollection) {
    	fen.ajouter(tirCollection.get(tirCollection.size()-1).getTex());
    }


    public boolean fin(){//METHODE QUI VERIFIE LES DIFFERENTES COLISIONS DANS LE JEU
	boolean finJeu=false;
	boolean verifIntersection=false;
	Font policeGameOver;
	Texture gameOver;

	if(jou.getTempVie()==0){//SI LE JOUEUR NA PAS ETAIT EN COLLISION RECEMMENT
	    for(int i=tabEnn.size()-1; i>0 ; i--){
		if(jou.intersection(tabEnn.get(i))==true){//COLLISION AVEC UN ENNEMIE
		    jou.setVie(jou.getVie()-1);
			fen.supprimer(tabEnn.get(i).getTex());
			tabEnn.remove(i);
		    //affichageNombreVieJoueur.setImg("./img/score/"+jou.getVie()+".png");
		    affichageNombreVieJoueur.setTexte(""+jou.getVie());
		    jou.setTempVie(100);//TIMER ANTI-COLLISION
		    jou.getTex().setImg("./img/life/damage/"+jou.getVie()+".png");
		    break;
		}
	    }
	    
            if(jou.getTempVie() != 100){//SI DEJA COLLISION AVEC ENNEMIE PAS DE VERIFICATION DES TIR ENNEMIE
		for(int i=tabTirEnn.size()-1; i>0 ; i--){
		    if(jou.intersection(tabTirEnn.get(i))==true){//COLLISION AVEC UN TIR ENNEMIE
			fen.supprimer(tabTirEnn.get(i).getTex());
			tabTirEnn.remove(i);
			jou.setVie(jou.getVie()-1);
			//affichageNombreVieJoueur.setImg("./img/score/"+jou.getVie()+".png");
			affichageNombreVieJoueur.setTexte(""+jou.getVie());
			jou.setTempVie(100);//TIMER ANTI-COLLISION
			jou.getTex().setImg("./img/life/damage/"+jou.getVie()+".png");
			break;
		    }
		}
		
		
	    }

	    if(jou.getVie()==0){//SI LE JOUEUR N'A PLUS DE VIE
		gameOver = new Texture("./img/background/3.png",new Point(640-89,360+89));
		fen.ajouter(gameOver);
		finJeu=true;

		try{
		    Thread.sleep(2000);
		}catch(Exception e){e.getMessage();}
		
		HighScore.demanderEnregistrerNom(fen,cla,null,score,"highscore");
	    
	    }
	}
	else{//DECREMENTATION DU TIMER ANTI-COLLISION
		if(joueurClignote==true){
			fen.ajouter(jou.getTex());
			joueurClignote=false;
		}else{
			fen.supprimer(jou.getTex());
			joueurClignote=true;
		}
	    jou.setTempVie(jou.getTempVie()-1);
	}
	
	return finJeu;
    } 
	
	public int getStatus() {
    	return status;
    }
	
	
}
