package com.m3rcuriel.controve.components;

/**
 * An abstract representation of a linearly actuated solenoid.
 *
 * @author Lee Mracek
 */
public interface Solenoid {
  /**
   * Enum representing the possible states of the solenoid.
   */
  static enum Direction {
    EXTENDING, RETRACTING, STOPPED;
  }

  /**
   * Retrieve the current {@link Direction} of the Solenoid.
   *
   * @return the current direction of the solenoid.
   */
  Direction getDirection();

  /**
   * Extend the solenoid.
   *
   * @return this Solenoid for method chaining.
   */
  Solenoid extend();

  /**
   * Retract the solenoid.
   *
   * @return this Solenoid for method chaining.
   */
  Solenoid retract();

  default boolean isExtending() {
    return getDirection() == Direction.EXTENDING;
  }

  default boolean isRetracting() {
    return getDirection() == Direction.RETRACTING;
  }

  default boolean isStopped() {
    return getDirection() == Direction.STOPPED;
  }
}
