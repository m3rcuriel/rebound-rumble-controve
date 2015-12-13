package com.m3rcuriel.controve.control.misc;

/**
 * Class containing a simple representation of the output to a left-right motor drivebase.
 *
 * @author Lee Mracek
 */
public final class DriveOutput {
  /**
   * The output of the left motors.
   */
  public double leftMotors;
  /**
   * The output of the right motors.
   */
  public double rightMotors;

  /**
   * Initialize a DriveOutput with the given left-right motor values.
   * @param left the left motor output
   * @param right the right motor output
   */
  public DriveOutput(double left, double right) {
    this.leftMotors = left;
    this.rightMotors = right;
  }

  /**
   * Stopped drive.
   */
  public static DriveOutput NEUTRAL = new DriveOutput(0, 0);
}
