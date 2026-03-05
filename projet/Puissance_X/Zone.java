class Zone {
	public final int x, y, longueur, largeur;
	public final Point milieu;

	//
	// CONSTRUCTEURS
	//

	public Zone(int x, int y, int longueur, int largeur) {
		this.x = x;
		this.y = y;
		this.longueur = longueur;
		this.largeur = largeur;
		this.milieu = new Point(longueur / 2 + x, largeur / 2 + y);
	}
}