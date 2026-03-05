import MG2D.*;
import MG2D.geometrie.*;

class Ennemi{


    /*-----Attribut-----*/
    protected Texture tex;
    protected int vit;
    protected int vie;
    protected int traj;



    /*-----Constructeur-----*/
    public Ennemi(){ // Défault
	tex=new Texture("./img/ennemie/ennemie1/1.png", new Point(0,0));
	vit=0;
	vie=0;
	traj=0;
    }

    public Ennemi(Ennemi a){ // Copie
	tex=new Texture(a.getTex());
	vit=a.getVit(); 
	vie=a.getVie();
	traj=a.getTraj();
    }

    public Ennemi(Texture tex, int vit, int vie, int traj){ // Choix
	this.tex=new Texture(tex);
	this.vit=vit;
	this.vie=vie;
	this.traj=traj; 
    }


    /*-----Get && Set-----*/
    public int getVit(){
	return vit;
    }

    public Texture getTex(){
	return tex;
    }

    public int getVie(){
	return vie;
    }

    public int getTraj(){
	return traj;
    }

    public void setVit(int vit){
	this.vit=vit;
    }

    public void setTex(Texture tex){
	this.tex=tex;
    }

    public void setVie(int vie){
	this.vie=vie;
    }

 
    public void setTraj(int traj){
	this.traj=traj;
    }


    /*-----Méthode-----*/
    public String toString(){
	return "Texture : "+tex+"\nVitesse : "+vit+"\nVie : "+vie+"\nTrajectoire : "+traj;
    }

    public boolean equals(Ennemi e){
	return ((tex.equals(e.getTex()))&&(vit==e.vit)&&(vie==e.vie)&&(traj==e.traj));
    }
}
