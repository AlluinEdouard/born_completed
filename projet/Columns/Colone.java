import MG2D.Fenetre;

public class Colone {
    //CONSTANTES
    public static final int GEMMESPARCOLONE=3;

    //ATTRIBUTS
    private Gemme gemmes[];
    //Coordonnées de la gemme du bas
    private double x;
    private int y;

    public Colone(double x, int y){
        this.gemmes=new Gemme[3];
        for(int i=0;i<GEMMESPARCOLONE;i++)
            this.gemmes[i]=new Gemme();
        this.x=x;
        this.y=y;
    }

    public Gemme getGemme(int i){
        return this.gemmes[i];
    }

    public void intervertir(){
        Gemme gemmeEchange=this.gemmes[2];
        this.gemmes[2]=this.gemmes[0];
        this.gemmes[0]=this.gemmes[1];
        this.gemmes[1]=gemmeEchange;
    }

    public int getX(){
        return (int)(this.x);
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x=x;
    }

    public void setY(int y){
        this.y=y;
    }

    public void afficher(Fenetre f,int x, int y){
        for(int i=0;i<GEMMESPARCOLONE;i++){
            if(this.y+i<Puits.LIGNEVIE)
                this.gemmes[i].afficher((int)(x+this.x*Gemme.COTE),y+(this.y+i)*Gemme.COTE);
        }
    }

    public boolean descendre(Gemme[][] grille){
        boolean res=false;
        if(this.y>0){
            //System.out.println(grille[this.x][this.y-1]);
            if(grille[(int)(this.x)][this.y-1].getCouleur()==0) {
                this.y--;
                res=true;
            }
        }
        //System.out.println("y="+y+" donc "+res);
        return res;
    }

    public int getCouleurGemme(int i){
        return this.gemmes[i].getCouleur();
    }

    public String toString(){
        String res=new String("(");
        for(int i=0;i<GEMMESPARCOLONE;i++){
            res+=gemmes[i]+";";
        }
        res+=")";
        return res;
    }

    public void deplacer(int dir, Gemme[][] grille){
        if(dir==1){
            if(this.x<Puits.LARGEUR-1){
                if(grille[(int)(this.x+1)][this.y].getCouleur()==0)
                    this.x++;
            }

        }
        if(dir==-1){
            if(this.x>0) {
                if (grille[(int)(this.x - 1)][this.y].getCouleur() == 0)
                    this.x--;
            }
        }
    }

    public boolean peutDescendre(Gemme[][] grille){
        boolean res=false;
        if(this.y>0){
            //System.out.println(grille[this.x][this.y-1]);
            if(grille[(int)(this.x)][this.y-1].getCouleur()==0) {
                res=true;
            }
        }
        //System.out.println("y="+y+" donc "+res);
        return res;
    }

    public void effacerColone(Fenetre f){
        for(int i=0;i<GEMMESPARCOLONE;i++) {
            this.gemmes[i].effacerGemme(f);
        }
    }




}