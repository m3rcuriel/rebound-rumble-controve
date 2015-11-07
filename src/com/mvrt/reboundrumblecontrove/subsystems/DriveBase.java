package com.mvrt.reboundrumblecontrove.subsystems;

import com.m3rcuriel.controve.api.Subsystem;
import com.m3rcuriel.controve.controllers.util.DriveOutput;
import com.m3rcuriel.controve.retrievable.StateHolder;
import com.mvrt.reboundrumblecontrove.Constants;

import edu.wpi.first.wpilibj.Talon;

public class DriveBase extends Subsystem {

  private Talon leftDriveBaseFront;
  private Talon leftDriveBaseRear;

  private Talon rightDriveBaseFront;
  private Talon rightDriveBaseRear;

  public DriveBase() {
    super("DriveBase");

    leftDriveBaseFront = new Talon(Constants.kLeftDriveFront);
    leftDriveBaseRear = new Talon(Constants.kLeftDriveRear);
    rightDriveBaseFront = new Talon(Constants.kRightDriveFront);
    rightDriveBaseRear = new Talon(Constants.kRightDriveRear);
  }

  public void setDriveOutputs(DriveOutput output) {
    leftDriveBaseFront.set(output.leftMotors);
    leftDriveBaseRear.set(output.leftMotors);
    rightDriveBaseFront.set(-output.rightMotors);
    rightDriveBaseRear.set(-output.rightMotors);
  }

  @Override
  public void getState(StateHolder states) {

  }

  @Override
  public void reloadConstants() {
    // nothing to do here
  }

}
