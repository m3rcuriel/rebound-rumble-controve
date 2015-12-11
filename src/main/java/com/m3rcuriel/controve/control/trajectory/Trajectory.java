package com.m3rcuriel.controve.control.trajectory;

// TODO this class should extend (nonexistant) DynamicTrajectory class (or similar)


/**
 * A class representing a general Trajectory consisting of a series of setpoints.
 *
 * @author Lee Mracek
 */
public class Trajectory {
  /**
   * Class defining a segment of a trajectory
   */
  public static class Segment implements Cloneable {
    public double pos, vel, acc, jerk, dt, x, y;

    public Segment() {
    }

    /**
     * Create a segment based on the physical state of the robot.
     *
     * @param pos the position
     * @param vel the velocity
     * @param acc the acceleration
     * @param jerk the jerk
     * @param dt the time period
     * @param x the x position
     * @param y the y position
     */
    public Segment(double pos, double vel, double acc, double jerk, double dt, double x, double y) {
      this.pos = pos;
      this.vel = vel;
      this.acc = acc;
      this.jerk = jerk;
      this.dt = dt;
      this.x = x;
      this.y = y;
    }

    /**
     * Construct a segment from another segment.
     *
     * @param toCopy the segment to copy.
     */
    public Segment(Segment toCopy) {
      pos = toCopy.pos;
      vel = toCopy.vel;
      acc = toCopy.acc;
      jerk = toCopy.jerk;
      dt = toCopy.dt;
      x = toCopy.x;
      y = toCopy.y;
    }

    @Override
    public Object clone() {
      return new Segment(this);
    }
  }


  Segment[] trajSegments = null;

  /**
   * Create a blank trajectory with the given number of {@link Segment}s.
   *
   * @param length the number of Segments in the Trajectory
   */
  public Trajectory(int length) {
    trajSegments = new Segment[length];
    for (int i = 0; i < length; i++) {
      trajSegments[i] = new Segment();
    }
  }

  /**
   * Construct a trajectory based on an array of segments.
   *
   * @param segments the segments in the Trajectory
   */
  public Trajectory(Segment[] segments) {
    this.trajSegments = segments;
  }

  /**
   * The number of segments in the Trajectory.
   *
   * @return the number of segments
   */
  public int getSegmentsLength() {
    return trajSegments.length;
  }

  /**
   * Retrieve a {@link Segment} at a specific index.
   * @param index the index of the Segment
   * @return the Segment at that index
   * @throws IndexOutOfBoundsException if the index is greater than the length
   */
  public Segment getSegment(int index) throws IndexOutOfBoundsException {
    return trajSegments[index];
  }

  /**
   * Sets a specific Segment to a new value.
   *
   * @param index the index of the Setpoint
   * @param segment the new value for the Setpoint
   * @throws IndexOutOfBoundsException if the index is greater than the length
   */
  public void setSegment(int index, Segment segment) throws IndexOutOfBoundsException {
    trajSegments[index] = segment;
  }

  /**
   * Clone a Trajectory.
   *
   * @return a deep copy of the Trajectory
   */
  @Override
  public Trajectory clone() {
    Trajectory cloned = new Trajectory(getSegmentsLength());
    cloned.trajSegments = copySegments(this.trajSegments);
    return cloned;
  }

  /**
   * Deep-copy an array of {@link Segment}s.
   * @param toCopy the array of {@link Segment}s to copy
   * @return an exact copy of the initial array
   */
  private Segment[] copySegments(Segment[] toCopy) {
    return toCopy.clone();
  }

  /**
   * Statically construct a new blank {@link Segment}.
   *
   * @return a blank segment
   */
  public static Segment[] getBlankSegment() {
    Segment[] blank = {new Segment(0, 0, 0, 0, 1F / 100F, 0, 0)};
    return blank;
  }
}
