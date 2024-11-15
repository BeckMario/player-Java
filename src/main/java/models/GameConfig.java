package models;

import java.util.ArrayList;
import java.util.List;

public class GameConfig{

    public List<BaseLevel> baseLevels =  new ArrayList<>();
    public PathConfig paths = new PathConfig(0,0);

    public GameConfig(List<BaseLevel> baseLevels, PathConfig paths){
        this.baseLevels = baseLevels;
        this.paths = paths;
    }
}
