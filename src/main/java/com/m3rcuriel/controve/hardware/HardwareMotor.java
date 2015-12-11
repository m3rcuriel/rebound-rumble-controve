package com.m3rcuriel.controve.hardware;

import com.m3rcuriel.controve.components.Motor;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;

import java.util.function.DoubleFunction;

/**
 * A hardware representation of a PWM motor allowing the user to manipuate and retrieve the speed.
 *
 * @author Lee Mracek
 */
final class HardwareMotor implements Motor {
  private final SpeedController controller;
  private final DoubleFunction<Double> speedValidator;

  /**
   * Construct a new HardwareMotor based on a {@link SpeedController} and a method with which to
   * validate the speed.
   *
   * @param controller the physical SpeedController this represents.
   * @param speedValidator the function used to validate a given speed.
   */
  HardwareMotor(SpeedController controller, DoubleFunction<Double> speedValidator) {
    this.controller = controller;
    this.speedValidator = speedValidator;
  }

  /**
   * Set the speed of the SpeedController. Validates using the speed validator.
   *
   * @param speed the desired speed
   * @return this motor for method chaining
   */
  @Override
  public HardwareMotor setSpeed(double speed) {
    controller.set(speedValidator.apply(speed));
    return this;
  }

  /**
   * Retrieve the speed of the SpeedController.
   *
   * @return the current speed of the controller
   */
  @Override
  public double getSpeed() {
    return controller.get();
  }

  /**
   * Retrieve the speed as a raw PWM output (short)
   *
   * @return the short representation of a raw PWM output
   */
  public short getSpeedAsShort() {
    return (short) ((PWM) controller).getRaw();
  }
}
