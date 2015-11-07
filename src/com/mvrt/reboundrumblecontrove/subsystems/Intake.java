package com.mvrt.reboundrumblecontrove.subsystems;

import com.m3rcuriel.controve.api.Subsystem;
import com.m3rcuriel.controve.retrievable.StateHolder;
import com.mvrt.reboundrumblecontrove.Constants;
import com.mvrt.reboundrumblecontrove.HardwareInterface;

import edu.wpi.first.wpilibj.Talon;

public class Intake extends Subsystem {

  private Talon intake = new Talon(Constants.kIntake);

  public Intake() {
    super("Intake");
  }

  public void setSpeed(double speed) {
    intake.set(speed);
  }

  @Override
  public void getState(StateHolder states) {
    states.put("voltage", HardwareInterface.kPdp.getVoltage() * intake.getSpeed());
    states.put("speed", intake.getSpeed());
  }

  @Override
  public void reloadConstants() {}
}
