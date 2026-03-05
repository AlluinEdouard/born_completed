import MG2D.*;
import MG2D.geometrie.*;

class Tir{

    /*-----Attribut-----*/
    private Texture tex;
    private int dmg;
    private int vit;
    private String orientation;

    /*-----Constructeur-----*/
    public Tir(){ // Défault
	tex=new Texture("./img/laser/player1/1.png", new Point(0,0));
	vit=1;
	dmg=1;
	orientation="droit";
    }

    public Tir(Texture tex, int dmg, int vit, String orientation){ // Choix
	this.tex=new Texture(tex);
	this.dmg=dmg;
	this.vit=vit;
	this.orientation=orientation; 
    }


    /*-----Get && Set-----*/
    public int getVit(){
	return vit;
    }

    public Texture getTex(){
	return tex;
    }

    public int getDmg(){
	return dmg;
    }

    public String getOrientation(){
	return orientation;
    }


    public void setVit(int vit){
	this.vit=vit;
    }

    public void setTex(Texture tex){
	this.tex=tex;
    }
	
    public void setDmg(int dmg){
	this.dmg=dmg;
    }

    public void setOrientation(){
	this.orientation=orientation;
    }

    public boolean intersection(Ennemi e){
	return tex.intersection(e.getTex());
    }


    /*-----Méthode-----*/
    public String toString(){
	return "Texture : "+tex+"\nDommage du tir : "+dmg+"\nVitesse du tir : "+vit+"\nOrientation du tir : "+orientation;
    }

    public boolean equals(Tir t){
	return ((tex.equals(t.getTex()))&&(dmg==t.dmg)&&(vit==t.vit)&&(orientation.equals(t.orientation)));
    }
}
