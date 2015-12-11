package com.m3rcuriel.controve;

/**
 * Something that can be executed.
 *
 * @author Lee Mracek
 * @see Conductor
 */
public interface Executable {

  /**
   * Perform an execution at a given moment in a match.
   *
   * @param timeInMillis the time at which to perform the action
   */
  public void execute(long timeInMillis);
}
