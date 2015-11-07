package com.mvrt.reboundrumblecontrove;

import com.m3rcuriel.controve.api.ConstantsBase;

public class Constants extends ConstantsBase {

	public static double kDriveSensitivity = .75;

	public static int kEndEditableArea = 0;

	public static int kRightDriveFront = 5;
	public static int kRightDriveRear = 6;
	public static int kLeftDriveFront = 0;
	public static int kLeftDriveRear = 1;
	
	public static int kDriverJoystick = 0;

	@Override
	public String getFileLocation() {
		return "~/constants.txt";
	}

}
