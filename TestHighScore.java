import MG2D.*;
import MG2D.geometrie.*;

class TestHighScore{

    public static void main(String[] args){
	Fenetre f = new Fenetre("test",1280,1024);
	ClavierBorneArcade clavier = new ClavierBorneArcade();
	f.addKeyListener(clavier);

	HighScore.demanderEnregistrerNom(f,clavier,new Texture("img/shoot.png",new Point(0,0)),40000,"./fichierTestHighScore/text5.hig");
    }
    
}
