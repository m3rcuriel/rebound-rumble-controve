package com.m3rcuriel.controve.trajectory;

// TODO this class should extend (nonexistant) DynamicTrajectory class (or similar)
public class Trajectory {
  public static class Segment implements Cloneable {
    public double pos, vel, acc, jerk, dt, x, y;

    public Segment() {
    }

    public Segment(double pos, double vel, double acc, double jerk, double dt, double x, double y) {
      this.pos = pos;
      this.vel = vel;
      this.acc = acc;
      this.jerk = jerk;
      this.dt = dt;
      this.x = x;
      this.y = y;
    }

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

  public Trajectory(int length) {
    trajSegments = new Segment[length];
    for (int i = 0; i < length; i++) {
      trajSegments[i] = new Segment();
    }
  }

  public Trajectory(Segment[] segments) {
    this.trajSegments = segments;
  }

  public int getSegmentsLength() {
    return trajSegments.length;
  }

  public Segment getSegment(int index) throws IndexOutOfBoundsException {
    return trajSegments[index];
  }

  public void setSegment(int index, Segment segment) throws IndexOutOfBoundsException {
    trajSegments[index] = segment;
  }

  public Trajectory copy() {
    Trajectory cloned = new Trajectory(getSegmentsLength());
    cloned.trajSegments = copySegments(this.trajSegments);
    return cloned;
  }

  private Segment[] copySegments(Segment[] toCopy) {
    return toCopy.clone();
  }

  public static Segment[] getBlankSegment() {
    Segment[] blank = {new Segment(0, 0, 0, 0, 1F / 100F, 0, 0)};
    return blank;
  }
}
