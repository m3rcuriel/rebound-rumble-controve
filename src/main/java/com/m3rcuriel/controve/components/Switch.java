package com.m3rcuriel.controve.components;

/**
 * Any readable device which is either active, or inactive.
 *
 * @author Lee Mracek
 */
public interface Switch {
  /**
   * Determine whether or not the Switch is presently triggered.
   *
   * @return true if the switch is triggered
   */
  public boolean isTriggered();
}
