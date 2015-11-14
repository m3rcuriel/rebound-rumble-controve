package com.mvrt.reboundrumblecontrove;

import com.mvrt.reboundrumblecontrove.statemachine.Commands;

import edu.wpi.first.wpilibj.Joystick;

public class OperatorInterface {
  private Commands commands = new Commands();

  private Joystick driverJoystick = HardwareInterface.kDriverJoystick;

  public void reset() {
    commands = new Commands();
  }

  public Commands getCommands() {
    if (driverJoystick.getRawButton(10)) {
      commands.elevatorRequest = Commands.ElevatorRequest.UP;
    } else if (driverJoystick.getRawButton(11)) {
      commands.elevatorRequest = Commands.ElevatorRequest.DOWN;
    } else {
      commands.elevatorRequest = Commands.ElevatorRequest.NONE;
    }

    if (driverJoystick.getRawButton(1)) {
      commands.shooterRequest = Commands.ShooterRequest.SHOOTER_KEY;
    } else if (driverJoystick.getRawButton(2)) {
      commands.shooterRequest = Commands.ShooterRequest.OFF;
    } else if (driverJoystick.getRawButton(4)) {
      commands.shooterRequest = Commands.ShooterRequest.SHOOTER_INCR;
    } else if (driverJoystick.getRawButton(5)) {
      commands.shooterRequest = Commands.ShooterRequest.SHOOTER_DECR;
    } else {
      commands.shooterRequest = Commands.ShooterRequest.NONE;
    }

    if (driverJoystick.getRawButton(6)) {
      commands.intakeRequest = Commands.IntakeRequest.IN;
    } else if (driverJoystick.getRawButton(8)) {
      commands.intakeRequest = Commands.IntakeRequest.OUT;
    } else {
      commands.intakeRequest = Commands.IntakeRequest.NONE;
    }

    return commands;
  }
}
