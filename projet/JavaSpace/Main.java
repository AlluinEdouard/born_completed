import java.util.Date;


class Main{


    public static void main(String[] args){
	Jeu javaSpace = new Jeu(); // Déclaration et initialisation du jeu
	long debut, fin, diff=0;
	long tpsAttente=20;
	int status = 0;
	int finJeu = 0;
	
	//String str="";

	while(finJeu != 1) {
		
	
	
	while(status!=1) {
		javaSpace.majMenu();
		status=javaSpace.getStatus();
		try{
			Thread.sleep(tpsAttente); // Réglage pour avoir un déplacement avec le clavier fluide, et une image fluide
	    }		
		catch(Exception e) {		
			System.out.println(e);
	    }
	}
	
	while(javaSpace.fin()==false){ // Boucle vraie tant que la vie du joueur est diffférente de zéro
	    try{
		Thread.sleep(tpsAttente-diff); // Réglage pour avoir un déplacement avec le clavier fluide, et une image fluide
	    }		
	    catch(Exception e) {		
		System.out.println(e);
	    }
	    debut = new Date().getTime();
	    javaSpace.avancerUnPasDeTemps(); // Méthode qui gère le jeu
	    fin = new Date().getTime();
	    diff=fin-debut;
	    /*if((tpsAttente-diff)>=0)
	      str=str+" - "+(tpsAttente-diff);
	      else
	      str=str+" - ***"+(tpsAttente-diff);
	    */
	    if(diff>tpsAttente)
			diff=tpsAttente;
	  
	}
	status = javaSpace.getStatus();
	//System.out.println(str);
	}
    }
}
