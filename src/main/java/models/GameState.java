package models;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    public List<BoardAction> actions = new ArrayList<>();
    public List<Base> bases = new ArrayList<>();
    public GameConfig config = new GameConfig(new ArrayList<BaseLevel>(), new PathConfig(0, 0));
    public Game game = new Game(0, 0, 0, 0, 0);

    public GameState(List<BoardAction> actions, List<Base> bases, GameConfig config, Game game){
        this.actions = actions;
        this.bases = bases;
        this.config = config;
        this.game = game;
    }
}
