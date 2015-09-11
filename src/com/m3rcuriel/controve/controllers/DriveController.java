package com.m3rcuriel.controve.controllers;

import com.m3rcuriel.controve.controllers.util.DriveOutput;
import com.m3rcuriel.controve.controllers.util.Motion;

public abstract class DriveController {
  public abstract DriveOutput update(Motion motion);

  public abstract Motion getCurrentSetpoint();

  public abstract boolean onTarget();
}
