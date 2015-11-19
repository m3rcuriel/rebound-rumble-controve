package com.m3rcuriel.controve.api;

public abstract class SensorThread implements Runnable {
  private static long SENSOR_DELAY_MS = 100;

  protected volatile boolean volatileHasData = false;

  @Override
  public abstract void run();

  public boolean hasData() {
    return volatileHasData;
  }

  public long getSensorPeriod() {
    return SENSOR_DELAY_MS;
  }
}
