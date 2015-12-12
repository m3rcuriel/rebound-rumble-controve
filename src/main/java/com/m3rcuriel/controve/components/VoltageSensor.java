package com.m3rcuriel.controve.components;

/**
 * A abstract sensor for voltage.
 *
 * @author Lee Mracek
 */
public interface VoltageSensor {

  /**
   * Retrieve the voltage.
   *
   * @return the voltage
   */
  public double getVoltage();
}
