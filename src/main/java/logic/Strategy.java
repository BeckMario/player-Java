package logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Strategy {

  private static final List<BaseLevel> LEVELS;

  // TEMP until base levels in game state is fixed
  static {
    List<BaseLevel> levels = new ArrayList<>();
    levels.add(new BaseLevel(20, 10, 1));   // Level 0
    levels.add(new BaseLevel(40, 20, 2));   // Level 1
    levels.add(new BaseLevel(80, 30, 3));   // Level 2
    levels.add(new BaseLevel(100, 40, 4));  // Level 3
    levels.add(new BaseLevel(200, 50, 5));  // Level 4
    levels.add(new BaseLevel(300, 100, 6)); // Level 5
    levels.add(new BaseLevel(400, 200, 7)); // Level 6
    levels.add(new BaseLevel(500, 400, 8)); // Level 7
    levels.add(new BaseLevel(600, 600, 9)); // Level 8
    levels.add(new BaseLevel(700, 800, 10));// Level 9
    levels.add(new BaseLevel(800, 1000, 15));// Level 10
    levels.add(new BaseLevel(900, 1500, 20));// Level 11
    levels.add(new BaseLevel(1000, 2000, 25));// Level 12
    levels.add(new BaseLevel(2000, 3000, 50));// Level 13

    LEVELS = Collections.unmodifiableList(levels);
  }

  public static void printAsJson(Object object) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      System.out.println(objectMapper.writeValueAsString(object));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<PlayerAction> decide(GameState gameState) {
    printAsJson(gameState);
    List<Base> ownBases = calculateOwnBases(gameState);
    List<PlayerAction> playerActions = decideTakeoverOrUpgrade(gameState, ownBases);
    for (PlayerAction playerAction : playerActions) {
      printAsJson(playerAction);
    }
    return playerActions;
  }

  public static List<PlayerAction> decideTakeoverOrUpgrade(GameState gameState, List<Base> ownBases) {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<PlayerAction>> futures = ownBases.stream()
          .map(base -> executor.submit(() -> decideForBase(gameState, base)))
          .toList();

      return futures.stream()
          .map(future -> {
            try {
              return future.get();
            } catch (Exception e) {
              e.printStackTrace();
              return null;
            }
          })
          .filter(Objects::nonNull)
          .toList();
    }
  }

  private static PlayerAction decideForBase(GameState gameState, Base base) {
    Base nearerstBase = calculateNearerstBase(gameState, base);
    if (nearerstBase == null) {
      return null;
    }
//    List<BaseLevel> baseLevels = gameState.config.baseLevels;
//    if (baseLevels == null || base.level >= baseLevels.size()) { // if base levels is not usable dont upgrade just expand
//      int travelTimeCost = calculateTravelTimeCost(base.position, nearerstBase.position, gameState.config.paths);
//
//      if (base.population >= travelTimeCost) {
//        return new PlayerAction(base.uid, nearerstBase.uid, base.population);
//      }
//      return null;
//    }

    BaseLevel baseLevel = LEVELS.get(base.level);//baseLevels.get(base.level);
    System.out.println("Current Decision: " + base + " -> " + nearerstBase);
    int upgradeCost = base.unitsUntilUpgrade != 0 ? base.unitsUntilUpgrade : baseLevel.upgradeCost;
    int takeoverCost = calculateTakeoverBase(gameState.config, base.position, nearerstBase);
    System.out.println("Upgrade Cost: " + upgradeCost + ", Takeover Cost: " + takeoverCost + ", Population: " + base.population);
    if (upgradeCost < takeoverCost) {
      if (base.population >= upgradeCost) {
        return new PlayerAction(base.uid, base.uid, upgradeCost);
      }
    } else {
      if (base.population >= takeoverCost) {
        return new PlayerAction(base.uid, nearerstBase.uid, base.population);
      }
    }
    return null;
  }

  public static List<Base> calculateOwnBases(GameState gameState) {
    return gameState.bases.stream().filter(base -> base.player == gameState.game.player).toList();
  }

  public static Base calculateNearerstBase(GameState gameState, Base sourceBase) {
    Base nearestBase = null;
    int minDistance = Integer.MAX_VALUE;
    for (Base base : gameState.bases) {
      if (base.player == gameState.game.player) {
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
    BaseLevel baseLevel = LEVELS.get(base.level);// gameConfig.baseLevels.get(base.level);
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