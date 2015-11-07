
package com.mvrt.reboundrumblecontrove;

import com.m3rcuriel.controve.controllers.util.DriveOutput;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
    
	private DriveSystem driveSystem = new DriveSystem(HardwareInterface.kDrive);
	
	private Joystick driveJoystick = HardwareInterface.kDriverJoystick;
	
	
    public void robotInit() {

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
    	boolean quickTurn = driveJoystick.getTrigger();
        double turn = driveJoystick.getX();

        if (quickTurn) {
          double sign = Math.signum(turn);
          turn = turn * turn * sign;
        }

        driveSystem.drive(driveJoystick.getY(), turn, quickTurn);
    }
    
    @Override
    public void disabledInit() {
    	HardwareInterface.kDrive.setDriveOutputs(DriveOutput.NEUTRAL);
    	
    	HardwareInterface.kDrive.reloadConstants();
    }
    
    public void testPeriodic() {
    
    }
    
}
