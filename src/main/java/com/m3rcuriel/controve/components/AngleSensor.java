package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;

/**
 * @author Lee Mracek
 */
public interface AngleSensor extends Zeroable {
  public double getAngle();

  public default double computeAngleChangeTo(double targetAngle, double tolerance) {
    double diff = targetAngle - this.getAngle();
    return Math.abs(diff) <= Math.abs(tolerance) ? 0.0 : diff;
  }

  @Override
  public default AngleSensor zero() {
    return this;
  }

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
