package com.m3rcuriel.controve.control;

import com.m3rcuriel.controve.control.trajectory.TrajectoryFollower;

/**
 * A controller which will follow a given 1D trajectory.
 *
 * @author Lee Mracek
 */
public class TrajectoryFollowingPositionController extends Controller {
  TrajectoryFollower follower;
  double goal;
  double error;
  double onTargetDelta;
  double result = 0;

  /**
   * Construct a new TrajectoryFollowingPositionController with a certain config.
   *
   * @param kp            the proportional constant
   * @param ki            the integral constant
   * @param kd            the derivative constant
   * @param kv            the velocity constant (the inverse of the max velocity)
   * @param ka            the acceleration constant
   * @param onTargetDelta the tolerance to be considered on target
   * @param config        the {@link com.m3rcuriel.controve.control.trajectory.TrajectoryFollower
   *                      TrajectoryFollower config} to be used by this controller.
   */
  public TrajectoryFollowingPositionController(double kp, double ki, double kd, double kv,
      double ka, double onTargetDelta, TrajectoryFollower.TrajectoryConfig config) {
    follower = new TrajectoryFollower();
    follower.initialize(kp, ki, kd, kv, ka, config);
    this.onTargetDelta = onTargetDelta;
  }

  /**
   * Set the goal position of the TrajectoryFollower.
   *
   * @param currentState the current setpoint of the robot
   * @param goal the destination
   */
  public void setGoal(TrajectoryFollower.TrajectorySetpoint currentState, double goal) {
    this.goal = goal;
    follower.setGoal(currentState, goal);
  }

  /**
   * Get the goal of the TrajectoryFollower.
   *
   * @return the goal of the internal TrajectoryFollower
   */
  public double getGoal() {
    return follower.getGoal();
  }

  /**
   * Set the internal TrajectoryFollower's configuration.
   *
   * @param config the config to set the TrajectoryFollower to
   *
   * @see com.m3rcuriel.controve.control.trajectory.TrajectoryFollower.TrajectoryConfig
   */
  public void setConfig(TrajectoryFollower.TrajectoryConfig config) {
    follower.setConfig(config);
  }

  /**
   * Get the internal TrajectoryFollower's configuration.
   *
   * @return the config of the TrajectoryFollower.
   *
   * @see com.m3rcuriel.controve.control.trajectory.TrajectoryFollower.TrajectoryConfig
   */
  public TrajectoryFollower.TrajectoryConfig getConfig() {
    return follower.getConfig();
  }

  /**
   * Get the necessary output as calculated by the controller.
   *
   * @return the necessary motor output
   */
  public double get() {
    return result;
  }

  /**
   * Update the controller based on a given position and velocity.
   *
   * @param position the current position of the robot
   * @param velocity the current velocity of the robot
   */
  public void update(double position, double velocity) {
    error = goal - position;
    result = follower.calculate(position, velocity);
  }

  /**
   * Gets the current setpoint of the TrajectoryFollower.
   *
   * @return the current setpoint
   *
   * @see com.m3rcuriel.controve.control.trajectory.TrajectoryFollower.TrajectorySetpoint
   */
  public TrajectoryFollower.TrajectorySetpoint getSetpoint() {
    return follower.getCurrentSetpoint();
  }

  @Override
  public void reset() {
    result = 0;
    error = 0;
    follower.setGoal(follower.getCurrentSetpoint(), goal);
  }

  /**
   * Determines if the trajectory is completed.
   *
   * @return true if the trajectory is completed
   */
  public boolean isFinishedTrajectory() {
    return follower.isFinishedTrajectory();
  }

  /**
   * Determine if the controller has reached the target.
   *
   * @return true if the robot is on target to within error
   */
  @Override
  public boolean isOnTarget() {
    return follower.isFinishedTrajectory() && Math.abs(error) < onTargetDelta;
  }

}
