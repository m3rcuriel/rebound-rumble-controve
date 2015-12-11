package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author Lee Mracek
 */
public interface PowerDistributionPanel {
  public CurrentSensor getCurrentSensor(int channel);

  public default double getCurrent(int channel) {
    return getCurrentSensor(channel).getCurrent();
  }

  public CurrentSensor getTotalCurrentSensor();

  public default double getTotalCurrent() {
    return getTotalCurrentSensor().getCurrent();
  }

  public VoltageSensor getVoltageSensor();

  public default double getVoltage() {
    return getVoltageSensor().getVoltage();
  }

  public TemperatureSensor getTemperatureSensor();

  public default double getTemperature() {
    return getTemperatureSensor().getTemperatureInCelcius();
  }

  public static PowerDistributionPanel create(IntFunction<Double> currentForChannel,
      DoubleSupplier totalCurrent, DoubleSupplier voltage, DoubleSupplier temperature) {
    return new PowerDistributionPanel() {
      @Override
      public CurrentSensor getCurrentSensor(int channel) {
        return () -> currentForChannel.apply(channel);
      }

      @Override
      public CurrentSensor getTotalCurrentSensor() {
        return totalCurrent::getAsDouble;
      }

      @Override
      public VoltageSensor getVoltageSensor() {
        return voltage::getAsDouble;
      }

      @Override
      public TemperatureSensor getTemperatureSensor() {
        return voltage::getAsDouble;
      }
    };
  }

  public static PowerDistributionPanel create(
      Function<Integer, CurrentSensor> currentSensorForChannel, CurrentSensor totalCurrent,
      VoltageSensor voltage, TemperatureSensor temperature) {
    return new PowerDistributionPanel() {
      @Override
      public CurrentSensor getCurrentSensor(int channel) {
        return currentSensorForChannel.apply(channel);
      }

      @Override
      public CurrentSensor getTotalCurrentSensor() {
        return totalCurrent;
      }

      @Override
      public VoltageSensor getVoltageSensor() {
        return voltage;
      }

      @Override
      public TemperatureSensor getTemperatureSensor() {
        return temperature;
      }
    };
  }
}
