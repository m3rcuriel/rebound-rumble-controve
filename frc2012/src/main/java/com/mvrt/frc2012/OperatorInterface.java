package com.mvrt.frc2012;

import com.m3rcuriel.controve.components.Switch;
import com.m3rcuriel.controve.components.oi.ContinuousRange;
import com.m3rcuriel.controve.components.oi.FlightStick;

/**
 * @author Lee Mracek
 */
public class OperatorInterface {
  public final ContinuousRange wheel;
  public final ContinuousRange throttle;

  public final Switch quickTurn;

  public OperatorInterface(FlightStick driverJoystick) {
    throttle = driverJoystick.getPitch();
    wheel = driverJoystick.getYaw();

    quickTurn = driverJoystick.getButton(5);
  }
}
