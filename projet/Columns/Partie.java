import MG2D.Fenetre;
import MG2D.audio.Bruitage;
import MG2D.geometrie.Dessin;
import MG2D.geometrie.Point;
import MG2D.geometrie.Texture;

import java.util.ArrayList;

public class Partie {
    //CONSTANTES
    //Statut de la partie
    public static final int MENU=0;
    public static final int INTRO=1;
    public static final int JEU=2;
    public static final int GAMEOVER=3;

    //Statut de la colone
    public static final int CHUTE=0;
    public static final int VEROUILLAGE=1;
    public static final int ATTENTE=2;
    public static final int COMBO=3;
    public static final int POSTCOMBO=4;

    //Constantes de temps
    public static final int TEMPSCHUTE=16;
    public static final int TEMPSVERR=16;
    public static final int TEMPSATT=4;

    //Constantes de son
    public static final int NBSONS=11;

    public static final int NUMMUSIQUE=0;
    public static final int NUMCHUTE=1;
    public static final int NUMSUPPR1=2;
    public static final int NUMSUPPR2=3;
    public static final int NUMSUPPR3=4;
    public static final int NUMSUPPR4=5;
    public static final int NUMSUPPR5=6;
    public static final int NUMSWITCH=7;
    public static final int NUMLEVEL =9;
    public static final int NUMSTART=10;

    public static ArrayList<Dessin> listeTextures;

    //Constante de progression
    public static final int GEMMESPARNIVEAU=35;
    public static final int VARSCORE=30;

    //ATTRIBUTS
    private int nbJoueurs;
    private int statut1;
    private int statut2;
    private int statutColone1;
    private int statutColone2;
    private boolean finPartie;

    private Puits puits1;
    private Puits puits2;

    private Fenetre f;

    private Texture bg;

    Bruitage mus;           //0
    Bruitage sonChute;      //1
    Bruitage sonSuppr1;     //2
    Bruitage sonSuppr2;     //3
    Bruitage sonSuppr3;     //4
    Bruitage sonSuppr4;     //5
    Bruitage sonSuppr5;     //6
    Bruitage sonSwitch;     //7
    Bruitage sonGameOver;   //8
    Bruitage sonLevel;      //9
    Bruitage sonStart;      //10

    private int tempsJoue[];
    private int tempsMax[];
    private boolean enLecture[];

    private boolean sonChuteJoue;

    private int nbGemmesSuppr;



    private int compteur1;
    private int compteur2;

    public Partie(){

    }

    public Partie(Fenetre f, int nbJoueurs){
        listeTextures=new ArrayList<Dessin>();
        this.f=f;
        this.nbJoueurs=nbJoueurs;
        this.statut1=MENU;
        this.statutColone1 =CHUTE;
        this.statut2=GAMEOVER;
        if(nbJoueurs==2){
            this.statut2=MENU;
            this.statutColone2=CHUTE;
        }

        this.finPartie=false;

        this.compteur1=0;
        this.compteur2=0;

        this.puits1 =new Puits(64,96,1);
        if(nbJoueurs==2)
            this.puits2 =new Puits(832,96,2);

        this.bg = new Texture("img/game/bg.png",new Point());
        f.ajouter(this.bg);

        mus=new Bruitage("sounds/music/Clotho.mp3");
        this.sonChute=new Bruitage("sounds/chute.mp3");
        this.sonSwitch=new Bruitage("sounds/switch.mp3");
        this.sonSuppr1=new Bruitage("sounds/suppr1.mp3");
        this.sonSuppr2=new Bruitage("sounds/suppr2.mp3");
        this.sonSuppr3=new Bruitage("sounds/suppr3.mp3");
        this.sonSuppr4=new Bruitage("sounds/suppr4.mp3");
        this.sonSuppr5=new Bruitage("sounds/suppr5.mp3");
        this.sonGameOver=new Bruitage("sounds/gameover.mp3");
        this.sonLevel=new Bruitage("sounds/level.mp3");
        this.sonStart=new Bruitage("sounds/start.mp3");

        this.sonChuteJoue=false;


        this.tempsMax= new int[]{100000, 1600, 950, 950, 950, 950, 950, 260, 195, 860, 5400};     //temps en ms
        //this.tempsMax= new int[]{122000, 1600, 950, 950, 950, 950, 950, 260, 195, 860, 6181};     //temps en ms

        this.tempsJoue=new int[NBSONS];
        this.enLecture=new boolean[NBSONS];
        for(int i=0;i<NBSONS;i++){
            this.tempsJoue[i]=0;
            this.enLecture[i]=false;
        }

        f.rafraichir();
    }

    public boolean prochaineFrame(int direction1, int boutonEnfonce1, int direction2, int boutonEnfonce2){



        //Parcours de tous les sons
        for(int i=0;i<NBSONS;i++){
            this.arretSon(i);
        }
        if(enLecture[NUMMUSIQUE]==false && (this.statut1==JEU || this.statut2==JEU)){
            System.out.println("ON REJOUE LA MUSIQUE");
            this.jouerSon(NUMMUSIQUE);
        }

        /*(
        try{

        }catch(Exception e){
        }
        */

        this.prochaineFrameJoueur(1,direction1,boutonEnfonce1);

        if(this.nbJoueurs==2){
            this.prochaineFrameJoueur(2,direction2,boutonEnfonce2);
        }

        //System.out.println("Statut1 : "+this.statut1);
        //System.out.println("Statut2 : "+this.statut2);



        f.rafraichir();
        boolean res=this.statut1==GAMEOVER && this.statut2==GAMEOVER;
            if(res){
                try{
                    this.mus.arret();
                }catch(Exception e){}
            }

        return res;
    }



    public void prochaineFrameJoueur(int numJoueur, int direction, int boutonEnfonce){
        Puits puits;
        int statut;
        int statutColone;
        int compteur;
        switch(numJoueur){
            case 1:
                puits=this.puits1;
                statut=this.statut1;
                statutColone=this.statutColone1;
                compteur=this.compteur1;
                break;
            case 2:
                puits=this.puits2;
                statut=this.statut2;
                statutColone=this.statutColone2;
                compteur=this.compteur2;
                break;
            default:
                puits=this.puits1;
                statut=this.statut1;
                statutColone=this.statutColone1;
                compteur=this.compteur1;
                break;
        }


        Gemme[][] grille = puits.getGrille();

        switch(statut){
            case MENU:
                //TODO : Gérer menu partie!
                //System.out.println("aaajeje");
                this.jouerSon(NUMSTART);
                statut =INTRO;
                //statut =JEU;
                break;
            case INTRO:
                if(this.enLecture[NUMSTART]==false) {
                    statut = JEU;
                    this.jouerSon(NUMMUSIQUE);
                }
                break;
            case JEU:
                //System.out.println(statutColone);
                switch(statutColone){
                    case CHUTE:
                        //Décrémentation du compteur en fonction de la touche bas enfoncée ou non
                        //Rotation des gemmes en fonction du bouton action enfoncé ou non
                        if(boutonEnfonce==Controles.ACTION){
                            puits.getColoneCourante().intervertir();
                            this.jouerSon(NUMSWITCH);
                        }
                        if(direction== Controles.BAS){
                            compteur+=TEMPSCHUTE;
                            puits.setScore(puits.getScore()+1);
                        }else{
                            compteur++;
                            if(direction==Controles.DROITE){
                                puits.getColoneCourante().deplacer(1,grille);
                            }
                            if(direction==Controles.GAUCHE){
                                puits.getColoneCourante().deplacer(-1,grille);
                            }
                        }
                        if(compteur>=TEMPSCHUTE){

                            if(puits.getColoneCourante().descendre(puits.getGrille())==false){
                                //System.out.println("On passe au verrouillage");
                                statutColone =VEROUILLAGE;
                            }
                            if(puits.getColoneCourante().peutDescendre(puits.getGrille())==false){
                                if(this.sonChuteJoue==false){
                                    this.jouerSon(NUMCHUTE);
                                    this.sonChuteJoue=true;
                                }
                            }
                            compteur=0;
                        }

                        break;

                    case VEROUILLAGE:
                        //Décrémentation du compteur à moins qu'il n'y ait mouvement
                        //Si rotation, on fait rotater loul
                        //Si mouvement entrainant nouvelle chute, on remet le statut à chute

                        this.sonChuteJoue=false;
                        compteur++;
                        if(compteur>=TEMPSVERR){
                            puits.verrouiller();
                            statutColone =ATTENTE;
                            compteur=0;
                        }

                        if(boutonEnfonce==Controles.ACTION){
                            puits.getColoneCourante().intervertir();
                        }

                        if(direction==Controles.DROITE){
                            puits.getColoneCourante().deplacer(1,grille);
                        }
                        if(direction==Controles.GAUCHE){
                            puits.getColoneCourante().deplacer(-1,grille);
                        }

                        //Reprise de la chute si la colone a été déplacée au dessus d'un vide
                        if(puits.getColoneCourante().peutDescendre(puits.getGrille())==true){
                            //System.out.println("On passe au verrouillage");
                            statutColone =CHUTE;
                        }
                        break;

                    case ATTENTE:
                        grille = puits.getGrille();
                        //Vérification du GAME OVER
                        for(int i=0;i<Puits.LARGEUR;i++){
                            if(grille[i][13].getCouleur()>0){
                                statut =GAMEOVER;
                            }
                        }

                        if(statut!=GAMEOVER){
                            compteur++;
                            nbGemmesSuppr=puits.verification();

                            //SUPPRESSION DE GEMMES
                            if(nbGemmesSuppr>0){
                                statutColone=COMBO;
                                puits.setNumCombo(puits.getNumCombo()+1);
                                //TODO : mettre le changement de score en tampon pour pouvoir l'afficher au dessus en rose
                               // System.out.println("Avant : " + puits.getScoreTampon());
                                puits.setScoreTampon(puits.getScoreTampon()+(puits.getNiveau()+1)*((int)(nbGemmesSuppr/3)+nbGemmesSuppr%3)*VARSCORE);
                                //System.out.println("SCORE TAMPON" + puits.getScoreTampon());
                                int niveau=puits.getNiveau();
                                puits.setNiveau((int)(puits.getGemmesSuppr()/35));

                                if(puits.getNiveau()!=niveau){
                                    System.out.println("levelup");
                                    //TODO : jouer son level up!
                                    jouerSon(NUMLEVEL);
                                }

                                if(puits.getNumCombo()<=5)
                                    this.jouerSon(NUMSUPPR1-1+puits.getNumCombo());
                                else
                                    this.jouerSon(NUMSUPPR5);
                            }else{      //FIN DU TOUR
                                if(compteur>=TEMPSATT){
                                    puits.setScore(puits.getScore()+puits.getScoreTampon());
                                    nbGemmesSuppr=0;
                                    puits.setScoreTampon(0);
                                    //System.out.println("On recommence");
                                    puits.nouvelleColone();
                                    statutColone =CHUTE;
                                    puits.setNumCombo(0);
                                    compteur=TEMPSCHUTE;
                                }
                            }
                        }
                        break;

                    case COMBO:
                        if(puits.animSuppr()==0)
                            statutColone=POSTCOMBO;
                        break;
                    case POSTCOMBO:
                        if(puits.verifChute()==false)
                            statutColone=ATTENTE;
                        break;
                }


                puits.afficher(f);
                if(puits.getNumCombo()==0)
                    puits.affichageCourant(f);
                this.rafraichissement();

                break;
            case GAMEOVER:
		//TODO: classe high score pour le jeu!!!
                break;
        }

        switch(numJoueur){
            case 1:
                this.puits1=puits;
                this.statut1=statut;
                this.statutColone1=statutColone;
                this.compteur1=compteur;
                break;
            case 2:
                this.puits2=puits;
                this.statut2=statut;
                this.statutColone2=statutColone;
                this.compteur2=compteur;
                break;
            default:
                this.puits1=puits;
                this.statut1=statut;
                this.statutColone1=statutColone;
                this.compteur1=compteur;
                break;
        }


    }


    //TODO: FONCTION JOUER SON!!!
    //TODO: si un son doit jouer alors qu'il est en lecture, alors on arrête le précédent pour jouer le nouveau!
    public void arretSon(int i){
        if(this.enLecture[i]){
            if(this.tempsJoue[i]>=this.tempsMax[i]){
                this.enLecture[i]=false;
                this.tempsJoue[i]=0;
                try{
                    switch(i){
                        case NUMMUSIQUE:
                            this.mus.arret();
                            break;
                        case NUMSWITCH:
                            this.sonSwitch.arret();
                            break;
                        case NUMCHUTE:
                            this.sonChute.arret();
                            break;
                        case NUMSUPPR1:
                            this.sonSuppr1.arret();
                            break;
                        case NUMSUPPR2:
                            this.sonSuppr2.arret();
                            break;
                        case NUMSUPPR3:
                            this.sonSuppr3.arret();
                            break;
                        case NUMSUPPR4:
                            this.sonSuppr4.arret();
                            break;
                        case NUMSUPPR5:
                            this.sonSuppr5.arret();
                            break;
                        case NUMSTART:
                            this.sonStart.arret();
                            break;
                        case NUMLEVEL:
                            this.sonLevel.arret();
                            break;
                    }
                }catch (Exception e){System.out.println("Echec de l'arret! "+e.getMessage());}
            }
            this.tempsJoue[i]+=Main.pasTemps;
        }else{

        }
    }

    public void jouerSon(int i){
        if(this.enLecture[i])
            arretSon(i);
        this.enLecture[i]=true;
        this.tempsJoue[i]=0;
        try{
            switch(i){
                case NUMMUSIQUE:
                    this.mus=new Bruitage("sounds/music/Clotho.mp3");
                    this.mus.lecture();
                    break;
                case NUMSWITCH:
                    this.sonSwitch=new Bruitage("sounds/switch.mp3");
                    this.sonSwitch.lecture();
                    break;
                case NUMCHUTE:
                    this.sonChute=new Bruitage("sounds/chute.mp3");
                    this.sonChute.lecture();
                    break;
                case NUMSUPPR1:
                    this.sonSuppr1=new Bruitage("sounds/suppr1.mp3");
                    this.sonSuppr1.lecture();
                    break;
                case NUMSUPPR2:
                    this.sonSuppr2=new Bruitage("sounds/suppr2.mp3");
                    this.sonSuppr2.lecture();
                    break;
                case NUMSUPPR3:
                    this.sonSuppr3=new Bruitage("sounds/suppr3.mp3");
                    this.sonSuppr3.lecture();
                    break;
                case NUMSUPPR4:
                    this.sonSuppr4=new Bruitage("sounds/suppr4.mp3");
                    this.sonSuppr4.lecture();
                    break;
                case NUMSUPPR5:
                    this.sonSuppr5=new Bruitage("sounds/suppr5.mp3");
                    this.sonSuppr5.lecture();
                    break;
                case NUMSTART:
                    this.sonStart=new Bruitage("sounds/start.mp3");
                    this.sonStart.lecture();
                    break;
                case NUMLEVEL:
                    this.sonLevel=new Bruitage("sounds/level.mp3");
                    this.sonLevel.lecture();
                    break;
            }
        }catch (Exception e){System.out.println("Echec de la lecture ! "+e.getMessage());}

    }

    public static void ajouterTex(Dessin tex){
        listeTextures.add(tex);
    }

    public void rafraichissement(){
        this.f.effacer();
        this.f.ajouter(bg);
        for(int i=0;i<listeTextures.size();i++) {
            f.ajouter(listeTextures.get(i));
        }
        this.f.rafraichir();
        listeTextures.clear();
    }


}
