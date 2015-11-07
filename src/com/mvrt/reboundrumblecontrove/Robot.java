
package com.mvrt.reboundrumblecontrove;

import com.m3rcuriel.controve.controllers.util.DriveOutput;
import com.m3rcuriel.controve.retrievable.SystemManager;
import com.mvrt.reboundrumblecontrove.statemachine.StateController;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Robot extends IterativeRobot {

  private ScheduledExecutorService controlLooper = Executors.newScheduledThreadPool(4);
  private ScheduledExecutorService slowLooper = Executors.newScheduledThreadPool(2);

  private StateController stateController = new StateController();
  private OperatorInterface operatorInterface = new OperatorInterface();
  private DriveSystem driveSystem = new DriveSystem(HardwareInterface.kDrive);

  private Joystick driveJoystick = HardwareInterface.kDriverJoystick;
  private ScheduledFuture<?> shooterThread = null;


  @Override
  public void robotInit() {
    SystemManager.getInstance().add(stateController);
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {
    shooterThread = controlLooper.scheduleWithFixedDelay(HardwareInterface.kShooter, 0, 20,
        TimeUnit.MILLISECONDS);
  }

  @Override
  public void teleopPeriodic() {
    boolean quickTurn = driveJoystick.getTrigger();
    double turn = driveJoystick.getX();

    if (quickTurn) {
      double sign = Math.signum(turn);
      turn = turn * turn * sign;
    }

    driveSystem.drive(driveJoystick.getY(), turn, quickTurn);
    stateController.update(operatorInterface.getCommands());
  }

  @Override
  public void disabledInit() {
    cancelAll();

    HardwareInterface.kShooter.reset();
    HardwareInterface.kDrive.setDriveOutputs(DriveOutput.NEUTRAL);

    HardwareInterface.kDrive.reloadConstants();
    HardwareInterface.kElevator.reloadConstants();
    HardwareInterface.kIntake.reloadConstants();
    HardwareInterface.kShooter.reloadConstants();

    System.gc();
  }

  @Override
  public void testPeriodic() {

  }

  private void cancelAll() {
    if (shooterThread != null) {
      shooterThread.cancel(false);
    }

    shooterThread = null;
  }

}
