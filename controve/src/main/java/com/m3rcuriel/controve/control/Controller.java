package com.m3rcuriel.controve.control;

/**
 * General controller abstraction.
 *
 * @author Lee Mracek
 */
public abstract class Controller {
  protected boolean enabled = false;

  /**
   * Reset the controller.
   */
  public abstract void reset();

  /**
   * Determine if the controller is on target.
   *
   * @return true if the controller is on target.
   */
  public abstract boolean isOnTarget();
}
