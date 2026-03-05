

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Cette classe implémente les méthodes de KeyListener permettant la gestion du clavier.
 * <br /><br />
 * Elle permet de gérer le clavier dans des applications développées pour la borne d'arcade de l'IUT.<br />
 * 
 * Les joysticks sont nommés joyJ1 et joyJ2. joyJ1Haut la touche envoyée lorsque le joystick 1 est poussé vers le haut, joyJ1Bas, joyJ1Gauche, joyJ1Droite lorsqu'il est poussé, respectivement, vers le bas, la gauche et la droite.<br />
 *
 * Les boutons sont nommés boutonJ1 et boutonJ2. Il y a 6 boutons possibles pour chaque joueur. Ils sont notés A, B et C pour les boutons du bas et X, Y et Z pour les boutons du haut. Ils sont donc notés boutonJ1A, boutonJ1B, boutonJ1C, boutonJ1X, boutonJ1Y et boutonJ1Z.<br />
 *
 * Pour chacune des directions des joysticks ou des boutons, deux méthodes seront présentes : une méthode pour savoir si la direction ou le bouton est pressé ou une autre méthode pour savoir s'il a été pressé.
 *
 * Elle fonctionne sur le principe de booléen que l'on change quand on appuie ou relâche les touches.
 */
public class ClavierBorneArcade implements KeyListener {

    // Attributs //

    private boolean gauche;
    private boolean gaucheTape;
    private boolean droite;
    private boolean droiteTape;
    private boolean haut;
    private boolean hautTape;
    private boolean bas;
    private boolean basTape;

    private boolean a;
    private boolean aTape;
    private boolean z;
    private boolean zTape;
    private boolean e;
    private boolean eTape;
    private boolean q;
    private boolean qTape;
    private boolean s;
    private boolean sTape;
    private boolean d;
    private boolean dTape;
    private boolean b;
    private boolean bTape;
   
    private boolean k;
    private boolean kTape;
    private boolean l;
    private boolean lTape;
    private boolean m;
    private boolean mTape;
    private boolean o;
    private boolean oTape;
    
    private boolean f;
    private boolean fTape;
    private boolean g;
    private boolean gTape;
    private boolean h;
    private boolean hTape;
    private boolean r;
    private boolean rTape;
    private boolean t;
    private boolean tTape;
    private boolean y;
    private boolean yTape;
   

    // Constructeur //

    /**
     * Crée un clavier et initialise tous les attributs à faux pour touches relâchés.
     */
    public ClavierBorneArcade () {

	gauche = gaucheTape = droite = droiteTape = false;
	haut = hautTape = bas = basTape = false;


	a = d = e = k = l = m = o = q = s = z = false;
	aTape = dTape = eTape = kTape = lTape = mTape = oTape = qTape = sTape = zTape = false;
	f = g = h = r = t = y = false;
	fTape = gTape = hTape = rTape = tTape = yTape = false;
	
    }

    // Accesseurs //

    // Getter //

    /**
     * Permet de savoir si la touche "flèche gauche" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "flèche gauche" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ1GaucheEnfoncee () {

	return gauche;
    }

    /**
     * Permet de savoir si la touche "flèche gauche" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "flèche gauche tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ1GaucheTape () {
	boolean aRetourner=gaucheTape;
	gaucheTape=false;
	return aRetourner;
    }

    /**
     * Permet de savoir si la touche "flèche droite" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "flèche droite" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ1DroiteEnfoncee () {

	return droite;
    }

    /**
     * Permet de savoir si la touche "flèche droite" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "flèche droite tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ1DroiteTape () {
	boolean aRetourner=droiteTape;
	droiteTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "flèche haut" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "flèche haut" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ1HautEnfoncee () {

	return haut;
    }

    /**
     * Permet de savoir si la touche "flèche haut" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "flèche haut tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ1HautTape () {
	boolean aRetourner=hautTape;
	hautTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "flèche bas" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "flèche bas" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ1BasEnfoncee () {

	return bas;
    }

    /**
     * Permet de savoir si la touche "flèche bas" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "flèche bas tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ1BasTape () {
	boolean aRetourner=basTape;
	basTape=false;
	return aRetourner;
    }




    /**
     * Permet de savoir si la touche "a" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "a" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ2XEnfoncee () {

	return a;
    }

    /**
     * Permet de savoir si la touche "a" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "a tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ2XTape () {
	boolean aRetourner=aTape;
	aTape=false;
	return aRetourner;
    }



    /**
     * Permet de savoir si la touche "z" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "z" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ2YEnfoncee () {

	return z;
    }

    /**
     * Permet de savoir si la touche "z" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "z tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ2YTape () {
	boolean aRetourner=zTape;
	zTape=false;
	return aRetourner;
    }



    /**
     * Permet de savoir si la touche "e" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "e" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ2ZEnfoncee () {

	return e;
    }

    /**
     * Permet de savoir si la touche "e" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "e tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ2ZTape () {
	boolean aRetourner=eTape;
	eTape=false;
	return aRetourner;
    }



    /**
     * Permet de savoir si la touche "q" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "q" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ2AEnfoncee () {

	return q;
    }

    /**
     * Permet de savoir si la touche "q" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "q tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ2ATape () {
	boolean aRetourner=qTape;
	qTape=false;
	return aRetourner;
    }



    /**
     * Permet de savoir si la touche "s" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "s" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ2BEnfoncee () {

	return s;
    }

    /**
     * Permet de savoir si la touche "s" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "s tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ2BTape () {
	boolean aRetourner=sTape;
	sTape=false;
	return aRetourner;
    }



    /**
     * Permet de savoir si la touche "d" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "d" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ2CEnfoncee () {

	return d;
    }

    /**
     * Permet de savoir si la touche "d" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "d tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ2CTape () {
	boolean aRetourner=dTape;
	dTape=false;
	return aRetourner;
    }

   


    /**
     * Permet de savoir si la touche "k" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "k" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ2GaucheEnfoncee () {
    	
	return k;
    }

    /**
     * Permet de savoir si la touche "k" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "k tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ2GaucheTape () {
	boolean aRetourner=kTape;
	kTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "l" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "l" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ2BasEnfoncee () {

	return l;
    }

    /**
     * Permet de savoir si la touche "l" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "l tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ2BasTape () {
	boolean aRetourner=lTape;
	lTape=false;
	return aRetourner;
    }

    /**
     * Permet de savoir si la touche "m" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "m tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ2DroiteTape () {
	boolean aRetourner=mTape;
	mTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "m" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "m" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ2DroiteEnfoncee () {

	return m;
    }




    /**
     * Permet de savoir si la touche "o" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "o" : vrai pour enfoncée, faux sinon.
     */
    public boolean getJoyJ2HautEnfoncee () {

	return o;
    }

    /**
     * Permet de savoir si la touche "o" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "o tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getJoyJ2HautTape () {
	boolean aRetourner=oTape;
	oTape=false;
	return aRetourner;
    }

   

    /**
     * Permet de savoir si la touche "f" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "f tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ1ATape () {
	boolean aRetourner=fTape;
	fTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "f" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "f" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ1AEnfoncee () {
    	
	return f;
    }

    /**
     * Permet de savoir si la touche "g" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "g tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ1BTape () {
	boolean aRetourner=gTape;
	gTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "g" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "g" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ1BEnfoncee () {
    	
	return g;
    }

    /**
     * Permet de savoir si la touche "h" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "h tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ1CTape () {
	boolean aRetourner=hTape;
	hTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "h" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "h" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ1CEnfoncee () {
    	
	return h;
    }

    /**
     * Permet de savoir si la touche "r" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "r tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ1XTape () {
	boolean aRetourner=rTape;
	rTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "r" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "r" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ1XEnfoncee () {
    	
	return r;
    }

    /**
     * Permet de savoir si la touche "t" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "t tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ1YTape () {
	boolean aRetourner=tTape;
	tTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "t" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "t" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ1YEnfoncee () {
    	
	return t;
    }

    /**
     * Permet de savoir si la touche "y" a été appuyée puis relâchée.
     * @return retourne la valeur du booléen correspondant à la touche "y tapée" : vrai pour tapée, faux sinon.
     */
    public boolean getBoutonJ1ZTape () {
	boolean aRetourner=yTape;
	yTape=false;
	return aRetourner;
    }


    /**
     * Permet de savoir si la touche "y" est enfoncée ou non.
     * @return retourne la valeur du booléen correspondant à la touche "y" : vrai pour enfoncée, faux sinon.
     */
    public boolean getBoutonJ1ZEnfoncee () {
    	
	return y;
    }

      
    

    // Méthodes //

    /**
     * Méthode permettant la reinitialisation du clavier. Reinitialisation de tous les événements.
     */
    public void reinitialisation(){
	gauche = gaucheTape = droite = droiteTape = false;
	haut = hautTape = bas = basTape = false;


	a = d = e = k = l = m = o = q = s = z = false;
	aTape = dTape = eTape = kTape = lTape = mTape = oTape = qTape = sTape = zTape = false;
	f = g = h = r = t = y = false;
	fTape = gTape = hTape = rTape = tTape = yTape = false;
	
    }

    /**
     * Implémentation de la méthode KeyReleased - méthode appelée automatiquement lors d'un événement clavier.
     * <br /><br />
     * Elle permet de mettre à jour les valeurs des attributs en fonction des interactions au clavier.<br />
     * Ici, si une touche est relâchée, l'attribut gérant cette touche est mis à jour.<br />
     * Pour la gestion sur l'appui des touches, voir la méthode keyPressed ( KeyEvent k ).
     * @param key un événement clavier.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html" target="_blank">KeyEvent</a>
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html" target="_blank">KeyListener</a>
     * @see #keyPressed keyPressed ( KeyEvent k )
     */
    @Override
    public void keyReleased ( KeyEvent key ) {

	switch (key.getKeyCode()) {

	case KeyEvent.VK_F:
	    f = false;
	    fTape = true;
	    break;

	case KeyEvent.VK_G:
	    g = false;
	    gTape = true;
	    break;

	case KeyEvent.VK_H:
	    h = false;
	    hTape = true;
	    break;

	case KeyEvent.VK_R:
	    r = false;
	    rTape = true;
	    break;

	case KeyEvent.VK_T:
	    t = false;
	    tTape = true;
	    break;

	case KeyEvent.VK_Y:
	    y = false;
	    yTape = true;
	    break;

	case KeyEvent.VK_LEFT:
	    gauche = false;
	    gaucheTape = true;
	    break;

	case KeyEvent.VK_RIGHT:
	    droite = false;
	    droiteTape = true;
	    break;

	case KeyEvent.VK_UP:
	    haut = false;
	    hautTape = true;
	    break;

	case KeyEvent.VK_DOWN:
	    bas = false;
	    basTape = true;
	    break;


	case KeyEvent.VK_A:
	    a = false;
	    aTape = true;
	    break;


	case KeyEvent.VK_D:
	    d = false;
	    dTape = true;
	    break;

	case KeyEvent.VK_E:
	    e = false;
	    eTape = true;
	    break;

	

	case KeyEvent.VK_K:
	    k = false;
	    kTape = true;
	    break;

	case KeyEvent.VK_L:
	    l = false;
	    lTape = true;
	    break;

	case KeyEvent.VK_M:
	    m = false;
	    mTape = true;
	    break;



	case KeyEvent.VK_O:
	    o = false;
	    oTape = true;
	    break;



	case KeyEvent.VK_Q:
	    q = false;
	    qTape = true;
	    break;



	case KeyEvent.VK_S:
	    s = false;
	    sTape = true;
	    break;



	case KeyEvent.VK_Z:
	    z = false;
	    zTape = true;
	    break;
	}
    }

    /**
     * Implémentation de la méthode KeyTyped - méthode appelee automatiquement lors d'un événement clavier.
     * <br /><br />
     * Cette méthode doit etre implémentee mais est inutile dans ce moteur.
     */
    @Override
    public void keyTyped ( KeyEvent k ) {
    }

    /**
     * Implémentation de la méthode KeyPressed - méthode appelee automatiquement lors d'un événement clavier.
     * <br /><br />
     * Elle permet de mettre a jour les valeurs des attributs en fonction des interactions au clavier.<br />
     * Ici, si une touche est pressee, l'attribut gerant cette touche est mis a jour.<br />
     * Pour la gestion du relachement des touches, voir la méthode keyReleased ( KeyEvent k ).
     * @param key un événement clavier.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html" target="_blank">KeyEvent</a>
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html" target="_blank">KeyListener</a>
     * @see #keyReleased keyReleased ( KeyEvent k )
     */
    @Override
    public void keyPressed(KeyEvent key) {

	switch (key.getKeyCode()) {

	case KeyEvent.VK_F:
	    f = true;
	    break;

	case KeyEvent.VK_G:
	    g = true;
	    break;

	case KeyEvent.VK_H:
	    h = true;
	    break;

	case KeyEvent.VK_R:
	    r = true;
	    break;

	case KeyEvent.VK_T:
	    t = true;
	    break;

	case KeyEvent.VK_Y:
	    y = true;
	    break;



	case KeyEvent.VK_LEFT:
	    gauche = true;
	    break;

	case KeyEvent.VK_RIGHT:
	    droite = true;
	    break;

	case KeyEvent.VK_UP:
	    haut = true;
	    break;

	case KeyEvent.VK_DOWN:
	    bas = true;
	    break;



	case KeyEvent.VK_A:
	    a = true;
	    break;



	case KeyEvent.VK_D:
	    d = true;
	    break;

	case KeyEvent.VK_E:
	    e = true;
	    break;



	case KeyEvent.VK_K:
	    k = true;
	    break;

	case KeyEvent.VK_L:
	    l = true;
	    break;

	case KeyEvent.VK_M:
	    m = true;
	    break;



	case KeyEvent.VK_O:
	    o = true;
	    break;



	case KeyEvent.VK_Q:
	    q = true;
	    break;

	

	case KeyEvent.VK_S:
	    s = true;
	    break;

	    

	case KeyEvent.VK_Z:
	    z = true;
	    break;
	}
    }
}
