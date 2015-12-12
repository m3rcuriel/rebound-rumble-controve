package com.m3rcuriel.controve.components;

import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * An abstract representation of a Power Distribution Panel on the robot.
 *
 * @author Lee Mracek
 */
public interface PowerDistributionPanel {
  /**
   * Retrieve the CurrentSensor instance representing the current on each port of the PDP.
   *
   * @param channel the channel to read the current from
   * @return the CurrentSensor instance
   */
  public CurrentSensor getCurrentSensor(int channel);

  /**
   * Get the current from a given channel on the PDP.
   *
   * @param channel the channel
   * @return the current through the specified channel
   */
  public default double getCurrent(int channel) {
    return getCurrentSensor(channel).getCurrent();
  }

  /**
   * A CurrentSensor instance representing the total current through the PDP.
   *
   * @return the CurrentSensor instance
   */
  public CurrentSensor getTotalCurrentSensor();

  /**
   * Get the total current through the PDP.
   *
   * @return the total current
   */
  public default double getTotalCurrent() {
    return getTotalCurrentSensor().getCurrent();
  }

  /**
   * Get a {@link VoltageSensor} representing the voltage supplied to the PDP.
   *
   * @return the VoltageSensor
   */
  public VoltageSensor getVoltageSensor();

  /**
   * Get the voltage supplied to the PDP.
   *
   * @return the voltage
   */
  public default double getVoltage() {
    return getVoltageSensor().getVoltage();
  }

  /**
   * Get the TemperatureSensor associated with the PDP.
   *
   * @return the {@link TemperatureSensor}
   */
  public TemperatureSensor getTemperatureSensor();

  /**
   * Get the temperature of the PDP.
   *
   * @return the temperature
   */
  public default double getTemperature() {
    return getTemperatureSensor().getTemperatureInCelsius();
  }

  /**
   * Create a new PowerDistributionPanel based on a mapper of int to double, a double supplier for
   * current, a double supplier for voltage, and a double supplier for temperature.
   *
   * @param currentForChannel the mapper between int and current
   * @param totalCurrent      the total current supplier
   * @param voltage           the voltage supplier
   * @param temperature       the temperature supplier
   * @return the constructed PowerDistributionPanel
   */
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

  /**
   * Construct a new PowerDistributionPanel based on a mapper of int to {@link CurrentSensor}, a
   * {@link CurrentSensor} for total current, a {@link VoltageSensor} for voltage, and a {@link
   * TemperatureSensor} for temperature.
   *
   * @param currentSensorForChannel the mapper between int and {@link CurrentSensor}
   * @param totalCurrent a CurrentSensor representing the total current
   * @param voltage a VoltageSensor representing the supplied voltage
   * @param temperature a {@link TemperatureSensor} representing the current temperature
   * @return the constructed {@link PowerDistributionPanel}
   */
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
