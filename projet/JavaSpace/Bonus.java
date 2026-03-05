import MG2D.*;
import MG2D.geometrie.*;

class Bonus{


    /*-----Attribut-----*/
    private Texture tex;
    private int numBonus;
    private int duree;


    /*-----Constructeur-----*/
    public Bonus(){ // Défault
	tex=new Texture("./img/ennemie/ennemie1/1.png", new Point(0,0));
	numBonus=0;
	duree=0;
    }

    public Bonus(Texture tex, int numBonus, int duree){ // Choix
	this.tex=new Texture(tex);
	this.numBonus=numBonus;
	this.duree=duree;
    }


    /*-----Get && Set-----*/
    public int getNumBonus(){
	return numBonus;
    }

    public Texture getTex(){
	return tex;
    }

    public int getDuree(){
	return duree;
    }

    public void setNumBonus(int numBonus){
	this.numBonus=numBonus;
    }

    public void setDuree(int duree){
	this.duree=duree;
    }

    public void setTex(Texture tex){
	this.tex=tex;
    }


    /* Méthode */
    public String toString(){
	return "Texture : "+tex+"\nNumero du bonus : "+numBonus+"\nDuree du bonus : "+duree;
    }

    public boolean equals(Bonus b){
	return ((tex.equals(b.getTex()))&&(numBonus==b.numBonus)&&(duree==b.duree));
    }
}
