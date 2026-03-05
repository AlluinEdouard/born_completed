public interface Theme {

    public String getBomb();
    public String getFlag();
    public String getFlagTrue();
    public String getDig();
    public String getDigTrue();
    public String getTileMasked();
    public String getTileDiscovered(int nbNeighbours);
    public String getQuit();
    public String getRestart();
    public String getBackground();
    public String getWin();
    public String getLose();
    public String toString();
    public String getLevelEasy();
    public String getLevelMedium();
    public String getLevelHard();
    public void display();
}
