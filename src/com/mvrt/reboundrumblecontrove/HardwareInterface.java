package com.mvrt.reboundrumblecontrove;

import com.kauailabs.navx.frc.AHRS;
import com.mvrt.reboundrumblecontrove.subsystems.DriveBase;
import com.mvrt.reboundrumblecontrove.subsystems.Elevator;
import com.mvrt.reboundrumblecontrove.subsystems.Intake;
import com.mvrt.reboundrumblecontrove.subsystems.Shooter;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;

public class HardwareInterface {
  public static PowerDistributionPanel kPdp = new PowerDistributionPanel();
  public static DriveBase kDrive = new DriveBase();
  public static Elevator kElevator = new Elevator();
  public static Intake kIntake = new Intake();
  public static Shooter kShooter = new Shooter();

  public static AHRS kGyro = new AHRS(SPI.Port.kMXP);

  public static Joystick kDriverJoystick = new Joystick(Constants.kDriverJoystick);
}
