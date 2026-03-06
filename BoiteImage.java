import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texture;
import java.io.File;


public class BoiteImage extends Boite{

    Texture image;
    private static final String DEFAULT_IMAGE = "img/bouton2.png";
    private static final double REMPLISSAGE_MAX = 0.64;

    private static String resolveImagePath(String basePath){
	String candidate = basePath + "/photo_small.png";
	File f = new File(candidate);
	if(f.isFile()){
	    return candidate;
	}
	String fallbackGameImage = basePath + "/img/image.png";
	File gameImage = new File(fallbackGameImage);
	if(gameImage.isFile()){
	    return fallbackGameImage;
	}
	System.err.println("Image manquante: " + candidate + " / " + fallbackGameImage + " (placeholder utilisé)");
	return DEFAULT_IMAGE;
    }

    BoiteImage(Rectangle rectangle, String image) {
	super(rectangle);
	this.image = new Texture(resolveImagePath(image), new Point(0, 0));
	adapterImageDansBoite();
    }

    public Texture getImage() {
	return this.image;
    }

    public void setImage(String chemin) {
	this.image.setImg(resolveImagePath(chemin));
	adapterImageDansBoite();
    }

    private void adapterImageDansBoite(){
	int largeurMax = Math.max(1, (int)Math.round(this.getRectangle().getLargeur() * REMPLISSAGE_MAX));
	int hauteurMax = Math.max(1, (int)Math.round(this.getRectangle().getHauteur() * REMPLISSAGE_MAX));
	int largeurOrigine = this.image.getImg().getWidth();
	int hauteurOrigine = this.image.getImg().getHeight();

	double ratioBoite = Math.min((double)largeurMax / largeurOrigine, (double)hauteurMax / hauteurOrigine);
	double ratio = Math.min(1.0, ratioBoite);
	int largeurFinale = Math.max(1, (int)Math.round(largeurOrigine * ratio));
	int hauteurFinale = Math.max(1, (int)Math.round(hauteurOrigine * ratio));

	int x = this.getRectangle().getA().getX() + (this.getRectangle().getLargeur() - largeurFinale) / 2;
	int y = this.getRectangle().getA().getY() + (this.getRectangle().getHauteur() - hauteurFinale) / 2;
	this.image.setA(new Point(x, y));
	this.image.setTaille(largeurFinale, hauteurFinale);
    }

}
