package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;

/**
 * An interface representation of a sensor measuring angular position.
 *
 * @author Lee Mracek
 */
public interface AngleSensor extends ReZeroable {
  /**
   * Get the current measured angle position of the sensor.
   *
   * @return the current angular position in degrees
   */
  public double getAngle();

  /**
   * Find the difference between the current location and the target angle within a tolerance.
   *
   * @param targetAngle the goal angle
   * @param tolerance the tolerance to consider "at" the goal
   * @return the difference between the target and the goal; 0 if within tolerance
   */
  public default double computeAngleChangeTo(double targetAngle, double tolerance) {
    double diff = targetAngle - this.getAngle();
    return Math.abs(diff) <= Math.abs(tolerance) ? 0.0 : diff;
  }

  /**
   * Zero the sensor.
   *
   * @return the newly zeroed AngleSensor.
   */
  @Override
  public default AngleSensor zero() {
    return this;
  }

  /**
   * Construct a new AngleSensor based on a {@link DoubleSupplier}.
   *
   * @param angleSupplier the supplier method for the sensor
   * @return the new AngleSensor
   */
  public static AngleSensor create(DoubleSupplier angleSupplier) {
    return new AngleSensor() {
      private volatile double zero = 0;

      @Override
      public double getAngle() {
        return angleSupplier.getAsDouble() - zero;
      }

      @Override
      public AngleSensor zero() {
        zero = angleSupplier.getAsDouble();
        return this;
      }
    };
  }

  /**
   * Invert a given AngleSensor instance.
   *
   * @param sensor the sensor to invert
   * @return the inverted sensor
   */
  public static AngleSensor invert(AngleSensor sensor) {
    return new AngleSensor() {
      @Override
      public double getAngle() {
        double angle = sensor.getAngle();
        return angle == 0.0 ? 0.0 : -angle;
      }

      @Override
      public AngleSensor zero() {
        return sensor.zero();
      }
    };
  }
}
