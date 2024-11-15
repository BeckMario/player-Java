package models;

public class Game{

    public int uid = 0;
    public int tick = 0;
    public int playerCount = 0;
    public int remainingPlayers = 0;
    public int player = 0;

    public Game(int uid, int tick, int playerCount, int remainingPlayers, int player){
        this.uid = uid;
        this.tick = tick;
        this.playerCount = playerCount;
        this.remainingPlayers = remainingPlayers;
        this.player = player;
    }
}
