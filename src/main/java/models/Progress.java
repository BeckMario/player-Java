package models;

public class Progress{

    public int distance = 0;
    public int traveled = 0;

    public Progress(int distance, int traveled){
        this.distance = distance;
        this.traveled = traveled;
    }

    public int getDistanceLeft(){
        return distance - traveled;
    }
}
