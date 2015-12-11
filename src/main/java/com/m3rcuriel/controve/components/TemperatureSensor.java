package com.m3rcuriel.controve.components;

/**
 * Created by lee on 12/10/15.
 */
public interface TemperatureSensor {
  public double getTemperatureInCelcius();

  public default double getTemperatureInFahrenheit() {
    return getTemperatureInCelcius() * 9.0 / 5.0 + 32.0;
  }
}
