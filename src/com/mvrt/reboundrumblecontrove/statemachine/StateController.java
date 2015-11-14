package com.mvrt.reboundrumblecontrove.statemachine;

import com.m3rcuriel.controve.api.Routine;
import com.m3rcuriel.controve.api.StateControllerBase;
import com.m3rcuriel.controve.retrievable.Retrievable;
import com.m3rcuriel.controve.retrievable.StateHolder;
import com.mvrt.reboundrumblecontrove.Constants;
import com.mvrt.reboundrumblecontrove.HardwareInterface;
import com.mvrt.reboundrumblecontrove.routines.ManualRoutine;

public class StateController extends StateControllerBase<Commands, RobotSetpoints>
    implements Retrievable {

  private ManualRoutine manualRoutine = new ManualRoutine();
  private Routine<Commands, RobotSetpoints> currentRoutine = null;
  
  public StateController() {
    setpoints = new RobotSetpoints();
    setpoints.reset();
  }

  private void setNewRoutine(Routine<Commands, RobotSetpoints> newRoutine) {
    boolean needsCancel = newRoutine != currentRoutine && currentRoutine != null;

    boolean needsReset = newRoutine != currentRoutine && newRoutine != null;
    if (needsCancel) {
      currentRoutine.cancel();
    }
    currentRoutine = newRoutine;
    if (needsReset) {
      currentRoutine.reset();
    }
  }

  @Override
  public void update(Commands commands) {
    setpoints.reset();

    if (currentRoutine != null && currentRoutine.isFinished()) {
      setNewRoutine(null);
    }

    if (currentRoutine != null) {
      setpoints = currentRoutine.update(commands, setpoints);
    }

    setpoints = manualRoutine.update(commands, setpoints);

    // elevator actions

    if (setpoints.elevatorAction == RobotSetpoints.ElevatorAction.UP) {
      HardwareInterface.kElevator.setSpeed(Constants.kElevatorSpeed);
    } else if (setpoints.elevatorAction == RobotSetpoints.ElevatorAction.DOWN) {
      HardwareInterface.kElevator.setSpeed(Constants.kElevatorSpeed * -1);
    } else {
      HardwareInterface.kElevator.setSpeed(0);
    }

    // intake actions

    if (setpoints.intakeAction == RobotSetpoints.IntakeAction.IN) {
      HardwareInterface.kIntake.setSpeed(-Constants.kIntakeSpeed * -1);
    } else if (setpoints.intakeAction == RobotSetpoints.IntakeAction.OUT) {
      HardwareInterface.kIntake.setSpeed(Constants.kIntakeSpeed);
    } else {
      HardwareInterface.kIntake.setSpeed(0);
    }

    // shooter actions

    if (setpoints.shooterSpeed.isPresent()) {
      HardwareInterface.kShooter.setSpeed(setpoints.shooterSpeed.orElse(0.0));
    }
  }

  @Override
  public void getState(StateHolder states) {
    states.put("mode", currentRoutine != null ? currentRoutine.getName() : "---");
  }

  @Override
  public String getName() {
    return "state controller";
  }
}
