import MG2D.Clavier;
import MG2D.FenetrePleinEcran;

public class Main {
    static final int pasTemps=60;
    static final int TAILLEX=1280;
    static final int TAILLEY=1024;


    public static void main(String args[]){
        // Fenetre f = new Fenetre("Columns",TAILLEX,TAILLEY);
        FenetrePleinEcran f = new FenetrePleinEcran("Columns");
        f.setAffichageNbPrimitives(true);
        //f.setAffichageFPS(true);
        f.setVisible(true);

        ClavierBorneArcade clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);

        boolean fermetureJeu=false;

        int statut=Menu.STATUTMENU;

        Menu m = new Menu(f);
        Partie p = new Partie();

        int direction1=Controles.NULL;
        int boutonEnfonce1=Controles.NULL;
        int direction2=Controles.NULL;
        int boutonEnfonce2=Controles.NULL;

        while(!fermetureJeu){
            direction1=Controles.NULL;
            if(clavier.getJoyJ1HautTape())
                direction1=Controles.HAUT;
            if(clavier.getJoyJ1BasTape())
                direction1=Controles.BAS;
            if(clavier.getJoyJ1DroiteTape())
                direction1=Controles.DROITE;
            if(clavier.getJoyJ1GaucheTape())
                direction1=Controles.GAUCHE;

            direction2=Controles.NULL;
            if(clavier.getJoyJ2HautTape())
                direction2=Controles.HAUT;
            if(clavier.getJoyJ2BasTape())
                direction2=Controles.BAS;
            if(clavier.getJoyJ2DroiteTape())
                direction2=Controles.DROITE;
            if(clavier.getJoyJ2GaucheTape())
                direction2=Controles.GAUCHE;

            boutonEnfonce1=Controles.NULL;
            if(clavier.getBoutonJ1ATape())
                boutonEnfonce1=Controles.ACTION;
            if(clavier.getBoutonJ1ZTape()) {
                boutonEnfonce1=Controles.QUITTER;
                statut=3;
            }

            boutonEnfonce2=Controles.NULL;
            if(clavier.getBoutonJ2ATape())
                boutonEnfonce2=Controles.ACTION;
            if(clavier.getBoutonJ2ZTape()) {
                boutonEnfonce2=Controles.QUITTER;
                statut=3;
            }

            switch(statut){
                case Menu.STATUTMENU:
                    //System.out.println("Statut actuel : 0 => MENU");
                    statut = m.prochaineFrame(direction1,boutonEnfonce1);
                    if(statut==Menu.BOUTON1JOUEUR ||statut==Menu.BOUTON2JOUEURS) {
                        f.effacer();
                        p = new Partie(f,statut);
                    }
                    break;
                case Menu.BOUTON1JOUEUR:
                    if(clavier.getJoyJ1BasEnfoncee())
                        direction1=Controles.BAS;
                    //System.out.println("Statut actuel : 1 => PARTIE SOLO EN COURS");
                    if(p.prochaineFrame(direction1,boutonEnfonce1,direction2,boutonEnfonce2)==true) {
                        statut = Menu.STATUTMENU;
                        m = new Menu(f);
                    }
                    break;
                case Menu.BOUTON2JOUEURS:
                    if(clavier.getJoyJ1BasEnfoncee())
                        direction1=Controles.BAS;
                    if(clavier.getJoyJ2BasEnfoncee())
                        direction2=Controles.BAS;
                    //System.out.println("Statut actuel : 2 => PARTIE MULTI EN COURS");
                    //statut=0;
                    if(p.prochaineFrame(direction1,boutonEnfonce1,direction2,boutonEnfonce2)==true) {
                        statut = Menu.STATUTMENU;
                        m = new Menu(f);
                    }
                    break;
                case Menu.BOUTONEXIT:
                    System.out.println("Fermeture!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Statut de la partie inconnu! Statut : "+statut);
                    System.exit(1);
            }
            try{
                Thread.sleep(pasTemps);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

    }
}
