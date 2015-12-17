package com.mvrt.frc2012.system;

import com.m3rcuriel.controve.DriveInterpreter;
import com.m3rcuriel.controve.components.PowerDistributionPanel;
import com.mvrt.frc2012.OperatorInterface;
import com.mvrt.frc2012.system.RobotBuilder.Components;

/**
 * @author Lee Mracek
 */
public class Robot {
  public final DriveInterpreter drive;
  public PowerDistributionPanel powerDistributionPanel;
  public final Components components;
  public final OperatorInterface operator;

  public Robot(DriveInterpreter drive, PowerDistributionPanel powerDistributionPanel,
      Components components, OperatorInterface operator) {
    this.drive = drive;
    this.powerDistributionPanel = powerDistributionPanel;
    this.components = components;
    this.operator = operator;
  }
}
