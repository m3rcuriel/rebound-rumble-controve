package com.m3rcuriel.controve.components;


import com.m3rcuriel.controve.util.Values;

/**
 * @author Lee Mracek
 */
public interface Motor extends SpeedSensor, SpeedController {
  public enum Direction {
    FORWARD, REVERSE, STOPPED
  }

  @Override
  public double getSpeed();

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
