import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.awt.image.BufferedImage;


import MG2D.Couleur;
import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;
import MG2D.geometrie.Texte;

/**
 * Représente un bouton de sélection de jeu dans le menu de la borne d'arcade.
 * Un bouton est composé d'un texte, d'une texture graphique et contient les chemins vers les fichiers du jeu
 */

public class Bouton {
    private static final int LARGEUR_MAX_TEXTE_BOUTON = 340;
    private Texte texte;
    private String chemin;
    private String nom;
    private Texture texture;
    private int numeroDeJeu;

	/**
	 * Constructeur par défaut; Initailise tous les attributs à null
	 */

    public Bouton(){
	this.texte = null;
	this.texture = null;
	this.chemin = null;
	this.nom = null;
    }

	/**
	 * Constructeur avec paramètres.
	 * @param texte L'objet Texte MG2D à afficher.
	 * @param texture L'objet Texture MG2D représentant l'image du bouton.
	 * @param chemin Le chemin relatif vers le dossier du jeu.
	 * @param nom Le nom du jeu.
	 */

    public Bouton(Texte texte, Texture texture, String chemin, String nom){
	this.texte = texte;
	this.texture = texture;
	this.chemin = chemin;
	this.nom = nom;
    }

	/**
	 * Scanne le répertoire "projet/" pour détecter les jeux disponibles.
	 * Instancie et configure automatiquement les boutons pour le menu en fonction des dossiers trouvés.
	 */

    public static void remplirBouton(){
	for(int i = 0 ; i < Graphique.tableau.length ; i++){
	    Graphique.tableau[i] = new Bouton();
	}

	Path yourPath = FileSystems.getDefault().getPath("projet/");

	try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(yourPath)) {
	    int i = Graphique.tableau.length - 1;
	    for (Path path : directoryStream) {
		if(i < 0){
		    break;
		}
		if(!Files.isDirectory(path)){
		    continue;
		}
		String nomJeu = path.getFileName().toString();
		Path script = FileSystems.getDefault().getPath(nomJeu + ".sh");
		if(!Files.isRegularFile(script)){
		    continue;
		}
		Font policeBouton = new Font("Calibri", Font.PLAIN, 26);
		Graphique.tableau[i].setTexte(new Texte(Couleur .NOIR, tronquerTexte(nomJeu, policeBouton, LARGEUR_MAX_TEXTE_BOUTON), policeBouton, new Point(300, 510)));
		Graphique.tableau[i].setTexture(new Texture("img/bouton2.png", new Point(100, 478), 400, 65));
		for(int j=0;j<Graphique.tableau.length-(i+1);j++){
		    Graphique.tableau[i].getTexte().translater(0,-110);
		    Graphique.tableau[i].getTexture().translater(0,-110);
		}
		Graphique.tableau[i].setChemin("projet/"+nomJeu);
		Graphique.tableau[i].setNom(nomJeu);
		Graphique.tableau[i].setNumeroDeJeu(i);
		i--;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public String getChemin() {
	return chemin;
    }

    public void setChemin(String chemin) {
	this.chemin = chemin;
    }

    public String getNom() {
	return nom;
    }

    public void setNom(String nom) {
	this.nom = nom;
    }

    public Texte getTexte() {
	return texte;
    }

    public void setTexte(Texte texte) {
	this.texte = texte;
    }

    public Texture getTexture() {
	return texture;
    }

    public void setTexture(Texture texture) {
	this.texture = texture;
    }

    public int getNumeroDeJeu() {
	return numeroDeJeu;
    }

    public void setNumeroDeJeu(int numeroDeJeu) {
	this.numeroDeJeu = numeroDeJeu;
    }

    private static String tronquerTexte(String texte, Font police, int largeurMax){
	if(texte == null){
	    return "";
	}

	BufferedImage imageMesure = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = imageMesure.createGraphics();
	try{
	    FontMetrics metrics = graphics.getFontMetrics(police);
	    if(metrics.stringWidth(texte) <= largeurMax){
		return texte;
	    }
	    String suffixe = "...";
	    int largeurSuffixe = metrics.stringWidth(suffixe);
	    if(largeurSuffixe >= largeurMax){
		return suffixe;
	    }
	    StringBuilder resultat = new StringBuilder();
	    for(int i = 0 ; i < texte.length() ; i++){
		char c = texte.charAt(i);
		if(metrics.stringWidth(resultat.toString() + c) + largeurSuffixe > largeurMax){
		    break;
		}
		resultat.append(c);
	    }
	    return resultat.toString() + suffixe;
	} finally {
	    graphics.dispose();
	}
    }
}
