package com.m3rcuriel.controve.control.misc;

/**
 * This class acts as a complete represenation of a robot's physical state as can be measured by
 * encoders and gyros.
 *
 * @author Lee Mracek
 */
public class Motion {

  private double leftDistance;
  private double rightDistance;
  private double leftVelocity;
  private double rightVelocity;
  private double heading;
  private double headingVelocity;

  /**
   * Initialize a Motion with the given values.
   *
   * @param leftDistance the left encoder distance
   * @param rightDistance the right encoder distance
   * @param leftVelocity the left encoder rate
   * @param rightVelocity the right encoder rate
   * @param heading the heading of the robot
   * @param headingVelocity the angular velocity of the robot
   */
  public Motion(double leftDistance, double rightDistance, double leftVelocity,
      double rightVelocity, double heading, double headingVelocity) {
    this.leftDistance = leftDistance;
    this.rightDistance = rightDistance;
    this.leftVelocity = leftVelocity;
    this.rightVelocity = rightVelocity;
    this.heading = heading;
    this.headingVelocity = headingVelocity;
  }

  /**
   * Reset a Motion to the given values.
   *
   * @param leftDistance the left encoder distance
   * @param rightDistance the right encoder distance
   * @param leftVelocity the left encoder rate
   * @param rightVelocity the right encoder rate
   * @param heading the heading of the robot
   * @param headingVelocity the angular velocity of the robot
   */
  public void reset(double leftDistance, double rightDistance, double leftVelocity,
      double rightVelocity, double heading, double headingVelocity) {
    this.leftDistance = leftDistance;
    this.rightDistance = rightDistance;
    this.leftVelocity = leftVelocity;
    this.rightVelocity = rightVelocity;
    this.heading = heading;
    this.headingVelocity = headingVelocity;
  }

  /**
   * Get left distance of the motion.
   *
   * @return the leftDistance
   */
  public double getLeftDistance() {
    return leftDistance;
  }

  /**
   * Get right distance of the motion.
   *
   * @return the rightDistance
   */
  public double getRightDistance() {
    return rightDistance;
  }

  /**
   * Get left velocity of the motion.
   *
   * @return the leftVelocity
   */
  public double getLeftVelocity() {
    return leftVelocity;
  }

  /**
   * Get right velocity of the motion.
   *
   * @return the rightVelocity
   */
  public double getRightVelocity() {
    return rightVelocity;
  }

  /**
   * Get the heading of the motion.
   *
   * @return the heading
   */
  public double getHeading() {
    return heading;
  }

  /**
   * Get the rate in change of angle of the motion.
   *
   * @return the headingVelocity
   */
  public double getHeadingVelocity() {
    return headingVelocity;
  }
}
