package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;

/**
 * Created by lee on 12/10/15.
 */
public interface SimpleAccumulatedSensor extends Zeroable {
  public double getPosition();

  public double getRate();

  @Override
  public default SimpleAccumulatedSensor zero() {
    return this;
  }

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
