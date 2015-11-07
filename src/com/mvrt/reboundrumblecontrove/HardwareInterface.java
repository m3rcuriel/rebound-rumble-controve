package com.mvrt.reboundrumblecontrove;

import com.kauailabs.navx.frc.AHRS;
import com.mvrt.reboundrumblecontrove.subsystem.DriveBase;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;

public class HardwareInterface {
	public static DriveBase kDrive = new DriveBase();

	public static AHRS kGyro = new AHRS(SPI.Port.kMXP);

	public static Joystick kDriverJoystick = new Joystick(Constants.kDriverJoystick);
}
