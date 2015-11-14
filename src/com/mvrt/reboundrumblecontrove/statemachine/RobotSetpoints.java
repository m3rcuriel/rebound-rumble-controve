package com.mvrt.reboundrumblecontrove.statemachine;

import com.m3rcuriel.controve.api.RobotSetpointsBase;

import java.util.Optional;

public class RobotSetpoints extends RobotSetpointsBase {

  public enum IntakeAction {
    IN, OUT, NONE;
  }

  public enum ElevatorAction {
    UP, DOWN, NONE;
  }

  public enum ShooterAction {
    NONE, FORCE
  }

  public IntakeAction intakeAction;
  public ElevatorAction elevatorAction;
  public ShooterAction shooterAction;
  public Optional<Double> shooterSpeed;

  @Override
  public void reset() {
    intakeAction = IntakeAction.NONE;
    elevatorAction = ElevatorAction.NONE;
    shooterAction = ShooterAction.NONE;
    shooterSpeed = Optional.empty();
  }
}
