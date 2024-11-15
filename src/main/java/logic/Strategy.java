package logic;

import java.util.ArrayList;
import java.util.List;

import models.Base;
import models.BaseLevel;
import models.GameConfig;
import models.GameState;
import models.PathConfig;
import models.PlayerAction;
import models.Position;

public class Strategy {

  public static final int PLAYER_ID = 1;

  public static List<PlayerAction> decide(GameState gameState) {
    List<Base> ownBases = calculateOwnBases(gameState);
    return decideTakeoverOrUpgrade(gameState, ownBases);
  }

  public static List<PlayerAction> decideTakeoverOrUpgrade(GameState gameState, List<Base> ownBases) {
    ArrayList<PlayerAction> playerActions = new ArrayList<>();
    for (Base base : ownBases) {
      Base nearerstBase = calculateNearerstBase(gameState, base);
      if (nearerstBase == null) {
        continue;
      }
      int upgradeCost = base.unitsUntilUpgrade;
      int takeoverCost = calculateTakeoverBase(gameState.config, base.position, nearerstBase);
      if (upgradeCost < takeoverCost) {
        if (base.population >= upgradeCost) {
          playerActions.add(new PlayerAction(base.uid, base.uid, upgradeCost));
        }
      } else {
        if (base.population >= takeoverCost) {
          playerActions.add(new PlayerAction(base.uid, nearerstBase.uid, base.population));
        }
      }
    }
    return playerActions;
  }

  public static List<Base> calculateOwnBases(GameState gameState) {
    return gameState.bases.stream().filter(base -> base.player == PLAYER_ID).toList();
  }

  public static Base calculateNearerstBase(GameState gameState, Base sourceBase) {
    Base nearestBase = null;
    int minDistance = Integer.MAX_VALUE;
    for (Base base : gameState.bases) {
      if (base.player == PLAYER_ID) {
        continue;
      }
      int distance = calculateDistance(sourceBase.position, base.position);
      if (distance < minDistance) {
        minDistance = distance;
        nearestBase = base;
      }
    }
    return nearestBase;
  }

  /**
   * @return min value to take over the base
   */
  public static int calculateTakeoverBase(GameConfig gameConfig, Position source, Base destinationBase) {
    int travelTimeCost = calculateTravelTimeCost(source, destinationBase.position, gameConfig.paths);
    if (destinationBase.player == 0) {
      return destinationBase.population + travelTimeCost + 1;
    }
    int population = calculatePopulationAfterNRounds(gameConfig, destinationBase, calculateDistance(source, destinationBase.position));
    return population + travelTimeCost + 1;
  }

  public static int calculatePopulationAfterNRounds(GameConfig gameConfig, Base base, int rounds) {
    BaseLevel baseLevel = gameConfig.baseLevels.get(base.level - 1);
    if (base.population == baseLevel.maxPopulation) {
      return base.population;
    }
    int population = base.population + (rounds * baseLevel.spawnRate);
    return Math.min(population, baseLevel.maxPopulation); //TODO: halt durch Vale
  }

  public static int calculateTravelTimeCost(Position source, Position destination, PathConfig pathConfig) {
    int distance = calculateDistance(source, destination);
    if (distance <= pathConfig.gracePeriod) {
      return 0;
    } else {
      return (distance - pathConfig.gracePeriod) * pathConfig.deathRate;
    }
  }

  public static int calculateDistance(Position source, Position destination) {
    int dx = destination.x - source.x;
    int dy = destination.y - source.y;
    int dz = destination.z - source.z;
    return (int) Math.floor(Math.sqrt(dx * dx + dy * dy + dz * dz));
  }

}