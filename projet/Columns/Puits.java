import MG2D.Fenetre;
import MG2D.Couleur;
import MG2D.geometrie.*;
import MG2D.geometrie.Point;

import java.awt.*;
import java.io.InputStream;

public class Puits {
    //CONSTANTES
    public static final int LARGEUR=6;
    public static final int HAUTEUR=16;
    public static final int LIGNEVIE=13;

    //ATTRIBUTS
    private Colone coloneSuivante;
    private Colone coloneCourante;

    private final int xA;
    private final int yA;

    private Gemme[][] grille;
    private boolean[][] aSupprimer;

    private int numJoueur;

    private int numCombo;

    private Integer scoreTampon;
    private Integer score;
    private Integer niveau;
    private Integer gemmesSuppr;

    private int xScore;
    private int yScore;
    private Point pointScore;

    private int xScoreTampon;
    private int yScoreTampon;
    private Point pointScoreTampon;

    private int xGemmesSuppr;
    private int yGemmesSuppr;
    private Point pointGemmesSuppr;

    private int xNiveau;
    private int yNiveau;
    private Point pointNiveau;

    private Texte scoreAff;
    private Texte scoreTamponAff;
    private Texte niveauAff;
    private Texte gemmesSupprAff;

    private Font font;
    private Font fontTampon;



    public Puits(int x, int y, int numJoueur){
        this.numJoueur=numJoueur;
        this.xA=x;
        this.yA=y;

        this.grille=new Gemme[LARGEUR][HAUTEUR];
        this.aSupprimer=new boolean[LARGEUR][HAUTEUR];
        for(int i=0;i<LARGEUR;i++){
            for(int j=0;j<HAUTEUR;j++){
                this.grille[i][j]=new Gemme(Gemme.VIDE);
            }
        }

        this.font = null;
        try{
            String nomFont = "fonts/Norse-Bold.ttf";
            InputStream is = this.getClass().getResourceAsStream(nomFont);
            this.font = this.font.createFont(Font.TRUETYPE_FONT, is);
            this.font = this.font.deriveFont(42.0f);
            this.fontTampon = this.font.deriveFont(69.0f);
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }

        this.coloneSuivante=new Colone(6.5,10);
        this.nouvelleColone();

        this.scoreTampon=0;
        this.score=0;
        this.niveau=0;
        this.gemmesSuppr=0;

        //TODO: niveau + nb gemmes
        if(this.numJoueur==1){
            this.xScore=600;
            this.yScore=489;
            this.xScoreTampon=560;
            this.yScoreTampon=665;
            this.xGemmesSuppr=542;
            this.yGemmesSuppr=210;
            this.xNiveau=526;
            this.yNiveau=338;
        }else{
            this.xScore=670;
            this.yScore=404;
            this.xScoreTampon=720;
            this.yScoreTampon=565;
            this.xGemmesSuppr=734;
            this.yGemmesSuppr=114;
            this.xNiveau=750;
            this.yNiveau=242;
        }
        pointScore = new Point(this.xScore,this.yScore);
        pointScoreTampon = new Point(this.xScoreTampon,this.yScoreTampon);
        pointGemmesSuppr=new Point(this.xGemmesSuppr,this.yGemmesSuppr);
        pointNiveau=new Point(this.xNiveau,this.yNiveau);

        scoreAff = new Texte("",this.font,pointScore);
        scoreAff.setCouleur(Couleur.JAUNE);
        scoreTamponAff = new Texte("",this.fontTampon,pointScoreTampon);
        scoreTamponAff.setCouleur(Couleur.ROSE);
        gemmesSupprAff=new Texte("",this.font,pointGemmesSuppr);
        gemmesSupprAff.setCouleur(Couleur.JAUNE);
        niveauAff=new Texte("",this.font,pointNiveau);
        niveauAff.setCouleur(Couleur.JAUNE);


    }

    public Gemme[][] getGrille(){
        return this.grille;
    }

    public int getXA(){
        return this.xA;
    }

    public int getYA(){
        return this.yA;
    }



    public void afficher(Fenetre f){
        //Affichage des gemmes verrouillées
        for(int i=0;i<LARGEUR;i++){
            for(int j=0;j<LIGNEVIE;j++){
                if(grille[i][j].getCouleur()!=0){
                    grille[i][j].afficher(this.xA+i*Gemme.COTE,this.yA+j*Gemme.COTE);
                }
            }
        }
        this.affichageSuivant(f);
        scoreAff.setTexte(score.toString());
        scoreAff.setCouleur(Couleur.JAUNE);
        niveauAff.setTexte(niveau.toString());
        niveauAff.setCouleur(Couleur.JAUNE);
        gemmesSupprAff.setTexte(gemmesSuppr.toString());
        gemmesSupprAff.setCouleur(Couleur.JAUNE);
        //f.ajouter(scoreAff);
        //f.ajouter(niveauAff);
        //f.ajouter(gemmesSupprAff);
        Partie.ajouterTex((Dessin)(scoreAff));
        Partie.ajouterTex((Dessin)(niveauAff));
        Partie.ajouterTex((Dessin)(gemmesSupprAff));
        if(scoreTampon!=0){
            scoreTamponAff.setTexte(scoreTampon.toString());
            scoreTamponAff.setCouleur(Couleur.ROSE);
            //f.ajouter(scoreTamponAff);
            Partie.ajouterTex((Dessin)(scoreTamponAff));
        }
        //f.ajouter(new Texte(score.toString(),this.font,new Point(this.xScore,this.yScore)));
    }

    public void affichageCourant(Fenetre f){
        //Affichage des colones courante et suivante
        for(int i=0;i<Colone.GEMMESPARCOLONE;i++) {
            //t=new Texture("img/game/gems/"+this.coloneCourante.getGemme(i).getCouleur()+".png",new Point(this.coloneCourante.getX(),this.coloneCourante.getX()+i*Gemme.COTE));
            //this.coloneCourante.afficher(f,this.coloneCourante.getX(),this.coloneCourante.getY());
            this.coloneCourante.afficher(f,this.xA,this.yA);
        }
    }

    public void affichageSuivant(Fenetre f){
        //Affichage des colones courante et suivante
        for(int i=0;i<Colone.GEMMESPARCOLONE;i++) {
            this.coloneSuivante.afficher(f,this.xA,this.yA);
        }
    }

    public void nouvelleColone(){
        this.coloneCourante=this.coloneSuivante;
        this.coloneCourante.setX(2);
        this.coloneCourante.setY(13);
        double x;
        int y=10;
        switch(this.numJoueur){
            case 1:
                x=6.5;
                break;
            case 2:
                x=-1.5;
                break;
            default:
                x=6.5;
                break;
        }
        this.coloneSuivante=new Colone(x,y);
    }

    public Colone getColoneCourante(){
        return this.coloneCourante;
    }

    public void verrouiller(){
        for(int i=0;i<Colone.GEMMESPARCOLONE;i++){
            this.grille[this.coloneCourante.getX()][this.coloneCourante.getY()+i]=this.coloneCourante.getGemme(i);
        }
    }


    //TODO : vérification des combos pour chaque duo de coordonnées dans chaque direction
    //TODO : on appelle la fonction la première fois avec un niveau de 1
    //TODO : suppression pour les mêmes coordonnées et direction si niveau >=3
    public int verification(){
        int res=0;
        for(int i=0;i<LARGEUR;i++){
            for(int j=0;j<LIGNEVIE;j++){
                    res+=verifSuppr(i, j, -1, 1);
                    res+=verifSuppr(i, j, -1, 0);
                    res+=verifSuppr(i, j, -1, -1);
                    res+=verifSuppr(i, j, 0, 1);
                    res+=verifSuppr(i, j, 0, -1);
                    res+=verifSuppr(i, j, 0, -1);
                    res+=verifSuppr(i, j, 1, 1);
                    res+=verifSuppr(i, j, 1, 0);
                    res+=verifSuppr(i, j, 1, -1);
            }
        }
        res=0;
        for(int i=0;i<LARGEUR;i++){
            for(int j=0;j<LIGNEVIE;j++){
                if(aSupprimer[i][j]==true){
                    res++;
                    this.grille[i][j].setCouleur(-Gemme.NBFRAMESUPPR);
                    this.aSupprimer[i][j]=false;
                }
            }
        }
        //System.out.println(res + "gemmes supprimées!");
        this.gemmesSuppr+=res;
        return res;
    }

    //parcours récursif des éléments dans la direction indiquée à partir des coordonnées indiquées
    //retour du niveau de la vérif
    //arrêt si couleur du suivant différente ou limites du puits
    public int verifCombo(int x, int y, int dirX, int dirY, int niveau){
        int res=niveau;
        if(x+dirX>=0 && x+dirX<this.LARGEUR && y+dirY>=0 && y+dirY<this.LIGNEVIE){
            //System.out.println("On vérifie "+(x+dirX)+" ; "+(y+dirY));
            //System.out.println("Couleur actuelle : "+this.grille[x][y].getCouleur() + " donc "+(this.grille[x+dirX][y+dirY].getCouleur()==this.grille[x][y].getCouleur()));
            if(this.grille[x+dirX][y+dirY].getCouleur()==this.grille[x][y].getCouleur()){
                res=verifCombo(x+dirX, y+dirY,dirX,dirY,niveau+1);
            }
        }
        return res;
    }

    //suppression récursive des éléments dans la direction indiquée à partir des coordonnées indiquées
    //arrêt quand nombre = 0, nombre diminue de 1 à chaque appel
    public void supprCombo(int x, int y, int dirX, int dirY, int nombre){
        //this.grille[x][y]=new Gemme(-Gemme.NBFRAMESUPPR);
        this.aSupprimer[x][y]=true;
        if(nombre>1){
            //System.out.println("Supprimer encore : "+nombre);
            supprCombo(x+dirX, y+dirY,dirX,dirY,nombre-1);
        }
    }

    public int verifSuppr(int x, int y, int dirX, int dirY){
        int res=0;
        if(this.grille[x][y].getCouleur()>0) {
            int nb = verifCombo(x, y, dirX, dirY, 1);
            if (nb >= 3) {
                //System.out.println("On supprime " + nb + " à partir de " + x + " ; " + y);
                supprCombo(x, y, dirX, dirY, nb);
                res = nb;
            }
        }
        return res;
    }

    public int animSuppr(){
        int res=0;
        for(int i=0;i<LARGEUR;i++) {
            for (int j = 0; j < LIGNEVIE; j++) {
                if(this.grille[i][j].getCouleur()<0) {
                    res = this.grille[i][j].getCouleur();
                    res++;
                    this.grille[i][j]=new Gemme(res);
                }
            }
        }
        return res;
    }

    public boolean verifChute(){
        boolean res=false;
        for(int i=0;i<LARGEUR;i++) {
            for (int j = 0; j < LIGNEVIE; j++) {
                if(this.grille[i][j+1].getCouleur()>0 && this.grille[i][j].getCouleur()==0) {
                    res = true;
                    this.grille[i][j]=new Gemme(this.grille[i][j+1].getCouleur());
                    this.grille[i][j+1]=new Gemme(Gemme.VIDE);

                }
            }
        }
        return res;
    }


    public int getNumCombo(){
        return this.numCombo;
    }

    public void setNumCombo(int num){
        this.numCombo=num;
    }

    public void setScore(int score){this.score=score;}

    public int getScore(){
        //System.out.println(this.score);
        return this.score;
    }

    public void setScoreTampon(int scoreTampon){
        this.scoreTampon=scoreTampon;
    }

    public int getScoreTampon(){
        return this.scoreTampon;
    }

    public int getNiveau(){
        return this.niveau;
    }

    public void setNiveau(int niveau){
        this.niveau=niveau;
    }

    public int getGemmesSuppr(){
        return this.gemmesSuppr;
    }




}
