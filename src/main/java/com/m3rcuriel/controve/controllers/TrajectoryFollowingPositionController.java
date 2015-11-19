package com.m3rcuriel.controve.controllers;

import com.m3rcuriel.controve.trajectory.TrajectoryFollower;

public class TrajectoryFollowingPositionController extends Controller {
  TrajectoryFollower follower;
  double goal;
  double error;
  double onTargetDelta;
  double result = 0;

  public TrajectoryFollowingPositionController(double kp, double ki, double kd, double kv,
      double ka, double onTargetDelta, TrajectoryFollower.TrajectoryConfig config) {
    follower = new TrajectoryFollower();
    follower.initialize(kp, ki, kd, kv, ka, config);
    this.onTargetDelta = onTargetDelta;
  }

  public void setGoal(TrajectoryFollower.TrajectorySetpoint currentState, double goal) {
    this.goal = goal;
    follower.setGoal(currentState, goal);
  }

  public double getGoal() {
    return follower.getGoal();
  }

  public void setConfig(TrajectoryFollower.TrajectoryConfig config) {
    follower.setConfig(config);
  }

  public TrajectoryFollower.TrajectoryConfig getConfig() {
    return follower.getConfig();
  }

  public double get() {
    return result;
  }

  public void update(double position, double velocity) {
    error = goal - position;
    result = follower.calculate(position, velocity);
  }

  public TrajectoryFollower.TrajectorySetpoint getSetpoint() {
    return follower.getCurrentSetpoint();
  }

  @Override
  public void reset() {
    result = 0;
    error = 0;
    follower.setGoal(follower.getCurrentSetpoint(), goal);
  }

  public boolean isFinishedTrajectory() {
    return follower.isFinishedTrajectory();
  }

  @Override
  public boolean isOnTarget() {
    return follower.isFinishedTrajectory() && Math.abs(error) < onTargetDelta;
  }

}
