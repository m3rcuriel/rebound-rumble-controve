package com.m3rcuriel.controve.trajectory;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.m3rcuriel.controve.util.CSVWriter;

public class TrajectoryFollowerTest {

	@Test
	public void test() {
		int netcycle = 0;
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new File("traj.log.csv"), "Time", "Position", "Velocity", "Acceleration", "Command");
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
			double command = follower.calculate(setpoint.position, setpoint.velocity);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			writer.writeLine(netcycle * config.dt, setpoint.position, setpoint.velocity, setpoint.acceleration,
					command);
			assert(setpoint.velocity > -1E-3);
			assert(command > -1E-3); // should never run this low
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
			double command = follower.calculate(setpoint.position, setpoint.velocity);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			writer.writeLine(netcycle * config.dt, setpoint.position, setpoint.velocity, setpoint.acceleration,
					command);
			assert(setpoint.velocity > -1E-3);
			assert(command > -1E-3); // should never run this low
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
			double command = follower.calculate(setpoint.position, setpoint.velocity);
			setpoint = follower.getCurrentSetpoint();
			System.out.println("Result: " + setpoint + "; Command: " + command);
			writer.writeLine(netcycle * config.dt, setpoint.position, setpoint.velocity, setpoint.acceleration,
					command);
			assert(setpoint.velocity > -1E-3);
			assert(command > -1E-3); // should never run this low
			cycles++;
			netcycle++;
		}
		follower.calculate(setpoint.position, setpoint.velocity);
		setpoint = follower.getCurrentSetpoint();
		System.out.println("Took " + cycles + " cycles; " + cycles * config.dt + " seconds");
		assertEquals(setpoint.position, 2.0, 1E-3);
		assertEquals(setpoint.velocity, 0.0, 1E-3);

	}

}
