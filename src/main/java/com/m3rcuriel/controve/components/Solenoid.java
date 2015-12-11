package com.m3rcuriel.controve.components;

/**
 * @author Lee Mracek
 */
public interface Solenoid {
  static enum Direction {
    EXTENDING, RETRACTING, STOPPED;
  }

  Direction getDirection();

  Solenoid extend();

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
