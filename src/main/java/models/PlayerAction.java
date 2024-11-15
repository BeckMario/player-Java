package models;

public class PlayerAction {

  // These values are public for the jackson serializer
  public int src = 0;
  public int dest = 0;
  public int amount = 0;

  public PlayerAction(int src, int dest, int amount) {
    this.src = src;
    this.dest = dest;
    this.amount = amount;
  }

  @Override public String toString() {
    return "PlayerAction{" +
        "src=" + src +
        ", dest=" + dest +
        ", amount=" + amount +
        '}';
  }
}
