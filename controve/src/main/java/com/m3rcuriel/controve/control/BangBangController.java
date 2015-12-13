package com.m3rcuriel.controve.control;

/**
 * Implementation of BangBang control for simple systems.
 *
 * @author Lee Mracek
 */
public class BangBangController extends Controller {

  private double position;
  private double goal;
  private double tolerance;
  private double direction = 0.0;

  /**
   * Construct a BangBangController with a given tolerance.
   *
   * @param tolerance the controller tolerance
   */
  public BangBangController(double tolerance) {
    this.tolerance = tolerance;
  }

  /**
   * Sets the goal of the controller.
   *
   * @param goal the goal of the controller.
   */
  public void setGoal(double goal) {
    this.goal = goal;
  }

  /**
   * Retrieve the goal of the controller.
   *
   * @return the current goal
   */
  public double getGoal() {
    return goal;
  }

  /**
   * Resets the controller.
   */
  @Override
  public void reset() {
    direction = 0.0;
  }

  /**
   * Determine if the controller is on target.
   *
   * @return true if the controller is on target
   */
  @Override
  public boolean isOnTarget() {
    if (direction == 0.0) {
      return false;
    }
    return (direction > 0 ? position > (goal - tolerance) : position < (goal + tolerance));
  }

  /**
   * Update the controller.
   *
   * @param position the current position of the system
   * @return the desired output value (-1.0, 1.0, or 0.0)
   */
  public double update(double position) {
    if (direction == 0.0) {
      direction = (position > goal ? -1.0 : 1.0);
    }
    this.position = position;
    if (direction > 0) {
      if (position < (goal - tolerance)) {
        return 1.0;
      } else {
        return -1.0;
      }
    } else {
      if (position > (goal + tolerance)) {
        return -1.0;
      } else {
        return 0.0;
      }
    }
  }

}
