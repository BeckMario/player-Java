package logic;

import models.PathConfig;
import models.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrategyTest {
  @Test
  void shouldCalculateDistanceCorrectlyFromOriginToPositiveCoordinate() {
    Strategy strategy = new Strategy();
    Position source = new Position(0, 0, 0);
    Position destination = new Position(3, 4, 0);
    int expectedDistance = 5;

    int actualDistance = strategy.calculateDistance(source, destination);

    assertEquals(expectedDistance, actualDistance);
  }

  @Test
  void shouldCalculateDistanceCorrectlyFromOriginToNegativeCoordinate() {
    Strategy strategy = new Strategy();
    Position source = new Position(0, 0, 0);
    Position destination = new Position(-3, -4, 0);
    int expectedDistance = 5;

    int actualDistance = strategy.calculateDistance(source, destination);

    assertEquals(expectedDistance, actualDistance);
  }

  @Test
  void shouldCalculateDistanceCorrectlyFromOriginTo111() {
    Strategy strategy = new Strategy();
    Position source = new Position(0, 0, 0);
    Position destination = new Position(1, 1, 1);
    int expectedDistance = 1;

    int actualDistance = strategy.calculateDistance(source, destination);

    assertEquals(expectedDistance, actualDistance);
  }

  @Test
  void shouldReturnZeroWhenDistanceEqualsGracePeriod() {
    Strategy strategy = new Strategy();
    Position source = new Position(0, 0, 0);
    Position destination = new Position(3, 4, 0); // Distance is 5
    PathConfig pathConfig = new PathConfig(5, 10); // Grace period is 5

    int travelTimeCost = strategy.calculateTravelTimeCost(source, destination, pathConfig);

    assertEquals(0, travelTimeCost);
  }

  @Test
  void shouldCalculateTravelTimeCostCorrectlyWhenDistanceGreaterThanGracePeriod() {
    Strategy strategy = new Strategy();
    Position source = new Position(0, 0, 0);
    Position destination = new Position(6, 8, 0); // Distance is 10
    PathConfig pathConfig = new PathConfig(5, 2); // Grace period is 5, death rate is 2

    int travelTimeCost = strategy.calculateTravelTimeCost(source, destination, pathConfig);

    assertEquals(10, travelTimeCost); // (10 - 5) * 2 = 10
  }
}