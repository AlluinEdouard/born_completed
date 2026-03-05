import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Entree extends KeyAdapter {
    static public final int INTERVAL_REPETITION = 100;

    private Hashtable<Integer, Long> derniereRecupTouche;

    public Entree() {
	derniereRecupTouche = new Hashtable<>();
    }

    private long maintenant() {
	return System.currentTimeMillis();
    }

    private boolean verifTouche(int touche, int intervalRepetition) {
	Integer toucheInteger = Integer.valueOf(touche);
	synchronized (derniereRecupTouche)
	    {
		if (derniereRecupTouche.containsKey(toucheInteger))
		    {
			long derniereRecup = derniereRecupTouche.get(toucheInteger), maintenant = this.maintenant();
			if (maintenant - derniereRecup >= intervalRepetition)
			    {
				derniereRecupTouche.put(toucheInteger, Long.valueOf(maintenant));
				return true;
			    }
		    }
	    }
	return false;
    }

    public boolean echap(int intervalRepetition) {
	return this.verifTouche(KeyEvent.VK_R, intervalRepetition) || this.verifTouche(KeyEvent.VK_A, intervalRepetition);
	//return this.verifTouche(KeyEvent.VK_F, intervalRepetition) || this.verifTouche(KeyEvent.VK_Q, intervalRepetition);
    }
    public boolean entree(int intervalRepetition) {
	return this.verifTouche(KeyEvent.VK_F, intervalRepetition) || this.verifTouche(KeyEvent.VK_Q, intervalRepetition);
	//return this.verifTouche(KeyEvent.VK_R, intervalRepetition) || this.verifTouche(KeyEvent.VK_A, intervalRepetition);
    }

    public boolean droite(int intervalRepetition) {
	return this.verifTouche(KeyEvent.VK_RIGHT, intervalRepetition) || this.verifTouche(KeyEvent.VK_M, intervalRepetition);
    }
    public boolean gauche(int intervalRepetition) {
	return this.verifTouche(KeyEvent.VK_LEFT, intervalRepetition) || this.verifTouche(KeyEvent.VK_K, intervalRepetition);
    }
    public boolean haut(int intervalRepetition) {
	return this.verifTouche(KeyEvent.VK_UP, intervalRepetition) || this.verifTouche(KeyEvent.VK_O, intervalRepetition);
    }
    public boolean bas(int intervalRepetition) {
	return this.verifTouche(KeyEvent.VK_DOWN, intervalRepetition) || this.verifTouche(KeyEvent.VK_L, intervalRepetition);
    }

    @Override
    public void keyPressed(KeyEvent e) {
	Integer touche = Integer.valueOf(e.getKeyCode());
	synchronized (derniereRecupTouche)
	    {
		if (!derniereRecupTouche.containsKey(touche))
		    derniereRecupTouche.put(touche, Long.valueOf(0));
	    }
    }
    @Override
    public void keyReleased(KeyEvent e) {
	Integer touche = Integer.valueOf(e.getKeyCode());
	synchronized (derniereRecupTouche)
	    {
		if (derniereRecupTouche.containsKey(touche))
		    derniereRecupTouche.remove(touche);
	    }
    }
}
