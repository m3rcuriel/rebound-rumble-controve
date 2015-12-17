package com.m3rcuriel.controve;

import com.m3rcuriel.controve.components.DriveTrain;
import com.m3rcuriel.controve.util.Values;

import java.util.function.DoubleFunction;

/**
 * @author Lee Mracek
 */
public class DriveInterpreter {
  private final DriveTrain driveTrain;

  private double quickStopAccumulator = 0.0;
  private double negativeInertiaAccumulator = 0.0;
  private double oldWheel = 0.0;

  private DoubleFunction<Double> limiter = Values.limiter(1.0, -1.0);

  public DriveInterpreter(DriveTrain driveTrain) {
    this.driveTrain = driveTrain;
  }

  public void stop() {
    this.driveTrain.drive(0.0, 0.0);
  }

  public void arcade(double driveSpeed, double turnSpeed) {
    arcade(driveSpeed, turnSpeed, true);
  }

  public void arcade(double driveSpeed, double turnSpeed, boolean squaredInputs) {
    double leftMotorSpeed;
    double rightMotorSpeed;

    driveSpeed = limiter.apply(driveSpeed);
    turnSpeed = limiter.apply(turnSpeed);

    if (squaredInputs) {
      double driveSig = Math.signum(driveSpeed);
      driveSpeed = driveSig * driveSpeed * driveSpeed;

      double turnSig = Math.signum(turnSpeed);
      turnSpeed = turnSig * turnSpeed * turnSpeed;
    }

    if (driveSpeed > 0.0) {
      if (turnSpeed > 0.0) {
        leftMotorSpeed = driveSpeed - turnSpeed;
        rightMotorSpeed = Math.max(driveSpeed, turnSpeed);
      } else {
        leftMotorSpeed = Math.max(driveSpeed, -turnSpeed);
        rightMotorSpeed = driveSpeed + turnSpeed;
      }
    } else {
      if (turnSpeed > 0.0) {
        leftMotorSpeed = -Math.max(-driveSpeed, turnSpeed);
        rightMotorSpeed = driveSpeed + turnSpeed;
      } else {
        leftMotorSpeed = driveSpeed - turnSpeed;
        rightMotorSpeed = -Math.max(-driveSpeed, -turnSpeed);
      }
    }

    this.driveTrain.drive(leftMotorSpeed, rightMotorSpeed);
  }

  public void tank(double leftSpeed, double rightSpeed, boolean squaredInputs) {
    leftSpeed = limiter.apply(leftSpeed);
    rightSpeed = limiter.apply(rightSpeed);

    if (squaredInputs) {
      double leftSig = Math.signum(leftSpeed);
      leftSpeed = leftSig * leftSpeed * leftSpeed;

      double rightSig = Math.signum(rightSpeed);
      rightSpeed = rightSig * rightSpeed * rightSpeed;
    }

    this.driveTrain.drive(leftSpeed, rightSpeed);
  }

  public void tank(double leftSpeed, double rightSpeed) {
    leftSpeed = limiter.apply(leftSpeed);
    rightSpeed = limiter.apply(rightSpeed);
    this.driveTrain.drive(leftSpeed, rightSpeed);
  }

  private static double dampen(double wheel, double wheelNonLinearity) {
    double factor = Math.PI * wheelNonLinearity;
    return Math.sin(factor * wheel) / Math.sin(factor);
  }

  public void austinDrive(double throttle, double wheel, boolean quickturn) {
    wheel = limiter.apply(wheel);
    throttle = limiter.apply(throttle);

    double negativeInertia = wheel - oldWheel;
    oldWheel = wheel;

    double wheelNonLinearity = 0.6;// tune this

    wheel = dampen(wheel, wheelNonLinearity);
    wheel = dampen(wheel, wheelNonLinearity);

    double leftPwm, rightPwm, overPower;
    double sensitivity;

    double angularPower;
    double linearPower;

    sensitivity = 0.7; // TODO add customization

    double negativeInertiaScalar;
    if (wheel * negativeInertia > 0) {
      negativeInertiaScalar = 2.5;
    } else {
      if (Math.abs(wheel) > 0.65) {
        negativeInertiaScalar = 5.0;
      } else {
        negativeInertiaScalar = 3.0;
      }
    }

    double negativeInertiaPower = negativeInertia * negativeInertiaScalar;
    negativeInertiaAccumulator += negativeInertiaPower;

    wheel += negativeInertiaPower;
    if (negativeInertiaAccumulator > 1) {
      negativeInertiaAccumulator -= 1;
    } else if (negativeInertiaAccumulator < -1) {
      negativeInertiaAccumulator += 1;
    } else {
      negativeInertiaAccumulator = 0;
    }

    linearPower = throttle;

    if (quickturn) {
      if (Math.abs(linearPower) > 0.2) {
        double alpha = 0.1;
        quickStopAccumulator =
            (1 - alpha) * quickStopAccumulator + alpha * Values.limiter(0.0, 1.0).apply(wheel) * 5;
      }
      overPower = 1.0;
      sensitivity = 1.0;
      angularPower = wheel;
    } else {
      overPower = 0.0;
      angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
      if (quickStopAccumulator > 1.0) {
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
    } else if (rightPwm < -1.0) {
      leftPwm -= overPower * (rightPwm - 1.0);
      rightPwm = 1.0;
    } else if (leftPwm < -1.0) {
      rightPwm += overPower * (-1.0 - leftPwm);
      leftPwm = -1.0;
    } else if (rightPwm < -1.0) {
      leftPwm += overPower * (-1.0 - rightPwm);
      rightPwm = -1.0;
    }

    driveTrain.drive(leftPwm, rightPwm);
  }
}
