package com.m3rcuriel.controve.components;

/**
 * An abstract representation of the Pneumatics Control Module on the robot.
 *
 * @author Lee Mracek
 */
public interface PneumaticsControlModule {

  /**
   * Return an instance of CurrentSensor which measures the current of the compressor.
   *
   * @return a CurrentSensor representing the current.
   */
  public CurrentSensor compressorCurrent();

  /**
   * An instance of {@link Switch} representing whether or not the compressor is running.
   *
   * @return the {@link Switch}
   */
  public Switch compressorRunningSwitch();

  /**
   * An instance of {@link Switch} representing whether or not the compressor is on low pressure.
   *
   * @return the {@link Switch}
   */
  public Switch lowPressureSwitch();

  /**
   * An instance of {@link Relay} allowing the user to control whether or not the compressor will
   * follow WPILib closed loop control.
   *
   * @return the {@link Relay}
   */
  public Relay automaticMode();

  /**
   * An instance of {@link Faults} representing the current instantaneous faults of the compressor.
   *
   * @return the representation of the compressor faults.
   */
  public Faults compressorFaults();

  /**
   * An instance of {@link Faults} representing the current sticky faults of the compressor.
   *
   * @return the representation of the compressor faults.
   */
  public Faults compressorStickyFaults();

  /**
   * Clear the sticky faults of the PCM.
   *
   * @return this PneumaticsControlModule for method chaining.
   */
  public PneumaticsControlModule clearStickyFaults();

  /**
   * A representation of the possible faults on a PCM.
   */
  public static interface Faults {
    /**
     * A {@link Switch} representing if the compressor is faulting because it is not connected.
     *
     * @return the {@link Switch}
     */
    Switch notConnected();

    /**
     * A {@link Switch} representing if the compressor is faulting because it is drawing too much current.
     *
     * @return the {@link Switch}
     */
    Switch currentTooHigh();

    /**
     * A {@link Switch} representing if the compressor is faulting because it is shorting.
     *
     * @return the {@link Switch}
     */
    Switch shorted();
  }

}
