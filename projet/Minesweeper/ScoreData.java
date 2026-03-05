public class ScoreData {
    private String name;
    private int score; // This is the score time convert into seconde :)

    public ScoreData(String dataName, int dataScore) {
        name = new String(dataName);
        score = Math.max(dataScore, 0);
    }

    public ScoreData(String scoreData) {
        String[] tab = scoreData.split("-");
        if (tab.length != 2) {
            name = "AAA";
            score = 0;
        } else {
            name = tab[0];
            String[] time = tab[1].split(":");
            if (time.length == 2) {
                int minutes = Integer.parseInt(time[0]);
                int seconds = Integer.parseInt(time[1]);
                score = minutes * 60 + seconds;
            } else {
                score = Integer.parseInt(tab[1]);
            }
        }
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String toFormattedString() {
        int minutes = score / 60;
        int seconds = score % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return name + "-" + toFormattedString(); // pour sauvegarder avec format 00:00
    }

}
