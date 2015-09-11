package com.m3rcuriel.controve.controllers.util;

public class Motion {

  public double leftDistance;
  public double rightDistance;
  public double leftVelocity;
  public double rightVelocity;
  public double heading;
  public double headingVelocity;

  public Motion(double leftDistance, double rightDistance, double leftVelocity,
      double rightVelocity, double heading, double headingVelocity) {
    this.leftDistance = leftDistance;
    this.rightDistance = rightDistance;
    this.leftVelocity = leftVelocity;
    this.rightVelocity = rightVelocity;
    this.heading = heading;
    this.headingVelocity = headingVelocity;
  }

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
   * @return the leftDistance
   */
  public double getLeftDistance() {
    return leftDistance;
  }

  /**
   * Get right distance of the motion.
   * @return the rightDistance
   */
  public double getRightDistance() {
    return rightDistance;
  }

  /**
   * Get left velocity of the motion.
   * @return the leftVelocity
   */
  public double getLeftVelocity() {
    return leftVelocity;
  }

  /**
   * Get right velocity of the motion.
   * @return the rightVelocity
   */
  public double getRightVelocity() {
    return rightVelocity;
  }

  /**
   * Get the heading of the motion.
   * @return the heading
   */
  public double getHeading() {
    return heading;
  }

  /**
   * Get the rate in change of angle of the motion.
   * @return the headingVelocity
   */
  public double getHeadingVelocity() {
    return headingVelocity;
  }
}
