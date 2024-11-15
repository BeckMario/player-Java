package models;

public class BaseLevel {

    public int maxPopulation = 0;
    public int upgradeCost = 0;
    public int spawnRate = 0;

    public BaseLevel(int maxPopulation, int upgradeCost, int spawnRate){
        this.maxPopulation = maxPopulation;
        this.upgradeCost = upgradeCost;
        this.spawnRate = spawnRate;
    }
}
