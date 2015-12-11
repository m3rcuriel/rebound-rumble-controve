package com.m3rcuriel.controve.control.trajectory;

import static org.junit.Assert.assertEquals;
import com.m3rcuriel.controve.util.CsvWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TrajectoryFollowerTest {

  CsvWriter writer;

  @Test
  public void test() {
    int netcycle = 0;
    writer = null;
    try {
      File dir = new File("logs/");
      if (!dir.exists()) {
        dir.mkdirs();
      }
      writer = new CsvWriter(new File("logs/traj.log.csv"), "Time", "Position", "Velocity",
          "Acceleration", "Command");
    } catch (IOException e) {
      e.printStackTrace();
    }

    TrajectoryFollower follower = new TrajectoryFollower();
    TrajectoryFollower.TrajectoryConfig config = new TrajectoryFollower.TrajectoryConfig();
    config.dt = 0.005;
    config.maxAcceleration = 180.0;
    config.maxVelocity = 60.0;
    TrajectoryFollower.TrajectorySetpoint setpoint = new TrajectoryFollower.TrajectorySetpoint();
    setpoint.acceleration = 0;
    setpoint.velocity = 0;
    setpoint.position = 0;
    follower.initialize(0.1, 0.0, 0.0, 1.0, 0.0, config);
    follower.setGoal(setpoint, 100.0);

    int cycles = 0;
    while (!follower.isFinishedTrajectory()) {
      loop(follower, setpoint, config.dt, netcycle);
      cycles++;
      netcycle++;
    }

    follower.calculate(setpoint.position, setpoint.velocity);
    setpoint = follower.getCurrentSetpoint();
    System.out.println("Took " + cycles + " cycles; " + cycles * config.dt + " seconds");
    assertEquals(setpoint.position, 100.0, 1E-3);
    assertEquals(setpoint.velocity, 0.0, 1E-3);

    follower.setGoal(setpoint, 0.0);
    cycles = 0;
    while (!follower.isFinishedTrajectory()) {
      loop(follower, setpoint, config.dt, netcycle);
      cycles++;
      netcycle++;
    }
    follower.calculate(setpoint.position, setpoint.velocity);
    setpoint = follower.getCurrentSetpoint();
    System.out.println("Took " + cycles + " cycles; " + cycles * config.dt + " seconds");
    assertEquals(setpoint.position, 0.0, 1E-3);
    assertEquals(setpoint.velocity, 0.0, 1E-3);

    setpoint.position = 0.0;
    setpoint.velocity = 60.0;
    setpoint.acceleration = 0;
    follower.setGoal(setpoint, 2.0);
    cycles = 0;
    while (!follower.isFinishedTrajectory()) {
      loop(follower, setpoint, config.dt, netcycle);
      cycles++;
      netcycle++;
    }
    follower.calculate(setpoint.position, setpoint.velocity);
    setpoint = follower.getCurrentSetpoint();
    System.out.println("Took " + cycles + " cycles; " + cycles * config.dt + " seconds");
    assertEquals(setpoint.position, 2.0, 1E-3);
    assertEquals(setpoint.velocity, 0.0, 1E-3);

  }

  private void loop(TrajectoryFollower follower, TrajectoryFollower.TrajectorySetpoint setpoint,
      double dt, double netcycle) {
    double command = follower.calculate(setpoint.position, setpoint.velocity);
    setpoint = follower.getCurrentSetpoint();
    System.out.println("Result: " + setpoint + "; Command: " + command);
    writer.writeLine(netcycle * dt, setpoint.position, setpoint.velocity, setpoint.acceleration,
        command);
  }

}
