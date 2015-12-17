package com.m3rcuriel.controve.components;

/**
 * @author Lee Mracek
 */
public interface DriveTrain {
  void drive(double leftMotor, double rightMotor);

  static DriveTrain create(Motor left, Motor right) {
    if (left == null) {
      throw new IllegalArgumentException("Left Motor cannot be null");
    }
    if (right == null) {
      throw new IllegalArgumentException("Right Motor cannot be null");
    }

    return (leftMotor, rightMotor) -> {
      left.setSpeed(leftMotor);
      right.setSpeed(rightMotor);
    };
  }
}
