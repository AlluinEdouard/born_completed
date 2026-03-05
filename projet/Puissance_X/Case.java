class Case {

	private int contenu;
	private boolean gagne;

	public Case() {
		this(0);
	}
	public Case(int contenu) {
		this.contenu = contenu;
		this.gagne = false;
	}

	public int getContenu() {
		return this.contenu;
	}
	public void setContenu(int contenu) {
		this.contenu = contenu;
	}

	public boolean estGagante() {
		return this.gagne;
	}
	public void setGagnante() {
		this.gagne = true;
	}

	@Override
	public String toString() {
		return (contenu == 0 ? " " : Integer.toString(contenu));
	}
}
