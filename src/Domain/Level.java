package Domain;

public class Level {

    private int levelID;
    private String level;

    public Level(int levelID, String level) {
        this.levelID = levelID;
        this.level = level;
    }

    public int getLevelID() {
        return levelID;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString(){
        return level;
    }

}
