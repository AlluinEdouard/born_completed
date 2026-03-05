import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;

public class Boss extends Ennemi{
    /*-----Constructeur-----*/
    public Boss(){ // Défault
        super();
    }

    public Boss(Texture tex, int vit, int vie, int traj){ // Choix
        super(tex,vit,vie,traj);
    }

    public boolean apparition(){
        boolean res=true;
        this.tex.translater(-4,-3);
        if(this.tex.getB().getY()>=Jeu.HAUTEUR)
            res=false;
        return res;
    }

    public void deplacement(){
        this.tex.translater(0,this.traj);
        if(this.tex.getA().getY()<=0 || this.tex.getB().getY()>=Jeu.HAUTEUR){
            this.traj=-this.traj;
        }
    }

}
