import MG2D.Fenetre;
import MG2D.geometrie.Dessin;
import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;

public class Gemme {
    //CONSTANTES
    public static final int JAUNE=1;
    public static final int ORANGE=2;
    public static final int VERT=3;
    public static final int VIOLET=4;
    public static final int ROUGE=5;
    public static final int BLEU=6;

    public static final int VIDE=0;
    public static final int NBFRAMESUPPR=7;

    public static final int COTE=64;

    public static final Texture TEXJAUNE=new Texture("img/game/gems/1.png", new Point());
    public static final Texture TEXORANGE=new Texture("img/game/gems/2.png", new Point());
    public static final Texture TEXVERT=new Texture("img/game/gems/3.png", new Point());
    public static final Texture TEXVIOLET=new Texture("img/game/gems/4.png", new Point());
    public static final Texture TEXROUGE=new Texture("img/game/gems/5.png", new Point());
    public static final Texture TEXBLEU=new Texture("img/game/gems/6.png", new Point());


   //TODO : constructeur par copie de texture => oui

    private int couleur;

    private Texture t;

    public Gemme(){
        this.couleur=(int)(Math.random()*6+1);
        this.t=new Texture("img/game/gems/" + this.getCouleur() + ".png", new Point());
    }

    public Gemme(int i){
        this.couleur=i;
        /*if(i!=0){
            this.t=new Texture("img/game/gems/" + this.getCouleur() + ".png", new Point());
        }*/

        switch(i){
            case VIDE :
                break;
            case JAUNE:
                this.t=new Texture(TEXJAUNE);
                break;
            case ORANGE :
                this.t=new Texture(TEXORANGE);
                break;
            case VERT:
                this.t=new Texture(TEXVERT);
                break;
            case VIOLET:
                this.t=new Texture(TEXVIOLET);
                break;
            case ROUGE :
                this.t=new Texture(TEXROUGE);
                break;
            case BLEU:
                this.t=new Texture(TEXBLEU);
                break;
            default:
                this.t=new Texture("img/game/gems/" + this.getCouleur() + ".png", new Point());
                break;
        }

    }

    public Gemme(Gemme g){
        this.couleur=g.getCouleur();
    }

    public int getCouleur(){
        return this.couleur;
    }

    public void setCouleur(int couleur){
        this.couleur=couleur;
    }

    public void afficher(int x, int y) {
        this.t.setA(new Point(x,y));
        //f.ajouter(this.t);
        Partie.ajouterTex((Dessin)(this.t));
    }

    //TODO : utiliser la méthode déplacer afin d'économiser en ressources sur le f.ajouter!!!
    //EUH... C'est possible..?
    public void deplacer(int x, int y){
        this.t.translater(x,y);
    }

    public void effacerGemme(Fenetre f) {
        f.supprimer(this.t);
    }

}
