package models;

public class PathConfig{

    public int gracePeriod = 0;
    public int deathRate = 0;

    public PathConfig(int gracePeriod, int deathRate){
        this.gracePeriod = gracePeriod;
        this.deathRate = deathRate;
    }
}
