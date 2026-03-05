import MG2D.*;
import MG2D.geometrie.*;

class Joueur{
	

    /*-----Attribut-----*/
    private Texture tex;
    private int vie;
    private int tempVie;
    private int tir;
    private int tempTir;
    private int bonus;
    private int tempBonus;


    /*-----Constructeur-----*/
    public Joueur(){ // Défault
	tex=new Texture("./img/player/player1/3.png", new Point(0,0));
	vie=0;
	tempVie=0;
	tir=0;
	tempTir=0;
	bonus=0;
	tempBonus=0;
    }

    public Joueur(Joueur a){ // Copie
	tex=new Texture(a.getTex());
	vie=a.getVie();
	tempVie=a.getTempVie();
	tir=a.getTir();
	tempTir=a.getTempTir();
	bonus=a.getBonus();
	tempBonus=a.getTempBonus();
    }

    public Joueur(Texture tex, int vie, int tempVie, int tir, int tempTir, int bonus, int tempBonus){ // Choix
	this.tex=new Texture(tex);
	this.vie=vie;
	this.tempVie=tempVie;
	this.tir=tir;
	this.tempTir=tempTir;
	this.bonus=bonus;
	this.tempBonus=tempBonus;
    }


    /*-----Get && Set-----*/
    public Texture getTex(){
	return tex;
    }

    public int getVie(){
	return vie;
    }

    public int getTempVie(){
	return tempVie;
    }

    public int getTir(){
	return tir;
    }

    public int getTempTir(){
	return tempTir;
    }

    public int getBonus(){
	return bonus;
    }

    public int getTempBonus(){
	return tempBonus;
    }

    public void setTex(Texture tex){
	this.tex=tex;
    }

    public void setVie(int vie){
	this.vie=vie;
    }

    public void setTempVie(int tempVie){
	this.tempVie=tempVie;
    }

    public void setTir(int tir){
	this.tir=tir;
    }

    public void setTempTir(int tempTir){
	this.tempTir=tempTir;;
    }

    public void getBonus(int bonus){
	this.bonus=bonus;
    }

    public void setTempBonus(int tempBonus){
	this.tempBonus=tempBonus;;
    }


    /*-----Méthode-----*/
    public boolean intersection(Ennemi e){
	return tex.intersection(e.getTex());
    }

    public boolean intersection(Tir t){
	return tex.intersection(t.getTex());
    }

    public boolean intersection(Bonus b){
	return tex.intersection(b.getTex());
    }

    public void bougerJoueur(ClavierBorneArcade cla, int xMin, int xMax, int yMin, int yMax, int xDecalage, int yDecalage, int largeurJoueur, int hauteurJoueur){ // Méthode afin de bouger un joueur sur une fenètre
	if(cla.getJoyJ1GaucheEnfoncee() && tex.getB().getX()>(largeurJoueur+5)){ // Déplacement vers la gauche avec limite de la bordure
	    tex.translater(-xDecalage,0);
	}

	if(cla.getJoyJ1DroiteEnfoncee() && tex.getA().getX()<(xMax-largeurJoueur-5)){ // Déplacement vers la droite avec limite de la bordure
	    tex.translater(+xDecalage,0);
	}
	
	if(cla.getJoyJ1BasEnfoncee() && tex.getB().getY()>(hauteurJoueur+5)){ // Déplacement vers le bas avec limite de la bordure
	    tex.translater(0,-yDecalage);
	}
	
	if(cla.getJoyJ1HautEnfoncee() && tex.getA().getY()<(yMax-hauteurJoueur-5)+152){ // Déplacement vers le haut et limite bordure haute
	    tex.translater(0,+yDecalage);    
	}
    }

    public String toString(){
	return "Texture : "+tex+"\nVie : "+vie+"\nTemporisation de vie : "+tempVie+"\nTir : "+tir+"\nTemporisation du tir : "+tempTir+"\nBonus actuel : "+bonus+"\nDuree du bonus actuel : "+tempBonus;
    }

    public boolean equals(Joueur j){
	return ((tex.equals(j.getTex()))&&(vie==j.vie)&&(tempVie==j.tempVie)&&(tir==j.tir)&&(tempTir==j.tempTir)&&(bonus==j.bonus)&&(tempBonus==j.tempBonus));
    }

}
