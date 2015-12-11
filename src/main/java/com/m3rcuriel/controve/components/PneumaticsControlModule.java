package com.m3rcuriel.controve.components;

/**
 * Created by lee on 12/10/15.
 */
public interface PneumaticsControlModule {

  public CurrentSensor compressorCurrent();

  public Switch compressorRunningSwitch();

  public Switch lowPressureSwitch();

  public Relay automaticMode();

  public Faults compressorFaults();

  public Faults compressorStickyFaults();

  public PneumaticsControlModule clearStickyFaults();

  public static interface Faults {
    Switch notConnected();

    Switch currentTooHigh();

    Switch shorted();
  }

}
