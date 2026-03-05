import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texture;
import java.io.File;


public class BoiteImage extends Boite{

    Texture image;
    private static final String DEFAULT_IMAGE = "img/bouton2.png";

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
	this.image = new Texture(resolveImagePath(image), new Point(760, 648));
    }

    public Texture getImage() {
	return this.image;
    }

    public void setImage(String chemin) {
	this.image.setImg(resolveImagePath(chemin));
	//this.image.setTaille(400, 320);
    }

}
