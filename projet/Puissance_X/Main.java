import MG2D.*;

class Main {
    // static public final int LONGUEUR_FENETRE = 1280;
    // static public final int LARGEUR_FENETRE = 900;
    static public final int FPS = 60;
    static public final int TEMPS_ATTENTE_MAX = 2;
    static public final int NB_IMAGE_RETARD_MAX = 20;

    static long periode = 1000000000 / FPS;

    public static void main(String[] args) {
	GestionAffichage affichage = new GestionAffichage("Puissance X"); //, LONGUEUR_FENETRE, LARGEUR_FENETRE
	affichage.nouvellePartie();

	long dateDebut, dateFin, aAttendre = -1, aRattraper = 0;
	while (!affichage.pretAQuitter()) {
	    dateDebut = System.nanoTime();

	    affichage.mettreAJour();
	    if (aAttendre != 0)
		affichage.rafraichir();

	    dateFin = System.nanoTime();

	    aAttendre = periode - (dateFin - dateDebut);
	    if (aAttendre < 0) // Trop de lag (l'image met plus que temps à se dessiner que la période à respecter pour synchroniser les FPS).
		{
		    aRattraper -= (aRattraper < (periode * NB_IMAGE_RETARD_MAX) ? aAttendre : 0);
		    aAttendre = 0;
		}
	    else if ((aRattraper -= aAttendre) < 0)
		aRattraper = 0;
	    
	    if ((aAttendre -= aRattraper) < 0)
		aAttendre = 0;
	    
	    // System.out.println(aAttendre + " - " + aRattraper);
	    
	    
	    try
		{
		    Thread.sleep((int)(aAttendre / 1000000), (int)(aAttendre % 1000000));
		}
	    catch (Exception e) {
		System.out.println(e);
	    }
	}
    }

    public static void setFPS(int fps) {
	if (fps > 1)
	    periode = 1000000000 / fps;
    }
}
