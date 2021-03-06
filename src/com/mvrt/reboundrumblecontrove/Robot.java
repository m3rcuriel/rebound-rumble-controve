package com.mvrt.reboundrumblecontrove;

import com.m3rcuriel.controve.controllers.util.DriveOutput;
import com.m3rcuriel.controve.retrievable.SystemManager;
import com.mvrt.reboundrumblecontrove.statemachine.StateController;
import com.mvrt.reboundrumblecontrove.web.WebServer;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Robot extends IterativeRobot {

  private ScheduledExecutorService controlLooper = Executors.newScheduledThreadPool(4);
  private ScheduledExecutorService lightLooper = Executors.newScheduledThreadPool(2);

  private StateController stateController = new StateController();
  private OperatorInterface operatorInterface = new OperatorInterface();
  private DriveSystem driveSystem = new DriveSystem(HardwareInterface.kDrive);

  private Joystick driveJoystick = HardwareInterface.kDriverJoystick;
  private ScheduledFuture<?> shooterThread = null;

  public enum RobotState {
    DISABLED, AUTONOMOUS, TELEOP;
  }

  public static RobotState robotState = RobotState.DISABLED;

  public static RobotState getState() {
    return robotState;
  }

  public static void setState(RobotState state) {
    robotState = state;
  }

  @Override
  public void robotInit() {
    SystemManager.getInstance().add(stateController);
    WebServer.startServer();
    lightLooper.scheduleAtFixedRate(new LedController("10.1.15.16", 5803), 0, 10,
        TimeUnit.MILLISECONDS);
  }

  @Override
  public void autonomousInit() {
    setState(RobotState.AUTONOMOUS);
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {
    setState(RobotState.TELEOP);
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
    setState(RobotState.DISABLED);

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
