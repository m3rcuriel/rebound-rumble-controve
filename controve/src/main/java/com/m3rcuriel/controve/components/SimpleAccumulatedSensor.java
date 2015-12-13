package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;

/**
 * An abstract representation of a sensor which reads the derivative and accumulates the value.
 * Example: read dx/dt, accumulate to get x.
 *
 * @author Lee Mracek
 */
public interface SimpleAccumulatedSensor extends ReZeroable {
  /**
   * Retrieve the position measured by the sensor.
   * @return the position
   */
  public double getPosition();

  /**
   * Retrieve the rate measured by the sensor.
   *
   * @return the rate
   */
  public double getRate();

  /**
   * Zeroes the SimpleAccumulatedSensor.
   *
   * @return the newly zeroed SimpleAccumulatedSensor.
   */
  @Override
  public default SimpleAccumulatedSensor zero() {
    return this;
  }

  /**
   * Creates a new SimpleAccumulatedSensor based on a supplier of position and of rate.
   *
   * @param positionSupplier the position supplier
   * @param rateSupplier the rate supplier
   * @return the SimpleAccumulatedSensor
   */
  public static SimpleAccumulatedSensor create(DoubleSupplier positionSupplier,
      DoubleSupplier rateSupplier) {
    return new SimpleAccumulatedSensor() {
      private volatile double zero = 0;

      @Override
      public double getPosition() {
        return positionSupplier.getAsDouble() - zero;
      }

      @Override
      public double getRate() {
        return rateSupplier.getAsDouble();
      }

      @Override
      public SimpleAccumulatedSensor zero() {
        zero = positionSupplier.getAsDouble();
        return this;
      }
    };
  }

  /**
   * Invert a new SimpleAccumulatedSensor.
   *
   * @param sensor the sensor to invert
   * @return the inverted sensor
   */
  public static SimpleAccumulatedSensor invert(SimpleAccumulatedSensor sensor) {
    return new SimpleAccumulatedSensor() {
      @Override
      public double getPosition() {
        double angle = sensor.getPosition();
        return angle == 0.0 ? 0.0 : -angle;
      }

      @Override
      public double getRate() {
        double rate = sensor.getRate();
        return rate == 0.0 ? 0.0 : -rate;
      }

      @Override
      public SimpleAccumulatedSensor zero() {
        return sensor.zero();
      }
    };
  }
}
