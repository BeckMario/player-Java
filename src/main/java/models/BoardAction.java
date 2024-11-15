package models;

import java.util.UUID;

public class BoardAction{

    public int src = 0;
    public int dest = 0;
    public int amount = 0;
    public UUID uuid = new UUID(0, 0);
    public int player = 0;
    public Progress progress = new Progress(0, 0);

    public BoardAction(int src, int dest, int amount, UUID uuid, int player, Progress progress){
        this.src = src;
        this.dest = dest;
        this.amount = amount;
        this.uuid = uuid;
        this.player = player;
        this.progress = progress;
    }
}
