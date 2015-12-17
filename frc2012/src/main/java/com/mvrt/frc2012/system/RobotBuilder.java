package com.mvrt.frc2012.system;

import com.m3rcuriel.controve.DriveInterpreter;
import com.m3rcuriel.controve.components.DriveTrain;
import com.m3rcuriel.controve.components.Motor;
import com.m3rcuriel.controve.components.PowerDistributionPanel;
import com.m3rcuriel.controve.components.SimpleAccumulatedSensor;
import com.m3rcuriel.controve.hardware.Hardware;
import com.m3rcuriel.controve.hardware.Hardware.HumanInterfaceDevices;
import com.m3rcuriel.controve.hardware.Hardware.Motors;
import com.mvrt.frc2012.Constants;
import com.mvrt.frc2012.OperatorInterface;

/**
 * Created by lee on 12/16/15.
 */
public class RobotBuilder {
  public static Robot buildRobot() {
    Components components = new Components();

    PowerDistributionPanel powerDistributionPanel = components.powerDistributionPanel;

    DriveInterpreter drive = new DriveInterpreter(DriveTrain.create(
        Motor.compose(components.leftFront, components.leftRear),
        Motor.compose(components.rightFront, components.rightRear).invert()
    ));

    // build more subsystems

    OperatorInterface operator = new OperatorInterface(HumanInterfaceDevices.logitechAttack3d(0));

    return new Robot(drive, powerDistributionPanel, components, operator);
  }

  public static final class Components {
    public final Motor leftFront = Motors.talon(Constants.kLeftDriveFront);
    public final Motor leftRear = Motors.talon(Constants.kLeftDriveRear);
    public final Motor rightFront = Motors.talon(Constants.kRightDriveFront);
    public final Motor rightRear = Motors.talon(Constants.kRightDriveRear);

    public final Motor shooter = Motors.talon(Constants.kShooter);
    public final SimpleAccumulatedSensor shooterEncoder = Hardware.AccumulatedSensors.encoder(
        Constants.kShooterEncoderA, Constants.kShooterEncoderB, Constants.kShooterDistancePerPulse);

    public final Motor intake = Motors.talon(Constants.kIntake);
    public final Motor elevator = Motors.talon(Constants.kElevator);

    public final PowerDistributionPanel powerDistributionPanel = Hardware.powerDistributionPanel();
  }
}
