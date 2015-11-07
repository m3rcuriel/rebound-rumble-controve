package com.mvrt.reboundrumblecontrove.subsystems;

import com.m3rcuriel.controve.api.Subsystem;
import com.m3rcuriel.controve.retrievable.StateHolder;
import com.mvrt.reboundrumblecontrove.Constants;
import com.mvrt.reboundrumblecontrove.HardwareInterface;

import edu.wpi.first.wpilibj.Talon;

public class Elevator extends Subsystem {

  private Talon elevator = new Talon(Constants.kElevator);

  public Elevator() {
    super("Elevator");
  }

  public void setSpeed(double speed) {
    elevator.set(speed);
  }

  @Override
  public void getState(StateHolder states) {
    states.put("voltage", HardwareInterface.kPdp.getVoltage() * elevator.getSpeed());
    states.put("speed", elevator.getSpeed());
  }

  @Override
  public void reloadConstants() {}
}
