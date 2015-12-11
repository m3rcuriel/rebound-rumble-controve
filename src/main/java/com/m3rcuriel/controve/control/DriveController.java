package com.m3rcuriel.controve.control;

import com.m3rcuriel.controve.control.misc.DriveOutput;
import com.m3rcuriel.controve.control.misc.Motion;

/**
 * A general controller for the drive train
 *
 * @author Lee Mracek
 */
public abstract class DriveController extends Controller {
  /**
   * Update the controller to retrieve a DriveOutput.
   *
   * @param motion the current state of the drivetrain.
   * @return the desired left-right output.
   */
  public abstract DriveOutput update(Motion motion);

  /**
   * Retrieve the current {@link Motion} of the drivetrain.
   *
   * @return the current state of the drivetrain
   */
  public abstract Motion getCurrentSetpoint();

  /**
   * Determines if the controller is on target.
   *
   * @return true if the controller is on target.
   */
  @Override
  public abstract boolean isOnTarget();
}
