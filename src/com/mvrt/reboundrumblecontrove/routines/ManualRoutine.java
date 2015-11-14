package com.mvrt.reboundrumblecontrove.routines;

import com.m3rcuriel.controve.api.Routine;
import com.mvrt.reboundrumblecontrove.Constants;
import com.mvrt.reboundrumblecontrove.statemachine.Commands;
import com.mvrt.reboundrumblecontrove.statemachine.RobotSetpoints;

import java.util.Optional;

public class ManualRoutine extends Routine<Commands, RobotSetpoints> {

  @Override
  public void reset() {
    // also nothing to do here
  }

  @Override
  public RobotSetpoints update(Commands commands, RobotSetpoints existing) {
    // use to resolve conflicts between automatic and manual

    if (existing.intakeAction == RobotSetpoints.IntakeAction.NONE) {
      if (commands.intakeRequest == Commands.IntakeRequest.IN) {
        existing.intakeAction = RobotSetpoints.IntakeAction.IN;
      } else if (commands.intakeRequest == Commands.IntakeRequest.OUT) {
        existing.intakeAction = RobotSetpoints.IntakeAction.OUT;
      }
    }

    if (existing.elevatorAction == RobotSetpoints.ElevatorAction.NONE) {
      if (commands.elevatorRequest == Commands.ElevatorRequest.UP) {
        existing.elevatorAction = RobotSetpoints.ElevatorAction.UP;
      } else if (commands.elevatorRequest == Commands.ElevatorRequest.DOWN) {
        existing.elevatorAction = RobotSetpoints.ElevatorAction.DOWN;
      }
    }

    if (existing.shooterAction == RobotSetpoints.ShooterAction.NONE) {
      if (commands.shooterRequest == Commands.ShooterRequest.SHOOTER_KEY) {
        existing.shooterSpeed = Optional.of(Constants.kShooterKeySpeed);
      } else if (commands.shooterRequest == Commands.ShooterRequest.SHOOTER_DECR) {
        existing.shooterSpeed =
            Optional.of(existing.shooterSpeed.orElse(0.0) - Constants.kShooterSpeedShift);
      } else if (commands.shooterRequest == Commands.ShooterRequest.SHOOTER_INCR) {
        existing.shooterSpeed =
            Optional.of(existing.shooterSpeed.orElse(0.0) + Constants.kShooterSpeedShift);
      } else if (commands.shooterRequest == Commands.ShooterRequest.OFF) {
        existing.shooterSpeed = Optional.of(0.0);
      }
    }

    return existing;
  }

  @Override
  public void cancel() {
    // nothing to do here
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public String getName() {
    return "Manual";
  }

}
