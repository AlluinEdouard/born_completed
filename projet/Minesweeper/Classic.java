public class Classic implements Theme {

    public Classic() {
    }

    @Override
    public String getBomb() {
        return this.toString() + "/Minesweeper_bomb.png";
    }

    @Override
    public String getFlag() {
        return this.toString() + "/Minesweeper_flag.png";
    }

    @Override
    public String getFlagTrue() {
        return this.toString() + "/Minesweeper_flag.png";
    }

    @Override
    public String getDig() {
        return this.toString() + "/Minesweeper_questionmark.png";
    }

    @Override
    public String getDigTrue() {
        return this.toString() + "/Minesweeper_questionmark.png";
    }
    
    @Override
    public String getTileMasked() {
        return this.toString() + "/Minesweeper_unopened_square.png";
    }

    @Override
    public String getTileDiscovered(int nbNeighbours) {
        return this.toString() + "/Minesweeper_" + nbNeighbours + ".png";
    }

    @Override
    public String getQuit() {
        return this.toString() + "/Minesweeper_cross.png";
    }

    @Override
    public String getRestart() {
        return this.toString() + "/Minesweeper_arrow.png";
    }

    @Override
    public String getBackground() {
        // return this.toString() + "/Minesweeper_background.png";
        return "assets/classic/Minesweeper_background.png";
    }

    @Override
    public String getWin() {
        return this.toString() + "/Minesweeper_win.png";
    }

    @Override
    public String getLose() {
        return this.toString() + "/Minesweeper_lose.png";
    }


    @Override
    public String toString() {
        return "classic";
    }

    @Override
    public void display() {
        System.out.println("Classic theme selected.");
    }

    @Override
    public String getLevelEasy() {
        return this.toString() + "/Level_easy.png";
    }

    @Override
    public String getLevelMedium() {
        return this.toString() + "/Level_medium.png";
    }

    @Override
    public String getLevelHard() {
        return this.toString() + "/Level_hard.png";
    }
    
}
