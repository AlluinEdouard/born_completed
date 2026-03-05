import MG2D.Fenetre;
import MG2D.Couleur;
import MG2D.geometrie.*;
import MG2D.geometrie.Point;

import java.awt.Font;
import java.io.InputStream;

public class Menu {
    //CONSTANTES
    public static final int STATUTMENU=0;
    public final static int BOUTON1JOUEUR=1;
    public final static int BOUTON2JOUEURS=2;
    public final static int BOUTONEXIT=3;

    //ATTRIBUTS
    private int choix;

    //Sert à définir dans quel sous-menu on se trouve
    //0 => Menu principal
    //1 => (Pas encore implémenté) Choix de la musique / difficulté
    private int statut;

    private Fenetre f;

    private Texture bg;
    private Texture title;


    //ELEMENTS DU MENU
    private Rectangle fondTexte;
    private Rectangle contourFondTexte;
    private int largeurFondTexte=300;
    private int hauteurFondTexte=225;
    private int margeFondTexte=10;
    private Texte texte1P;
    private Texte texte2P;
    private Texte texteExit;
    private int yTexte1P;
    private int yTexte2P;
    private int yTexteExit;
    private int espaceElemMenu;

    private Font font;



    public Menu(Fenetre f){
        this.f=f;
        this.choix=this.BOUTON1JOUEUR;
        this.statut=this.STATUTMENU;

        //Sert à définir un fond d'écran aléatoire parmi les deux du jeu d'origine!
        //TODO : Faire alterner les fonds d'écran à coup sûr, comme dans le vrai jeu (?)
        //TODO : Faire jaillir des gemmes de la bourse du premier fond d'écran (fresque)
        if((int)(Math.random()*2)==0)
            this.bg=new Texture("img/menu/bg1.png",new Point(0,0));
        else
            this.bg=new Texture("img/menu/bg2.png",new Point(0,0));
        f.ajouter(bg);

        this.title=new Texture("img/menu/title_big.png",new Point());
        this.title.setA(new Point(f.getMilieu().getX()-this.title.getLargeur()/2,650));
        f.ajouter(title);

        this.contourFondTexte = new Rectangle(Couleur.BLANC,new Point(f.getMilieu().getX()-(largeurFondTexte+margeFondTexte)/2,120-margeFondTexte/2),largeurFondTexte+margeFondTexte,hauteurFondTexte+margeFondTexte,true);
        this.fondTexte = new Rectangle(Couleur.NOIR,new Point(f.getMilieu().getX()-largeurFondTexte/2,120),largeurFondTexte,hauteurFondTexte,true);
        f.ajouter(contourFondTexte);
        f.ajouter(fondTexte);

        this.font = null;
        try{
            String nomFont = "fonts/Norse-Bold.ttf";
            InputStream is = this.getClass().getResourceAsStream(nomFont);
            this.font = this.font.createFont(Font.TRUETYPE_FONT, is);
            this.font = this.font.deriveFont(69.0f);
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }

        this.espaceElemMenu=75;
        this.yTexte1P=300;
        this.texte1P = new Texte("1 PLAYER",this.font,new Point(this.f.getMilieu().getX(),this.yTexte1P));
        this.texte1P.setCouleur(Couleur.ROSE);
        this.f.ajouter(this.texte1P);

        this.yTexte2P=yTexte1P-espaceElemMenu;
        this.texte2P = new Texte("2 PLAYERS",this.font,new Point(this.f.getMilieu().getX(),this.yTexte2P));
        this.texte2P.setCouleur(Couleur.JAUNE);
        this.f.ajouter(this.texte2P);

        this.yTexteExit=yTexte2P-espaceElemMenu;
        this.texteExit = new Texte("EXIT",this.font,new Point(this.f.getMilieu().getX(),this.yTexteExit));
        this.texteExit.setCouleur(Couleur.JAUNE);
        this.f.ajouter(this.texteExit);

        this.f.rafraichir();
    }

    public int prochaineFrame(int direction, int boutonEnfonce){
        int res=this.STATUTMENU;

        //Gestion de la direction enfoncée
        if(direction!=0){
            switch(direction){
                case 1:
                    if(choix>this.BOUTON1JOUEUR)
                        choix--;
                    break;
                case -1:
                    if(choix<this.BOUTONEXIT)
                        choix++;
                    break;
            }

            switch(choix){
                case 1:
                    this.texte1P.setCouleur(Couleur.ROSE);
                    this.texte2P.setCouleur(Couleur.JAUNE);
                    this.texteExit.setCouleur(Couleur.JAUNE);
                    break;
                case 2:
                    this.texte1P.setCouleur(Couleur.JAUNE);
                    this.texte2P.setCouleur(Couleur.ROSE);
                    this.texteExit.setCouleur(Couleur.JAUNE);
                    break;

                case 3:
                    this.texte1P.setCouleur(Couleur.JAUNE);
                    this.texte2P.setCouleur(Couleur.JAUNE);
                    this.texteExit.setCouleur(Couleur.ROSE);
                    break;
            }
        }

        //Si le bouton d'action est enfoncé, on envoie au menu l'information du bouton enfoncé
        if(boutonEnfonce==1){
            res=choix;
        }

        f.rafraichir();
        return res;
    }

}
