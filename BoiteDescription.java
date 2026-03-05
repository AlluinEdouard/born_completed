import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import MG2D.geometrie.Texture;	
import MG2D.Couleur;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texte;
import java.io.IOException;


/**
 * Gère l'affichage des informations textuelles d'un jeu sélectionné dans le menu.
 * Cela inclut le résumé du jeu, la configuration des contrôles (joystick et boutons),
 * ainsi que l'affichage du top 10 des meilleurs scores.
 */
public class BoiteDescription extends Boite{

    private Texte[] message;
    private boolean stop;
    private int nombreLigne;
    private Texture joystick;
    private Texture[] bouton;
    private Texte tJoystick;
    private Texte[] tBouton;
    private String[] texteBouton;
    private Texte highscore;
    private Texte[] listeHighScore;
	
	/*HACKED BY BENDAL*/
	private Font font1 = null;
	private Font font2 = null;
	private Font font3 = null;
	private Font font4 = null;
	/****************/
	
	
	
    BoiteDescription(Rectangle rectangle) {
	super(rectangle);
	
	/*HACKED BY BENDAL*/
	try{
	    Font font = null;
		Font fontTexte = null;
		File in = new File("fonts/PrStart.ttf");
		font = Font.createFont(Font.TRUETYPE_FONT, in);
		in = new File("fonts/Volter__28Goldfish_29.ttf");
		fontTexte = Font.createFont(Font.TRUETYPE_FONT, in);
	    font1 = fontTexte.deriveFont(15.0f);
		font2 = fontTexte.deriveFont(20.0f);
		font3 = font.deriveFont(25.0f);
		font4 = font.deriveFont(14.0f);
	}catch (Exception e) {
	    System.err.println(e.getMessage());
	    font1 = new Font("SansSerif", Font.PLAIN, 15);
	    font2 = new Font("SansSerif", Font.PLAIN, 20);
	    font3 = new Font("SansSerif", Font.PLAIN, 25);
	    font4 = new Font("SansSerif", Font.PLAIN, 14);
	}
	/****************/
	
	bouton = new Texture[6];
	tBouton = new Texte[6];
	texteBouton = new String[7];
		
	//declaration des texture bouton + joystick
	this.joystick = new Texture("img/joystick2.png", new Point(740, 100), 40,40);
	for(int i = 0 ; i < 3 ; i++){
	    this.bouton[i] = new Texture("img/ibouton2.png", new Point(890+130*i, 130), 40, 40);
	}
	for(int i = 3 ; i < 6 ; i++){
	    this.bouton[i] = new Texture("img/ibouton2.png", new Point(890+130*(i-3), 50), 40, 40);
	}
	
	//declaration des textes bouton + joystick
	this.tJoystick = new Texte(Couleur .NOIR, "...", font1, new Point(760, 80));
	for(int i = 0 ; i < 3 ; i++){
	    this.tBouton[i] = new Texte(Couleur .NOIR, "...", font1, new Point(910+130*i, 120));
	}
	for(int i = 3 ; i < 6 ; i++){
	    this.tBouton[i] = new Texte(Couleur .NOIR, "...", font1, new Point(910+130*(i-3), 40));
	}
	stop = false;
	message = new Texte[10];
	for(int i = 0 ; i < message.length ; i++){
	    message[i] = new Texte(Couleur .NOIR, "", font2, new Point(960, 590));
	    message[i].translater(0, -i*30);

	}
	nombreLigne = 0;

	highscore = new Texte(Couleur.NOIR, "HIGHSCORE", font3, new Point(960, 335));
	listeHighScore = new Texte[10];
	for(int i=0;i<5;i++){
	    listeHighScore[i] = new Texte(Couleur.NOIR, "", font4, new Point(820,310));
	    listeHighScore[i].translater(0,-i*25);
	}
	for(int i=5;i<10;i++){
	    listeHighScore[i] = new Texte(Couleur.NOIR, "", font4, new Point(1100,310));
	    listeHighScore[i].translater(0,-(i-5)*25);
	}
	
	
	/*
	//declaration des textes bouton + joystick
	this.tJoystick = new Texte(Couleur .NOIR, "...", new Font("Calibri", Font.TYPE1_FONT, 15), new Point(760, 80));
	for(int i = 0 ; i < 3 ; i++){
	    this.tBouton[i] = new Texte(Couleur .NOIR, "...", new Font("Calibri", Font.TYPE1_FONT, 15), new Point(910+130*i, 120));
	}
	for(int i = 3 ; i < 6 ; i++){
	    this.tBouton[i] = new Texte(Couleur .NOIR, "...", new Font("Calibri", Font.TYPE1_FONT, 15), new Point(910+130*(i-3), 40));
	}
	stop = false;
	message = new Texte[10];
	for(int i = 0 ; i < message.length ; i++){
	    message[i] = new Texte(Couleur .NOIR, "", new Font("Calibri", Font.TYPE1_FONT, 20), new Point(960, 590));
	    message[i].translater(0, -i*30);

	}
	nombreLigne = 0;

	highscore = new Texte(Couleur.NOIR, "HIGHSCORE", new Font("Calibri", Font.TYPE1_FONT, 25), new Point(960, 335));
	listeHighScore = new Texte[10];
	for(int i=0;i<5;i++){
	    listeHighScore[i] = new Texte(Couleur.NOIR, "", new Font("Calibri", Font.TYPE1_FONT, 17), new Point(820,310));
	    listeHighScore[i].translater(0,-i*25);
	}
	for(int i=5;i<10;i++){
	    listeHighScore[i] = new Texte(Couleur.NOIR, "", new Font("Calibri", Font.TYPE1_FONT, 17), new Point(1100,310));
	    listeHighScore[i].translater(0,-(i-5)*25);
	}*/

    }
	/**
     * Lit le fichier de description (description.txt) du jeu spécifié et met à jour l'affichage.
     * Le fichier est lu jusqu'à un maximum de 10 lignes.
     * @param path Le chemin d'accès vers le dossier du jeu.
     */
    public void lireFichier(String path){
	//System.out.println(path);
	String fichier =path+"/description.txt";
		
	//lecture du fichier texte	
	try{
	    InputStream ips=new FileInputStream(fichier); 
	    InputStreamReader ipsr=new InputStreamReader(ips);
	    BufferedReader br=new BufferedReader(ipsr);
	    String ligne;
	    while (/*(ligne=br.readLine())!=null &&*/stop == false){
		ligne=br.readLine();
		//System.out.println(ligne);
		if(ligne != null){
		    //changer message
					
		    message[nombreLigne].setTexte(ligne);
		    setMessage(ligne, nombreLigne);
		}else{
		    //changer message
					
		    message[nombreLigne].setTexte("");
		    setMessage("", nombreLigne);
		}
		nombreLigne++;
		if(nombreLigne >= 10){
		    stop = true;
		    nombreLigne = 0;
		}
	    }
	    stop = false;
	    br.close(); 
	}		
	catch (Exception e){
	    System.err.println(e.toString());
	}
    }

	/**
     * Lit le fichier des meilleurs scores (highscore) du jeu spécifié.
     * Si le fichier n'existe pas, affiche des tirets par défaut.
     * @param path Le chemin d'accès vers le dossier du jeu.
     */
    public void lireHighScore(String path){
	
        for(int i=0;i<10;i++){
	    if(i==0)
		listeHighScore[i].setTexte("1er - ");
	    else
		listeHighScore[i].setTexte((i+1)+"eme - ");
	}
	
	String fichier =path+"/highscore";
	
	File f = new File(fichier);
	if(!f.exists()){
	    for(int i=0;i<10;i++)
		listeHighScore[i].setTexte("/");
	}else{
	    ArrayList<LigneHighScore> liste = HighScore.lireFichier(fichier);
	    for(int i=0;i<liste.size();i++){
		if(i==0)
		    listeHighScore[i].setTexte("1er : "+liste.get(i).getNom()+" - "+liste.get(i).getScore());
		else
		    listeHighScore[i].setTexte((i+1)+"eme : "+liste.get(i).getNom()+" -  "+liste.get(i).getScore());
	    }
	}
    }

	/**
     * Lit le fichier de configuration des contrôles (bouton.txt) du jeu spécifié.
     * Parse une ligne séparée par des ":" pour assigner les actions aux boutons.
     * @param path Le chemin d'accès vers le dossier du jeu.
     */
    public void lireBouton(String path){
	//System.out.println(path);
	String fichier =path+"/bouton.txt";
	File f = new File(fichier);
	if(!f.isFile()){
	    settJoystick("...");
	    for(int i = 0 ; i < 6 ; i++){
		settBouton("...", i);
	    }
	    return;
	}
		
	//lecture du fichier texte	
	try{
	    InputStream ips=new FileInputStream(fichier); 
	    InputStreamReader ipsr=new InputStreamReader(ips);
	    BufferedReader br=new BufferedReader(ipsr);
	    String ligne;
	    ligne = br.readLine();
	    if(ligne == null){
		settJoystick("...");
		for(int i = 0 ; i < 6 ; i++){
		    settBouton("...", i);
		}
	    }else{
		texteBouton = ligne.split(":");
		if(texteBouton.length >= 7){
		    //changer le texte des boutons
		    settJoystick(texteBouton[0]);
		    for(int i = 0 ; i < 6 ; i++){
			settBouton(texteBouton[i+1], i);
		    }
		}else{
		    settJoystick("...");
		    for(int i = 0 ; i < 6 ; i++){
			settBouton("...", i);
		    }
		}
	    }
	}catch(Exception e){System.err.println(e);};
		
    }
	
    public Texte[] getMessage(){
	return message;
    }
	
    public void setMessage(String message, int a) {
	this.message[a].setTexte(message);	
    }
	
    public Texture[] getBouton(){
	return this.bouton;
    }
	
    public Texture getJoystick(){
	return this.joystick;
    }
	
    public Texte[] gettBouton(){
	return this.tBouton;
    }
	
    public Texte gettJoystick(){
	return this.tJoystick;
    }

    public Texte getHighscore(){
	return this.highscore;
    }

    public Texte[] getListeHighScore(){
	return this.listeHighScore;
    }

	
    public void settJoystick(String s){
	this.tJoystick.setTexte(s);		
    }
	
    public void settBouton(String s, int a){
	this.tBouton[a].setTexte(s);		
    }
	
    /*public Texte getMessage() {
      return message;
      }
    */
	

}
