package models;

public class Base{

    public Position position = new Position(0, 0, 0);
    public int uid = 0;
    public int player = 0;
    public int population = 0;
    public int level = 0;
    public int unitsUntilUpgrade = 0;
    
    public Base(Position position, int uid, int player, int population, int level, int unitsUntilUpgrade){
        this.position = position;
        this.uid = uid;
        this.player = player;
        this.population = population;
        this.level = level;
        this.unitsUntilUpgrade = unitsUntilUpgrade;
    }
}
