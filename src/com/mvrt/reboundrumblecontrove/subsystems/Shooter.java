package com.mvrt.reboundrumblecontrove.subsystems;

import com.m3rcuriel.controve.api.Subsystem;
import com.m3rcuriel.controve.misc.SynchronousPid;
import com.m3rcuriel.controve.retrievable.StateHolder;
import com.mvrt.reboundrumblecontrove.Constants;
import com.mvrt.reboundrumblecontrove.HardwareInterface;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class Shooter extends Subsystem implements Runnable {

  private SynchronousPid speedPid;

  private Talon shooter = new Talon(Constants.kShooter);
  private Encoder encoder = new Encoder(Constants.kShooterEncoderA, Constants.kShooterEncoderB);

  private static final double IN_PER_PULSE = 0; // TODO add real value

  public Shooter() {
    super("Shooter");

    speedPid = new SynchronousPid(Constants.kShooterP, Constants.kShooterI, Constants.kShooterD);
    speedPid.setOutputRange(-1, 1);

    encoder.setDistancePerPulse(IN_PER_PULSE);
  }

  @Override
  public void getState(StateHolder states) {
    states.put("voltage", shooter.getSpeed() * HardwareInterface.kPdp.getVoltage());
    states.put("output speed", shooter.getSpeed());
    states.put("encoder speed", encoder.getRate());
    states.put("pid output", speedPid.get());
  }

  public void setSpeed(double setpoint) {
    speedPid.setSetpoint(setpoint);
  }

  public void setOutput(double output) {
    shooter.set(output);
  }

  public void reset() {
    shooter.set(0);
    speedPid.reset();
  }

  @Override
  public void run() {
    speedPid.calculate(encoder.getRate());
    setOutput(speedPid.get());
  }

  @Override
  public void reloadConstants() {
    speedPid = new SynchronousPid(Constants.kShooterP, Constants.kShooterI, Constants.kShooterD);
  }
}
