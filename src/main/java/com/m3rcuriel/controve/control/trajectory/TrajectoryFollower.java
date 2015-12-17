package com.m3rcuriel.controve.control.trajectory;

// TODO should extend more general Follower or TrajectoryFollower


/**
 * A class which steps through a Trajectory and calculates the necessary next state based on the
 * current state. Essentially, this class uses simple kinematics to generate trapezoidal motion
 * profiles on the fly, including with a given initial state, etc.
 *
 * @author Lee Mracek
 */
public class TrajectoryFollower {

  /**
   * A configuration for a TrajectoryFollower, including the period and maximums.
   */
  public static class TrajectoryConfig {
    /**
     * the period
     */
    public double dt;
    /**
     * the maximum acceleration
     */
    public double maxAcceleration;
    /**
     * the maximum velocity
     */
    public double maxVelocity;

    @Override
    public String toString() {
      return "dt: " + dt + ", Max Acc: " + maxAcceleration + ", Max Vel: " + maxVelocity;
    }
  }


  /**
   * A specific setpoint on the 1D trajectory containing position, velocity, and acceleration.
   */
  public static class TrajectorySetpoint {
    /**
     * the position
     */
    public double position;
    /**
     * the velocity
     */
    public double velocity;
    /**
     * the acceleration
     */
    public double acceleration;

    @Override
    public String toString() {
      return "Position: " + position + ", Velocity: " + velocity + ", Acceleration: "
          + acceleration;
    }
  }


  private double kP, kI, kD, kV, kA, lastError, errorSum;
  private double maxOutput = 1.0, minOutput = -1.0;
  private boolean reset = true;
  private double lastTimestamp;
  private TrajectorySetpoint nextState = new TrajectorySetpoint();

  private TrajectoryConfig config = new TrajectoryConfig();
  private double goalPosition;
  private TrajectorySetpoint currentState = new TrajectorySetpoint();

  /**
   * Initialize a TrajectoryFollower with PID constants and a config. The config is used to
   * determine the trapezoidal trajectory on the fly.
   *
   * @param kP     the proportional constant
   * @param kI     the integral constant
   * @param kD     the derivative constant
   * @param kV     the velocity constant (inverse of the max velocity)
   * @param kA     the acceleration constant
   * @param config the configuration for the TrajectoryFollower
   */
  public void initialize(double kP, double kI, double kD, double kV, double kA,
      TrajectoryConfig config) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kV = kV;
    this.kA = kA;
    this.config = config;
  }

  /**
   * Set a new goal for the TrajectoryFollower.
   *
   * @param currentState the current state of the robot
   * @param goalPosition the new goal
   */
  public void setGoal(TrajectorySetpoint currentState, double goalPosition) {
    this.goalPosition = goalPosition;
    this.currentState = currentState;
    reset = true;
    errorSum = 0F;
  }

  /**
   * Retrieve the current goal of the trajectory follower.
   *
   * @return the current goal position
   */
  public double getGoal() {
    return goalPosition;
  }

  /**
   * Returns the {@link TrajectoryConfig} of the TrajectoryFollower.
   *
   * @return the TrajectoryConfig of the TrajectoryFollower instance
   */
  public TrajectoryConfig getConfig() {
    return config;
  }

  /**
   * Sets the TrajectoryConfig.
   *
   * @param config the new config
   */
  public void setConfig(TrajectoryConfig config) {
    this.config = config;
  }

  /**
   * Calculuates the next appropriate state for the robot based on simple kinematics and
   * constraints. It knows how fast it's going, how fast it can go, and how much time/space it has.
   * Pretend like this is Birdsong physics.
   *
   * @param position the current position
   * @param velocity the current velocity
   * @return the calculated necessary speed values
   */
  public double calculate(double position, double velocity) {
    double dt = config.dt;
    // we done.
    if (isFinishedTrajectory()) {
      currentState.position = goalPosition;
      currentState.velocity = 0;
      currentState.acceleration = 0;
    } else {
      double distanceToGo = goalPosition - currentState.position;
      double currentVelocity = currentState.velocity;
      double currentVelocitySqr = currentVelocity * currentVelocity;
      boolean inverted = false;
      // Marty! We need to go back!
      if (distanceToGo < 0) {
        inverted = true;
        distanceToGo *= -1;
        currentVelocity *= -1;
      }

      // Determine max reachable velocities so that we know if we are going to go /\ or nah
      double maxReachableVelocityDisc =
          currentVelocitySqr / 2.0 + config.maxAcceleration * distanceToGo;
      double minReachableVelocityDisc =
          currentVelocitySqr / 2.0 - config.maxAcceleration * distanceToGo;

      double cruiseVelocity = currentVelocity;

      // our cruise velocity is either the absolute max, or possible max
      if (minReachableVelocityDisc < 0 || cruiseVelocity < 0) {
        cruiseVelocity = Math.min(config.maxVelocity, Math.sqrt(maxReachableVelocityDisc));
      }

      // calculate the time to reach cruise velocity, and the distance requried
      double tStart = (cruiseVelocity - currentVelocity) / config.maxAcceleration;
      double xStart = currentVelocity * tStart + .5 * config.maxAcceleration * tStart * tStart;

      // calculate the time to ramp down from cruise velocity and the distance required
      double tEnd = Math.abs(cruiseVelocity / config.maxAcceleration);
      double xEnd = cruiseVelocity * tEnd - .5 * config.maxAcceleration * tEnd * tEnd;

      // calculate the time and distance at cruise velocity (0 if we never reach max velocity)
      double xCruise = Math.max(0, distanceToGo - xStart - xEnd);
      double tCruise = Math.abs(xCruise / cruiseVelocity);

      // figure out next setpoint
      if (tStart >= dt) {
        // okay, we're ramping up
        /// x = v0 + 1/2at^2
        nextState.position = currentVelocity * dt + .5 * config.maxAcceleration * dt * dt;
        // v = v0 + at
        nextState.velocity = currentVelocity + config.maxAcceleration * dt;
        // a = a
        nextState.acceleration = config.maxAcceleration;
      } else if (tStart + tCruise >= dt) {
        // okay, we're cruisin'
        // position = x0 + vdeltat
        nextState.position = xStart + cruiseVelocity * (dt - tStart);
        // v = v
        nextState.velocity = cruiseVelocity;
        // we ain't acceleratin' no more.
        nextState.acceleration = 0;
      } else if (tStart + tCruise + tEnd >= dt) {
        // stahhpppp
        double deltaT = dt - tStart - tCruise;
        // slow down using -a
        nextState.position = xStart + xCruise + cruiseVelocity * deltaT
            - .5 * config.maxAcceleration * deltaT * deltaT;
        // v = v - at
        nextState.velocity = cruiseVelocity - config.maxAcceleration * deltaT;
        // a = -a
        nextState.acceleration = -config.maxAcceleration;
      } else {
        // sum ting wong
        nextState.position = distanceToGo;
        nextState.velocity = 0;
        nextState.acceleration = 0;
      }
      if (inverted) {
        nextState.position *= -1;
        nextState.velocity *= -1;
        nextState.acceleration *= -1;
      }

      currentState.position += nextState.position;
      currentState.velocity = nextState.velocity;
      currentState.acceleration = nextState.acceleration;
    }
    double error = currentState.position - position;
    if (reset) {
      // prevent derivative jump on reset
      reset = false;
      lastError = error;
      errorSum = error;
    }

    double output = kP * error + kD * ((error - lastError) / dt - currentState.velocity) + (
        kV * currentState.velocity + kA * currentState.acceleration);
    if (output < maxOutput && output > minOutput) {
      // if the output is already maxed don't increment integral term
      errorSum += error * dt;
    }
    output += kI * errorSum;

    lastError = error;
    return output;
  }

  /**
   * Determine if the trajectory has been completed successfully.
   *
   * @return true if the robot has reached the goal.
   */
  public boolean isFinishedTrajectory() {
    return Math.abs(currentState.position - goalPosition) < 1E-3
        && Math.abs(currentState.velocity) < 1E-2;
  }

  /**
   * Return the currentt state of the trajectory follower .
   * @return the current TrajectorySetpoint
   */
  public TrajectorySetpoint getCurrentSetpoint() {
    return currentState;
  }
}
