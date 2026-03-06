import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import MG2D.geometrie.*;
import MG2D.geometrie.Point;
import MG2D.audio.*;
import MG2D.*;
import MG2D.Fenetre;

/**
 * Classe principale qui gère l'interface graphique du menu de la borne d'arcade.
 * Elle affiche la liste des jeux, gère la navigation au joystick, et lance la musique de fond. 
 */

public class Graphique {

    private static final int TAILLEX = 1280;
    private static final int TAILLEY = 1024;
    private static final Fenetre f = new Fenetre("_Menu Borne D'arcade_", TAILLEX, TAILLEY);
    private static final GraphicsDevice ECRAN = recupererEcranPrincipal();
    private static final int AJUSTEMENT_Y = 45;
    private static final int DECALAGE_X = calculerDecalageX();
    private static final int DECALAGE_Y = calculerDecalageY();
    private static final int DECALAGE_FINAL_X = DECALAGE_X;
    private static final int DECALAGE_FINAL_Y = DECALAGE_Y + AJUSTEMENT_Y;
    private ClavierBorneArcade clavier;
    private BoiteSelection bs;
    private BoiteImage bi;
    private BoiteDescription bd;
    public static Bouton[] tableau;
    private Pointeur pointeur;
    Font font;
    Font fontSelect;
	public static boolean[] textesAffiches;
	public static Bruitage musiqueFond;
	private static String[] tableauMusiques;
	private static int cptMus;

	/**
	 * Initialise la fenêtre principale, charge les polices d'écriture, les musiques de fond,
	 * et construit l'interface visuelle du carrousel de sélection des jeux. 
	 */

    public Graphique(){
    	
	int add_variables = 0;
	font = null;
		try{
		    File in = new File("fonts/PrStart.ttf");
		    font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(26.0f);
		}catch (Exception e) {
		    System.err.println(e.getMessage());
		    font = new Font("SansSerif", Font.PLAIN, 26);
		}

	clavier = new ClavierBorneArcade();
	f.addKeyListener(clavier);
	f.setFocusable(true);
	f.addWindowFocusListener(new WindowAdapter() {
	    @Override
	    public void windowGainedFocus(WindowEvent e) {
		clavier.reset();
		reprendreFocusMenu();
	    }

	    @Override
	    public void windowLostFocus(WindowEvent e) {
		// Evite les touches "bloquées" lors d'un changement de fenêtre.
		clavier.reset();
	    }
	});
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		reprendreFocusMenu();
	    }
	});

	/*Retrouver le nombre de jeux dispo*/
	Path yourPath = FileSystems.getDefault().getPath("projet/");
	int cpt=0;
	try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(yourPath)) {
	    for (Path path : directoryStream) {
		if(!Files.isDirectory(path)){
		    continue;
		}
		String nomJeu = path.getFileName().toString();
		Path script = FileSystems.getDefault().getPath(nomJeu + ".sh");
		if(Files.isRegularFile(script)){
		    cpt++;
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

	tableau = new Bouton[cpt];
	textesAffiches = new boolean[cpt];
	for(int i=0;i<cpt;i++){
		textesAffiches[i]=true;
	}
	
		Bouton.remplirBouton();
		pointeur = new Pointeur();
		bs = new BoiteSelection(new Rectangle(Couleur .GRIS_CLAIR, pointEcran(0, 0), pointEcran(640, TAILLEY), true), pointeur);
		//f.ajouter(bs.getRectangle());
		//System.out.println(tableau[pointeur.getValue()].getChemin());
		bi = new BoiteImage(new Rectangle(Couleur .GRIS_FONCE, pointEcran(640, 512), pointEcran(TAILLEX, TAILLEY), true), new String(tableau[pointeur.getValue()].getChemin()));
		//f.ajouter(bi.getRectangle());
		bd = new BoiteDescription(new Rectangle(Couleur .GRIS, pointEcran(640, 0), pointEcran(TAILLEX, 512), true));
		bd.lireFichier(tableau[pointeur.getValue()].getChemin());
		bd.lireHighScore(tableau[pointeur.getValue()].getChemin());
		appliquerDecalageInterface();
		//f.ajouter(bd.getRectangle());

		Texture fond = new Texture("img/fondretro3.png", pointEcran(0, 0), TAILLEX, TAILLEY);
	f.ajouter(fond);
	//ajout apres fond car bug graphique sinon
	f.ajouter(bi.getImage());
	for(int i = 0 ; i < bd.getMessage().length ; i++){
	    f.ajouter(bd.getMessage()[i]);
	}
	//f.ajouter(bd.getMessage());
	f.ajouter(pointeur.getTriangleGauche());
	f.ajouter(pointeur.getTriangleDroite());
	for(int i = 0 ; i < tableau.length ; i++){
	    f.ajouter(tableau[i].getTexture());
	}
	f.ajouter(pointeur.getRectangleCentre());
	for(int i = 0 ; i < tableau.length ; i++){
	    f.ajouter(tableau[i].getTexte());
	    tableau[i].getTexte().setPolice(font);
	    tableau[i].getTexte().setCouleur(Couleur.BLANC);
	}
	//add texture
	for(int i = 0 ; i < bd.getBouton().length ; i++){
	    f.ajouter(bd.getBouton()[i]);
	}
	f.ajouter(bd.getJoystick());
	//add texte
	for(int i = 0 ; i < bd.gettBouton().length ; i++){
	    f.ajouter(bd.gettBouton()[i]);
	}
	f.ajouter(bd.gettJoystick());
		f.ajouter(new Ligne(Couleur.NOIR,pointEcran(670,360), pointEcran(1250,360)));
		f.ajouter(new Ligne(Couleur.NOIR,pointEcran(670,190), pointEcran(1250,190)));
		f.ajouter(new Ligne(Couleur.NOIR,pointEcran(960,210), pointEcran(960,310)));
	f.ajouter(bd.getHighscore());
	for(int i = 0 ; i < bd.getListeHighScore().length ; i++){
	    f.ajouter(bd.getListeHighScore()[i]);
	}
	
	/*Musique de fond*/
	//Comptage du nombre de musiques disponibles
	Path cheminMusiques = FileSystems.getDefault().getPath("sound/bg/");
	cptMus=0;
	if(Files.isDirectory(cheminMusiques)){
	    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(cheminMusiques)) {
		for (Path path : directoryStream) {
		    cptMus++;
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    //Creation d'un tableau de musiques
	    if(cptMus > 0){
		tableauMusiques = new String[cptMus];
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(cheminMusiques)) {
		    int i = cptMus-1;
		    for (Path path : directoryStream) {
			tableauMusiques[i]=path.getFileName().toString();
			i--;
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		//Choix d'une musique aleatoire et lecture de celle-ci
		this.lectureMusiqueFond();
	    }else{
		System.err.println("Aucune musique de fond trouvée dans sound/bg, musique désactivée.");
	    }
	}else{
	    System.err.println("Dossier sound/bg introuvable, musique de fond désactivée.");
	}
    }

	/**
	 * Boucle principale de l'interface du menu.
	 * Gère les animations, les interactions avec le clavier d'arcade, le rafraîchissement
	 * des images des jeux sélectionnés, et la boîte de dialogue de confirmation pour quitter. 
	 */

    public void selectionJeu(){	
			Texture fondBlancTransparent = new Texture("./img/blancTransparent.png", pointEcran(0,0));
			Rectangle boutonNon = new Rectangle(Couleur.ROUGE, pointEcran(340, 600), 200, 100, true);
			Rectangle boutonOui = new Rectangle(Couleur.VERT, pointEcran(740, 600), 200, 100, true);
			Texte message = new Texte(Couleur.NOIR, "Voulez vous vraiment quitter ?", font, pointEcran(640, 800));
			Texte non = new Texte(Couleur.NOIR, "NON", font, pointEcran(440, 650));
			Texte oui = new Texte(Couleur.NOIR, "OUI", font, pointEcran(840, 650));
			Rectangle rectSelection = new Rectangle(Couleur.BLEU, pointEcran(330,590),220,120, true);
		int frame=0;
		boolean fermetureMenu=false;
		int selectionSur = 0;
		Texte textePrec=tableau[pointeur.getValue()].getTexte();
		while(true){
			try {
				if(frame==0){
					if(textesAffiches[pointeur.getValue()]==true){
						f.supprimer(tableau[pointeur.getValue()].getTexte());
						textesAffiches[pointeur.getValue()]=false;
					}
				}
				if(frame==3){
					if(textesAffiches[pointeur.getValue()]==false){
						f.ajouter(tableau[pointeur.getValue()].getTexte());
						textesAffiches[pointeur.getValue()]=true;
					}
				}
				if(frame==6){
					frame=-1;
				}
				frame++;
				// System.out.println("frame n°"+frame);
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			
			if(!fermetureMenu){
				if(bs.selection(clavier)){
				bi.setImage(tableau[pointeur.getValue()].getChemin());

				fontSelect = null;
				try{
				File in = new File("fonts/PrStart.ttf");
				fontSelect = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(48.0f);
				}catch (Exception e) {
				System.err.println(e.getMessage());
				fontSelect = new Font("SansSerif", Font.PLAIN, 48);
				}

				// if(!tableau[pointeur.getValue()].getTexte().getPolice().equals(fontSelect)){
				// tableau[pointeur.getValue()].getTexte().setPolice(fontSelect);
				// }
				
				
				
				

				tableau[pointeur.getValue()].getTexte().setPolice(font);

				bd.lireFichier(tableau[pointeur.getValue()].getChemin());
				bd.lireHighScore(tableau[pointeur.getValue()].getChemin());
				bd.lireBouton(tableau[pointeur.getValue()].getChemin());
				/*
				// System.out.println(tableau[pointeur.getValue()].getChemin());
				// bd.setMessage(tableau[pointeur.getValue()].getNom());
				*/
				pointeur.lancerJeu(clavier);
				
				
				}else{
					f.ajouter(fondBlancTransparent);
					f.ajouter(message);
					f.ajouter(rectSelection);
					f.ajouter(boutonNon);
					f.ajouter(boutonOui);
					f.ajouter(non);
					f.ajouter(oui);
					fermetureMenu=true;
					
				}
			}else{
					if(clavier.getJoyJ1DroiteEnfoncee()){
						selectionSur=1;
					}
						
					if(clavier.getJoyJ1GaucheEnfoncee()){
						selectionSur=0;
					}
					   
					
						if(selectionSur==0){
							rectSelection.setA(pointEcran(330,590));
							rectSelection.setB(pointEcran(550,710));
						}
						else{
							rectSelection.setB(pointEcran(950,710));
							rectSelection.setA(pointEcran(730,590));
							
						}
					if(clavier.getBoutonJ1ATape()){
						if(selectionSur==0){
							f.supprimer(fondBlancTransparent);
							f.supprimer(message);
							f.supprimer(rectSelection);
							f.supprimer(boutonNon);
							f.supprimer(boutonOui);
							f.supprimer(non);
							f.supprimer(oui);
							fermetureMenu=false;
						}
						else{
							System.exit(0);
						}
					}

			}
			f.rafraichir();
		}//fin while true
    }
    
	/**
	 * Sélectionne aléatoirement une musique dans le répertoire `sound/bg/`  et la joue en boucle
	 */

    public static void lectureMusiqueFond() {
	if(tableauMusiques == null || cptMus <= 0){
	    return;
	}
    	musiqueFond = new Bruitage ("sound/bg/"+tableauMusiques[(int)(Math.random()*cptMus)]);
    	musiqueFond.lecture();
    }
	
	/**
	 * Arrête la lecture de la musique de fond actuelle (utilisé au lancement d'un jeu).
	 */

	public static void stopMusiqueFond(){
		if(musiqueFond == null){
		    return;
		}
		musiqueFond.arret();
	}
	
	/**
	 * Affiche le texte associé à un bouton de jeu spécifique.
	 * @param valeur L'index du jeu dans le tableau des boutons.
	 */
	public static void afficherTexte(int valeur){
			f.ajouter(tableau[valeur].getTexte());
		}

	private void appliquerDecalageInterface() {
	    if (DECALAGE_FINAL_X == 0 && DECALAGE_FINAL_Y == 0) {
		return;
	    }

	    pointeur.getTriangleGauche().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    pointeur.getTriangleDroite().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    pointeur.getRectangleCentre().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);

	    for (int i = 0; i < tableau.length; i++) {
		if (tableau[i].getTexture() != null) {
		    tableau[i].getTexture().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
		}
		if (tableau[i].getTexte() != null) {
		    tableau[i].getTexte().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
		}
	    }

	    for (int i = 0; i < bd.getMessage().length; i++) {
		bd.getMessage()[i].translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    }
	    for (int i = 0; i < bd.getBouton().length; i++) {
		bd.getBouton()[i].translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    }
	    for (int i = 0; i < bd.gettBouton().length; i++) {
		bd.gettBouton()[i].translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    }
	    for (int i = 0; i < bd.getListeHighScore().length; i++) {
		bd.getListeHighScore()[i].translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    }
	    bd.getJoystick().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    bd.gettJoystick().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	    bd.getHighscore().translater(DECALAGE_FINAL_X, DECALAGE_FINAL_Y);
	}

	private static Point pointEcran(int x, int y) {
	    return new Point(x + DECALAGE_FINAL_X, y + DECALAGE_FINAL_Y);
	}

	private static void executerSurEDT(boolean attendre, Runnable action) {
	    if (SwingUtilities.isEventDispatchThread()) {
		action.run();
		return;
	    }
	    if (attendre) {
		try {
		    SwingUtilities.invokeAndWait(action);
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		}
	    } else {
		SwingUtilities.invokeLater(action);
	    }
	}

	private static GraphicsDevice recupererEcranPrincipal() {
	    try {
		GraphicsDevice[] ecrans = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		if (ecrans.length > 0) {
		    return ecrans[0];
		}
	    } catch (Exception e) {
		// On reste compatible même si l'environnement graphique est indisponible.
	    }
	    return null;
	}

	private static int calculerDecalageX() {
	    if (ECRAN == null || !ECRAN.isFullScreenSupported() || ECRAN.getDisplayMode() == null) {
		return 0;
	    }
	    return Math.max(0, (ECRAN.getDisplayMode().getWidth() - TAILLEX) / 2);
	}

	private static int calculerDecalageY() {
	    if (ECRAN == null || !ECRAN.isFullScreenSupported() || ECRAN.getDisplayMode() == null) {
		return 0;
	    }
	    return Math.max(0, (ECRAN.getDisplayMode().getHeight() - TAILLEY) / 2);
	}

	private static void libererPleinEcranMenu() {
	    if (ECRAN == null) {
		return;
	    }
	    try {
		if (ECRAN.getFullScreenWindow() == f) {
		    ECRAN.setFullScreenWindow(null);
		}
	    } catch (Exception e) {
		// Ignore: certains WM n'autorisent pas cette opération.
	    }
	}

	private static void activerPleinEcranMenu() {
	    if (ECRAN == null) {
		return;
	    }
	    try {
		ECRAN.setFullScreenWindow(f);
	    } catch (Exception e) {
		// Ignore: fallback en fenêtre classique.
	    }
	}

	public static void masquerMenuPourJeu() {
	    executerSurEDT(true, new Runnable() {
		@Override
		public void run() {
		    libererPleinEcranMenu();
		    f.setVisible(false);
		}
	    });
	}

	public static void reprendreFocusMenu() {
	    executerSurEDT(false, new Runnable() {
		@Override
		public void run() {
		    if (!f.isVisible()) {
			f.setVisible(true);
		    }
		    activerPleinEcranMenu();
		    f.setState(Frame.NORMAL);
		    f.toFront();
		    f.requestFocus();
		    f.requestFocusInWindow();
		}
	    });
	}

	public static void restaurerMenuApresJeu(final ClavierBorneArcade clavier) {
	    if (clavier != null) {
		clavier.reset();
	    }
	    executerSurEDT(true, new Runnable() {
		@Override
		public void run() {
		    f.setVisible(true);
		    activerPleinEcranMenu();
		    f.setState(Frame.NORMAL);
		    f.toFront();
		    f.requestFocus();
		    f.requestFocusInWindow();
		}
	    });

	    // Re-tente légèrement après le retour du jeu pour les WM capricieux.
	    Timer rattrapageFocus = new Timer(120, e -> {
		f.toFront();
		f.requestFocus();
		f.requestFocusInWindow();
		if (clavier != null) {
		    clavier.reset();
		}
		((Timer) e.getSource()).stop();
	    });
	    rattrapageFocus.setRepeats(false);
	    rattrapageFocus.start();
	}
}
