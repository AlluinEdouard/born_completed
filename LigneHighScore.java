/**
 * Représente une entrée dans le tableau des meilleurs scores.
 * Contient le nom du joueur (limité à 3 caractères) et son score.
 */

class LigneHighScore{
    private String nom;
    private int score;

	/**
	 * Constructeur par défaut.
	 * Initialise le nom à "AAA" et le score à 0
	 */

    public LigneHighScore(){
	nom="AAA";
	score=0;
    }

	/**
	 * Constructeur avec paramètres.
	 * @param nnom Le nom du joueur (sera tronqué ou remplacé par "AAA" si plus de 3 caractères).
	 * @param sscore Le score obtenu (sera mis à 0 si négatif).
	 */

    public LigneHighScore(String nnom, int sscore){
	if(nnom.length()>3)
	    nnom="AAA";
	else
	    nom=new String(nnom);
	if(sscore<0)
	    score=0;
	else
	    score=sscore;
    }

	/**
	 * Constructeur par copie
	 * @param l L'objet LigneHighScore à copier
	 */

    public LigneHighScore(LigneHighScore l){
	nom=new String(l.nom);
	score=l.score;
    }

	/**
	 * Constructeur depuis une chaîne de caractères.
	 * @param str Chaîne formatée sous la forme "NOM-SCORE"
	 */

    public LigneHighScore(String str){
	String[] tab = str.split("-");
	if(tab.length!=2){
	    nom = "AAA";
	    score=0;
	}else{
	    nom=new String(tab[0]);
	    score = Integer.parseInt(tab[1]);
	}
	    
    }

	/**
	 * Récupère le score.
	 * @return Le score en entier.
	 */

    public int getScore(){
	return score;
    }

	/**
	 * Récupère le nom du joueur.
	 * @return Le nom sous forme de chaîne de caractères.
	 */

    public String getNom(){
	return nom;
    }

	/**
	 * Convertit la ligne de score en chaîne de caractères pour la sauvergarde.
	 * @return Une chaîne au format "NOM-SCORE".
	 */

    public String toString(){
	return nom+"-"+score;
    }
}
