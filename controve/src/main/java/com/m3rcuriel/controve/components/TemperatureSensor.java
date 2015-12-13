package com.m3rcuriel.controve.components;

/**
 * An abstract representation of a sensor for temperature.
 *
 * @author Lee Mracek
 */
public interface TemperatureSensor {
  /**
   * Retrieve the current temperature in degrees Celsius.
   * @return the temperature
   */
  public double getTemperatureInCelsius();

  /**
   * Retrieve the current temperature in degrees Fahrenheit.
   * @return the temperature
   */
  public default double getTemperatureInFahrenheit() {
    return getTemperatureInCelsius() * 9.0 / 5.0 + 32.0;
  }
}
