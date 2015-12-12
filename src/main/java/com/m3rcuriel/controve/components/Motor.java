package com.m3rcuriel.controve.components;


import com.m3rcuriel.controve.util.Values;

/**
 * An abstract interface representing a Motor. Allows you to set speed and retrieve speed.
 *
 * @author Lee Mracek
 */
public interface Motor extends SpeedSensor, SpeedController {
  /**
   * An enum representing the possible states of the Motor.
   */
  public enum Direction {
    FORWARD, REVERSE, STOPPED
  }

  /**
   * Get the speed of the motor.
   *
   * @return the speed of the motor
   */
  @Override
  public double getSpeed();

  /**
   * Set the speed of the motor.
   *
   * @param speed the new speed of the motor
   * @return this Motor for method chaining
   */
  @Override
  public Motor setSpeed(double speed);

  default void stop() {
    setSpeed(0.0);
  }

  /**
   * Create a new motor that inverts this motor
   *
   * @return the new inverted motor; never null.
   */
  default Motor invert() {
    return Motor.invert(this);
  }

  /**
   * Create a new motor that inverts the motor
   *
   * @param motor the motor to invert
   * @return the new inverted motor; never null.
   */
  static Motor invert(Motor motor) {
    return new Motor() {
      @Override
      public Motor setSpeed(double speed) {
        motor.setSpeed(-1 * speed);
        return this;
      }

      @Override
      public double getSpeed() {
        return -1 * motor.getSpeed();
      }
    };
  }

  /**
   * Retrieve the current direction of the motor based on its speed.
   *
   * @return the {@link Direction} of the motor
   */
  public default Direction getDirection() {
    int direction = Values.fuzzyCompare(getSpeed(), 0.0);
    if (direction < 0) {
      return Direction.REVERSE;
    } else if (direction > 0) {
      return Direction.FORWARD;
    } else {
      return Direction.STOPPED;
    }
  }

  /**
   * Construct a new motor which is the composite of two separate Motors. Useful for the left and
   * right sides of a West Coast Drive.
   *
   * @param motor1 the first motor
   * @param motor2 the second motor
   * @return a new motor composed of both parameter motors
   */
  static Motor compose(Motor motor1, Motor motor2) {
    return new Motor() {
      @Override
      public double getSpeed() {
        return motor1.getSpeed();
      }

      @Override
      public Motor setSpeed(double speed) {
        motor1.setSpeed(speed);
        motor2.setSpeed(speed);
        return this;
      }
    };
  }

  /**
   * Construct a new motor which is the composite of many Motors. Useful for left and right sides
   * of a West Coast Drive.
   *
   * @param motors the motors to combine
   * @return the new motor composed of both parameter motors
   */
  static Motor compose(Motor... motors) {
    return new Motor() {
      @Override
      public double getSpeed() {
        return motors[0].getSpeed();
      }

      @Override
      public Motor setSpeed(double speed) {
        for (Motor motor : motors) {
          motor.setSpeed(speed);
        }
        return this;
      }
    };
  }
}
