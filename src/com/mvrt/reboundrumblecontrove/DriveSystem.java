package com.mvrt.reboundrumblecontrove;

import com.m3rcuriel.controve.controllers.util.DriveOutput;
import com.mvrt.reboundrumblecontrove.subsystem.DriveBase;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveSystem {
  private DriveBase drive;
  private double oldWheel;
  private double quickStopAccumulator;
  private double negInertiaAccumulator = 0;
  private double throttleDeadband = 0.02;
  private double wheelDeadband = 0.02;

  public DriveSystem(DriveBase drive) {
    this.drive = drive;
  }

  public void drive(double throttle, double wheel, boolean quickTurn) {
    if (DriverStation.getInstance().isAutonomous()) {
      return;
    }

    wheel = handleDeadband(wheel, wheelDeadband);
    throttle = handleDeadband(throttle, throttleDeadband);

    double negativeInertia = wheel - oldWheel;

    oldWheel = wheel;

    double logScale = 0.6;

    wheel = Math.sin(Math.PI / 2.0 * logScale * wheel) / Math.sin(Math.PI / 2.0 * logScale);
    wheel = Math.sin(Math.PI / 2.0 * logScale * wheel) / Math.sin(Math.PI / 2.0 * logScale);

    double leftPwm, rightPwm, overPower;
    double sensitivity;

    double angularPower;
    double linearPower;

    // Negative inertia!
    double negInertiaScalar;
    if (wheel * negativeInertia > 0) {
      negInertiaScalar = 2.5;
    } else {
      if (Math.abs(wheel) > 0.65) {
        negInertiaScalar = 5.0;
      } else {
        negInertiaScalar = 3.0;
      }
    }
    sensitivity = Constants.kDriveSensitivity;

    double negInertiaPower = negativeInertia * negInertiaScalar;
    negInertiaAccumulator += negInertiaPower;

    wheel = wheel + negInertiaAccumulator;
    if (negInertiaAccumulator > 1) {
      negInertiaAccumulator -= 1;
    } else if (negInertiaAccumulator < -1) {
      negInertiaAccumulator += 1;
    } else {
      negInertiaAccumulator = 0;
    }

    linearPower = throttle;

    if (quickTurn) {
      if (Math.abs(linearPower) < 0.2) {
        double alpha = 0.1;
        quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * limit(wheel, 1.0) * 5;
      }
      overPower = 1.0;
      sensitivity = 1.0;
      angularPower = wheel;
    } else {
      overPower = 0.0;
      angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
      if (quickStopAccumulator > 1) {
        quickStopAccumulator -= 1;
      } else if (quickStopAccumulator < -1) {
        quickStopAccumulator += 1;
      } else {
        quickStopAccumulator = 0.0;
      }
    }

    rightPwm = leftPwm = linearPower;
    leftPwm += angularPower;
    rightPwm -= angularPower;

    if (leftPwm > 1.0) {
      rightPwm -= overPower * (leftPwm - 1.0);
      leftPwm = 1.0;
    } else if (rightPwm > 1.0) {
      leftPwm -= overPower * (rightPwm - 1.0);
      rightPwm = 1.0;
    } else if (leftPwm < -1.0) {
      rightPwm += overPower * (-1.0 - leftPwm);
      leftPwm = -1.0;
    } else if (rightPwm < -1.0) {
      leftPwm += overPower * (-1.0 - rightPwm);
      rightPwm = -1.0;
    }

    DriveOutput output = new DriveOutput(leftPwm, rightPwm);
    drive.setDriveOutputs(output);
  }

  public double handleDeadband(double val, double deadband) {
    return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
  }

  public static double limit(double val, double limit) {
    return (Math.abs(val) < limit) ? val : limit * (val < 0 ? -1 : 1);
  }
}
