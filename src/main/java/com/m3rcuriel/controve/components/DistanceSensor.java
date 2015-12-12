package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;

/**
 * The abstract representation of a sensor measuring distance. Can be zeroed.
 *
 * @author Lee Mracek
 */
public interface DistanceSensor extends ReZeroable {
  /**
   * Gets the current distance in inches.
   *
   * @return the current distance in inches
   */
  public double getDistanceInInches();

  /**
   * Gets the current distance in feet.
   *
   * @return the current distance in feet
   */
  public default double getDistanceInFeet() {
    return getDistanceInInches() / 12.0;
  }

  /**
   * Zeroes the DistanceSensor.
   *
   * @return the newly zeroed DistanceSensor
   */
  @Override
  public default DistanceSensor zero() {
    return this;
  }

  /**
   * Create a new DistanceSensor based on a {@link DoubleSupplier} representing the sensor.
   *
   * @param distanceSupplier the supplier function representing the distance
   * @return the new DistanceSensor
   */
  public static DistanceSensor create(DoubleSupplier distanceSupplier) {
    return new DistanceSensor() {
      private double zero = 0;

      @Override
      public double getDistanceInInches() {
        return distanceSupplier.getAsDouble() - zero;
      }

      @Override
      public DistanceSensor zero() {
        zero = distanceSupplier.getAsDouble();
        return this;
      }
    };
  }

  /**
   * Inverts a DistanceSensor.
   *
   * @param sensor the DistanceSensor to invert
   * @return the inverted DistanceSensor
   */
  public static DistanceSensor invert(DistanceSensor sensor) {
    return new DistanceSensor() {
      @Override
      public double getDistanceInInches() {
        double dist = sensor.getDistanceInInches();
        return dist == 0.0 ? 0.0 : -dist;
      }

      @Override
      public DistanceSensor zero() {
        return sensor.zero();
      }
    };
  }
}
