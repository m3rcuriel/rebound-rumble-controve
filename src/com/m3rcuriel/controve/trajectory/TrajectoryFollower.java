package com.m3rcuriel.controve.trajectory;

// TODO should extend more general Follower or TrajectoryFollower
public class TrajectoryFollower {

	public static class TrajectoryConfig {
		public double dt;
		public double maxAcceleration;
		public double maxVelocity;

		@Override
		public String toString() {
			return "dt: " + dt + ", Max Acc: " + maxAcceleration + ", Max Vel: " + maxVelocity;
		}
	}

	public static class TrajectorySetpoint {
		public double position;
		public double velocity;
		public double acceleration;

		@Override
		public String toString() {
			return "Position: " + position + ", Velocity: " + velocity + ", Acceleration: " + acceleration;
		}
	}

	private double kP, kI, kD, kV, kA, lastError, errorSum;
	private double maxOutput = 1.0, minOutput = -1.0;
	private boolean reset = true;
	private double lastTimestamp;
	private TrajectorySetpoint nextState = new TrajectorySetpoint();

	private TrajectoryConfig config = new TrajectoryConfig();
	private double goalPosition;
	private TrajectorySetpoint currentState = new TrajectorySetpoint();

	public void initialize(double kP, double kI, double kD, double kV, double kA, TrajectoryConfig config) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kV = kV;
		this.kA = kA;
		this.config = config;
	}

	public void setGoal(TrajectorySetpoint currentState, double goalPosition) {
		this.goalPosition = goalPosition;
		this.currentState = currentState;
		reset = true;
		errorSum = 0F;
	}

	public double getGoal() {
		return goalPosition;
	}

	public TrajectoryConfig getConfig() {
		return config;
	}

	public void setConfig(TrajectoryConfig config) {
		this.config = config;
	}

	public double calculate(double position, double velocity) {
		double dt = config.dt;
		/*
		 * Add if inaccurate if (!reset) { double now =
		 * Timer.getFPGATimestamp(); dt = now - lastTimestamp; lastTimestamp =
		 * now; } else { lastTimestamp = Timer.getFPGATimestamp(); }
		 */

		if (isFinishedTrajectory()) {
			currentState.position = goalPosition;
			currentState.velocity = 0;
			currentState.acceleration = 0;
		} else {
			double distanceToGo = goalPosition - currentState.position;
			double currentVelocity = currentState.velocity;
			double currentVelocitySqr = currentVelocity * currentVelocity;
			boolean inverted = false;
			if (distanceToGo < 0) {
				inverted = true;
				distanceToGo *= -1;
				currentVelocity *= -1;
			}

			double maxReachableVelocityDisc = currentVelocitySqr / 2.0 + config.maxAcceleration * distanceToGo;
			double minReachableVelocityDisc = currentVelocitySqr / 2.0 - config.maxAcceleration * distanceToGo;

			double cruiseVelocity = currentVelocity;

			if (minReachableVelocityDisc < 0 || cruiseVelocity < 0) {
				cruiseVelocity = Math.min(config.maxVelocity, Math.sqrt(maxReachableVelocityDisc));
			}

			double tStart = (cruiseVelocity - currentVelocity) / config.maxAcceleration;
			double xStart = currentVelocity * tStart + .5 * config.maxAcceleration * tStart * tStart;

			double tEnd = Math.abs(cruiseVelocity / config.maxAcceleration);
			double xEnd = cruiseVelocity * tEnd - .5 * config.maxAcceleration * tEnd * tEnd;

			double xCruise = Math.max(0, distanceToGo - xStart - xEnd);
			double tCruise = Math.abs(xCruise / cruiseVelocity);

			// figure out next setpoint
			if (tStart >= dt) {
				nextState.position = currentVelocity * dt + .5 * config.maxAcceleration * dt * dt;
				nextState.velocity = currentVelocity + config.maxAcceleration * dt;
				nextState.acceleration = config.maxAcceleration;
			} else if (tStart + tCruise >= dt) {
				nextState.position = xStart + cruiseVelocity * (dt - tStart);
				nextState.velocity = cruiseVelocity;
				nextState.acceleration = 0;
			} else if (tStart + tCruise + tEnd >= dt) {
				double deltaT = dt - tStart - tCruise;
				nextState.position = xStart + xCruise + cruiseVelocity * deltaT
						- .5 * config.maxAcceleration * deltaT * deltaT;
				nextState.velocity = cruiseVelocity - config.maxAcceleration * deltaT;
				nextState.acceleration = -config.maxAcceleration;
			} else {
				nextState.position = distanceToGo;
				nextState.velocity = 0;
				nextState.acceleration = 0;
			}
			if (inverted) {
				nextState.position *= -1;
				nextState.velocity *= -1;
				nextState.acceleration *= -1;
			}

			currentState.position += nextState.position;
			currentState.velocity = nextState.velocity;
			currentState.acceleration = nextState.acceleration;
		}
		double error = currentState.position - position;
		if (reset) {
			// prevent derivative jump on reset
			reset = false;
			lastError = error;
			errorSum = error;
		}
		double output = kP * error + kD * ((error - lastError) / dt - currentState.velocity)
				+ (kV * currentState.velocity + kA * currentState.acceleration);
		if (output < maxOutput && output > minOutput) {
			// if the output is already maxed don't increment integral term
			errorSum += error * dt;
		}
		output += kI * errorSum;

		lastError = error;
		return output;
	}

	public boolean isFinishedTrajectory() {
		return Math.abs(currentState.position - goalPosition) < 1E-3 && Math.abs(currentState.velocity) < 1E-2;
	}

	public TrajectorySetpoint getCurrentSetpoint() {
		return currentState;
	}
}
