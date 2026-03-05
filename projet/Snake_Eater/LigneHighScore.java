class LigneHighScore{
    private String nom;
    private int score;

    public LigneHighScore(){
	nom="AAA";
	score=0;
    }

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

    public LigneHighScore(LigneHighScore l){
	nom=new String(l.nom);
	score=l.score;
    }

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

    public int getScore(){
	return score;
    }

    public String getNom(){
	return nom;
    }

    public String toString(){
	return nom+"-"+score;
    }
}
