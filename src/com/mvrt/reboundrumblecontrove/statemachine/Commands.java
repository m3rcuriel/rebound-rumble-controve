package com.mvrt.reboundrumblecontrove.statemachine;

import com.m3rcuriel.controve.api.CommandsBase;

public class Commands extends CommandsBase {
  public enum ElevatorRequest {
    UP, DOWN, NONE;
  }

  public enum IntakeRequest {
    IN, OUT, NONE;
  }

  public enum ShooterRequest {
    SHOOTER_KEY, SHOOTER_INCR, SHOOTER_DECR, OFF, NONE;
  }

  public ElevatorRequest elevatorRequest = ElevatorRequest.NONE;
  public IntakeRequest intakeRequest = IntakeRequest.NONE;
  public ShooterRequest shooterRequest = ShooterRequest.OFF;
}
