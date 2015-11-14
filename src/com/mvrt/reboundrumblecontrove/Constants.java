package com.mvrt.reboundrumblecontrove;

import com.mvrt.lib.ConstantsBase;

public class Constants extends ConstantsBase {

  public static double kDriveSensitivity = .75;

  public static double kShooterP = 0.01;
  public static double kShooterI = 0.0;
  public static double kShooterD = 0.0;

  public static double kIntakeSpeed = 1.0;

  public static double kShooterKeySpeed = 4000; // rpm
  public static double kShooterSpeedShift = 0.1; // delta rpm

  public static double kElevatorSpeed = 0.8;

  public static int kEndEditableArea = 0;

  public static int kShooterEncoderA = 0;
  public static int kShooterEncoderB = 1;

  public static int kRightDriveFront = 5;
  public static int kRightDriveRear = 6;
  public static int kLeftDriveFront = 0;
  public static int kLeftDriveRear = 1;

  public static int kIntake = 7;
  public static int kElevator = 4;
  public static int kShooter = 3;

  public static int kDriverJoystick = 0;

  @Override
  public String getFileLocation() {
    return "~/constants.txt";
  }

}
