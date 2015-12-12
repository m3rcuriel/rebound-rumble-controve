package com.m3rcuriel.controve.components;

/**
 * An abstract interface representing an object which can have a new zero point. The newly zeroed
 * object is returned.
 *
 * @uathor Lee Mracek
 */
public interface ReZeroable {
  /**
   * Establish a new zero and return the newly zeroed object.
   *
   * @return the newly zeroed object
   */
  public ReZeroable zero();
}
