package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;

/**
 * Created by lee on 12/10/15.
 */
public interface DistanceSensor extends Zeroable {
  public double getDistanceInInches();

  public default double getDistanceInFeet() {
    return getDistanceInInches() / 12.0;
  }

  @Override
  public default DistanceSensor zero() {
    return this;
  }

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
