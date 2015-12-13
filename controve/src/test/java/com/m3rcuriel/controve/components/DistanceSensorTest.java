package com.m3rcuriel.controve.components;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by lee on 12/10/15.
 */
public class DistanceSensorTest extends AbstractDoubleValueTest {
  private static DistanceSensor sensor = DistanceSensor.create(DistanceSensorTest::getValue);

  protected static void assertDistance(double distance) {
    assertValue(distance, sensor::getDistanceInInches, distance);
  }

  protected static void assertDistance(double distance, double result) {
    assertValue(distance, sensor::getDistanceInInches, result);
  }

  protected static void assertDistanceInFeet(double distance) {
    assertValue(distance, sensor::getDistanceInFeet, distance / 12.0);
  }

  @Override
  @Before
  public void beforeEach() {
    setValue(0);
    sensor.zero();
  }

  @Test
  public void shouldReturnPositiveDistance() {
    assertDistance(0.0);
    assertDistance(10.0);
    assertDistance(100.0);
    assertDistance(0.001);
  }

  @Test
  public void shouldReturnPositiveDistanceInFeet() {
    assertDistanceInFeet(0.0);
    assertDistanceInFeet(12.0);
    assertDistanceInFeet(144.0);
    assertDistanceInFeet(1);
  }

  @Test
  public void shouldReturnNegativeDistance() {
    assertDistance(-0.0);
    assertDistance(-10.0);
    assertDistance(-100.0);
    assertDistance(-0.001);
  }

  @Test
  public void shouldRemoveZeroFromValue() {
    setValue(45);
    sensor.zero();
    assertDistance(45, 0);
    assertDistance(90, 45);
    assertDistance(0, -45);
  }
}
