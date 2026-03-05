import MG2D.*;
import MG2D.geometrie.*;
import java.io.File;
import java.awt.Font;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Utilisaires pour la gestion et l'affichage des High Scores de la borne d'arcade.
 */

class HighScore{

	/**
	 * Retourne le caractère suivant dans l'alphabet (boucle après 'Z' sur '.', ' ' puis 'A').
	 * @param c Le caractère actuel.
	 * @return Le caractère suivant.
	 */

    public static char suivant(char c){
	if(c>='A' && c<'Z')
	    return (char)(c+1);
	if(c=='Z')
	    return '.';
	if(c=='.')
	    return ' ';
	return 'A';
    }

	/**
	 * Retourne le caractère précedent dans l'alaphebet.
	 * @param c Le caractère actuel.
	 * @return Le caractère précédent.
	 */

    public static char precedent(char c){
	if(c>'A' && c<='Z')
	    return (char)(c-1);
	if(c=='A')
	    return ' ';
	if(c==' ')
	    return '.';
	return 'Z';
    }

	/**
	 * Affiche l'interface de saisie du nom à 3 caractères pour le High Score.
	 * Intercepte les actions du joystick pour changer les lettres et valider.
	 * @param f La fenêtre MG2D courante.
	 * @param clavier Le clavier de la borne pour la saisie.
	 * @param t Une texture optionnelle de fond.
	 * @param s Le score obtenu par le joueur.
	 * @param fichierHighScore Le chemin vers le fichier de sauvergarde des scores.
	 */
    public static void demanderEnregistrerNom(Fenetre f, ClavierBorneArcade clavier, Texture t, int s, String fichierHighScore){

	ArrayList<LigneHighScore> list = lireFichier(fichierHighScore);
	for(LigneHighScore l:list)
	    System.out.println(l);

	int position=0;
	boolean fin = false;
	while(!fin){
	    if(position==list.size())
		fin=true;
	    else
		if(s<=list.get(position).getScore())
		    position++;
		else{
		    fin=true;
		}
	}

	//System.out.println("position : "+position);
	if(position>=10)
	    System.exit(0);
	
	String score=s+"";

	char cprec[]={' ',' ',' '};
	char c[]={'A',' ',' ','#'};
	char csuiv[]={' ',' ',' '};
	int indexSelec=0;

	Font font;
	font = null;
	try{
	    File in = new File("fonts/PrStart.ttf");
	    font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(40.0f);
	}catch (Exception e) {
	    System.err.println(e.getMessage());
	    font = new Font("SansSerif", Font.PLAIN, 40);
	}
	Texte highscore = new Texte(Couleur.NOIR, "H  I  G  H  S  C  O  R  E", font, new Point(640,950));
	Texte scoreAtteint = new Texte(Couleur.NOIR, score, font, new Point(420,400));
	Texte enterYourName = new Texte(Couleur.NOIR, "E n t e r   Y o u r   n a m e", font, new Point(640,800));
	Texte posNum = new Texte(Couleur.NOIR, (position+1)+"eme", font, new Point(120,400));

	if(position==0)
	    posNum.setTexte("1er");

	Texte posNumPrec = new Texte(Couleur.NOIR, "", font, new Point(120,580));
	Texte posNumSuiv = new Texte(Couleur.NOIR, "", font, new Point(120,170));
	
	Texte caracteres[] = new Texte[4];
	caracteres[0] = new Texte(Couleur.NOIR, c[0]+"", font, new Point(690,400));
	caracteres[1] = new Texte(Couleur.NOIR, c[1]+"", font, new Point(840,400));
	caracteres[2] = new Texte(Couleur.NOIR, c[2]+"", font, new Point(990,400));
	caracteres[3] = new Texte(Couleur.NOIR, c[3]+"", font, new Point(1140,400));
	Texte caracteresPrec[] = new Texte[3];
	caracteresPrec[0] = new Texte(Couleur.NOIR, cprec[0]+"", font, new Point(690,580));
	caracteresPrec[1] = new Texte(Couleur.NOIR, cprec[1]+"", font, new Point(840,580));
	caracteresPrec[2] = new Texte(Couleur.NOIR, cprec[2]+"", font, new Point(990,580));
	Texte scorePrec = new Texte(Couleur.NOIR, "", font, new Point(420,580));
	Texte caracteresSuiv[] = new Texte[3];
	caracteresSuiv[0] = new Texte(Couleur.NOIR, csuiv[0]+"", font, new Point(690,170));
	caracteresSuiv[1] = new Texte(Couleur.NOIR, csuiv[1]+"", font, new Point(840,170));
	caracteresSuiv[2] = new Texte(Couleur.NOIR, csuiv[2]+"", font, new Point(990,170));
	Texte scoreSuiv = new Texte(Couleur.NOIR, "", font, new Point(420,170));
	
	Rectangle rect1 = new Rectangle(Couleur.NOIR,new Point(650,350), new Point(720,480), false);
	Rectangle rect2 = new Rectangle(Couleur.NOIR,new Point(800,350), new Point(870,480), false);
	Rectangle rect3 = new Rectangle(Couleur.NOIR,new Point(950,350), new Point(1020,480), false);
	Rectangle rect4 = new Rectangle(Couleur.NOIR,new Point(1100,350), new Point(1170,480), false);
	
	Triangle select = new Triangle(Couleur.NOIR, new Point(690,340), new Point(670,300), new Point(710,300),true);

	Texture blancTrans = new Texture("img/blancTransparent.png",new Point(0,0));

	if(t!=null)
	    f.ajouter(t);
	
	f.ajouter(blancTrans);
	f.ajouter(highscore);
	f.ajouter(scoreAtteint);
	f.ajouter(scorePrec);
	f.ajouter(scoreSuiv);
	f.ajouter(enterYourName);
	f.ajouter(caracteres[0]);
	f.ajouter(caracteres[1]);
	f.ajouter(caracteres[2]);
	f.ajouter(caracteres[3]);
	f.ajouter(caracteresPrec[0]);
	f.ajouter(caracteresPrec[1]);
	f.ajouter(caracteresPrec[2]);
	f.ajouter(caracteresSuiv[0]);
	f.ajouter(caracteresSuiv[1]);
	f.ajouter(caracteresSuiv[2]);
	f.ajouter(posNum);
	f.ajouter(posNumPrec);
	f.ajouter(posNumSuiv);
	f.ajouter(rect1);
	f.ajouter(rect2);
	f.ajouter(rect3);
	f.ajouter(rect4);
	f.ajouter(select);

	if(position!=0){
	    //System.out.println("ajout du record precedent");
	    caracteresPrec[0].setTexte(list.get(position-1).getNom().charAt(0)+"");
	    caracteresPrec[1].setTexte(list.get(position-1).getNom().charAt(1)+"");
	    caracteresPrec[2].setTexte(list.get(position-1).getNom().charAt(2)+"");
	    scorePrec.setTexte(list.get(position-1).getScore()+"");
	    if(position==1)
		posNumPrec.setTexte("1er");
	    else
		posNumPrec.setTexte(position+"eme");
	}
	if(position!=list.size()){
	    //System.out.println("ajout du record suivant");
	    caracteresSuiv[0].setTexte(list.get(position).getNom().charAt(0)+"");
	    caracteresSuiv[1].setTexte(list.get(position).getNom().charAt(1)+"");
	    caracteresSuiv[2].setTexte(list.get(position).getNom().charAt(2)+"");
	    scoreSuiv.setTexte(list.get(position).getScore()+"");
	    posNumSuiv.setTexte((position+2)+"eme");
	}

	fin=false;

	while(!fin){
	    try{
		Thread.sleep(10);
	    }catch(Exception e){}

	    if(clavier.getJoyJ1DroiteTape()){
		if(indexSelec<3){
		    indexSelec++;
		    select.translater(150,0);
		}
	    }

	    if(clavier.getJoyJ1GaucheTape()){
		if(indexSelec>0){
		    indexSelec--;
		    select.translater(-150,0);
		}
	    }

	    if(clavier.getJoyJ1HautTape()){
		if(indexSelec!=3){
		    c[indexSelec]=suivant(c[indexSelec]);
		    caracteres[indexSelec].setTexte(c[indexSelec]+"");
		}
	    }

	    if(clavier.getJoyJ1BasTape()){
		if(indexSelec!=3){
		    c[indexSelec]=precedent(c[indexSelec]);
		    caracteres[indexSelec].setTexte(c[indexSelec]+"");
		}
	    }

	    if(clavier.getBoutonJ1ATape() && indexSelec==3)
		fin=true;
	    
	    f.rafraichir();
	}

	enregistrerFichier(fichierHighScore, list, ""+c[0]+c[1]+c[2],s);

	System.exit(0);
    }

	/**
	 * Lit un fichier de sauvergarde de High Scores. 
	 * @param fichier Le chemin vers le fichier à lire (.hig).
	 * @return Une liste de LigneHighScore contenant les données lues.
	 */

    public static ArrayList<LigneHighScore> lireFichier(String fichier){
	ArrayList<LigneHighScore> l = new ArrayList<LigneHighScore>();

	try{
	    BufferedReader reader = new BufferedReader(new FileReader(fichier));
	    String currentLine;
	    while ((currentLine = reader.readLine()) != null) {
		l.add(new LigneHighScore(currentLine));
	    }
	    reader.close();
	}catch(Exception e){}
	
	return l;
    }

	/**
	 * Insère un nouveau score dans la liste, tronque à 10 éléments, puis sauvergarde le fichier.  
	 * @param fichier Le chemin du fichier de sauvergarde.
	 * @param list La liste actuelle des scores.
	 * @param nom le nom du joeur à enregistrer (3 lettres).
	 * @param score Le score obtenu.
	 */

    public static void enregistrerFichier(String fichier, ArrayList<LigneHighScore> list, String nom, int score){
	int position=0;
	boolean fin = false;
	while(!fin){
	    if(position==list.size())
		fin=true;
	    else
		if(score<=list.get(position).getScore())
		    position++;
		else{
		    fin=true;
		}
	}

	list.add(position,new LigneHighScore(nom,score));
	while(list.size()>10)
	    list.remove(list.size()-1);
	
	try{
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fichier));
	    for(int i=0;i<list.size();i++){
		writer.write(list.get(i).toString());
		if(i!=(list.size()-1))
		    writer.write("\n");
	    }
	    writer.close();
	}catch(Exception e){}

	

	
	
    }
}
