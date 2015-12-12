package com.m3rcuriel.controve.components;

/**
 * An abstract interface for an object measuring a current.
 *
 * @author Lee Mracek
 */
public interface CurrentSensor {
  /**
   * Get the current value of the current.
   *
   * @return the current
   */
  public double getCurrent();
}
