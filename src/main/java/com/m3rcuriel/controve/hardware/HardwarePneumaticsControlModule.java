package com.m3rcuriel.controve.hardware;

import com.m3rcuriel.controve.components.CurrentSensor;
import com.m3rcuriel.controve.components.PneumaticsControlModule;
import com.m3rcuriel.controve.components.Relay;
import com.m3rcuriel.controve.components.Switch;
import edu.wpi.first.wpilibj.Compressor;

/**
 * A hardware representation of a Pneumatics Control Module.
 *
 * Wraps around WPILib to fit to {@link PneumaticsControlModule}
 *
 * @author Lee Mracek
 */
public class HardwarePneumaticsControlModule implements PneumaticsControlModule {
  private final Compressor pcm;
  private final Relay closedLoop;
  private final Faults instantaneousFaults;
  private final Faults stickyFaults;

  /**
   * Construct a new HardwarePneumaticsControlModule for a given compressor.
   *
   * @param pcm the compressor to use
   */
  HardwarePneumaticsControlModule(Compressor pcm) {
    this.pcm = pcm;
    this.closedLoop =
        Relay.instantaneous(this.pcm::setClosedLoopControl, this.pcm::getClosedLoopControl);
    this.instantaneousFaults = new Faults() {
      @Override
      public Switch notConnected() {
        return pcm::getCompressorNotConnectedFault;
      }

      @Override
      public Switch currentTooHigh() {
        return pcm::getCompressorCurrentTooHighFault;
      }

      @Override
      public Switch shorted() {
        return pcm::getCompressorShortedFault;
      }
    };

    this.stickyFaults = new Faults() {
      @Override
      public Switch notConnected() {
        return pcm::getCompressorNotConnectedStickyFault;
      }

      @Override
      public Switch currentTooHigh() {
        return pcm::getCompressorCurrentTooHighStickyFault;
      }

      @Override
      public Switch shorted() {
        return pcm::getCompressorShortedStickyFault;
      }
    };
  }

  /**
   * Get the sensor used to retrieve current.
   *
   * @return returns a {@link CurrentSensor} object used to get the current
   */
  @Override
  public CurrentSensor compressorCurrent() {
    return pcm::getCompressorCurrent;
  }

  /**
   * Retrieve a switch indicating the state of the compressor.
   *
   * @return a switch indicating if the compressor is running
   */
  @Override
  public Switch compressorRunningSwitch() {
    return pcm::enabled;
  }

  /**
   * Retrieve a relay allowing one to set the mode of the compressor.
   *
   * @return the closed loop relay
   */
  @Override
  public Relay automaticMode() {
    return closedLoop;
  }

  /**
   * Retrieve a switch indicating whether or not the compressor is low in pressure.
   *
   * @return the switch indicating the pressure
   */
  @Override
  public Switch lowPressureSwitch() {
    return pcm::getPressureSwitchValue;
  }

  /**
   * Retrieve a {@link com.m3rcuriel.controve.components.PneumaticsControlModule.Faults} object
   * containing the instantaneous faults of the compressor.
   *
   * @return the instantaneous faults
   */
  @Override
  public Faults compressorFaults() {
    return instantaneousFaults;
  }

  /**
   * Retrieve a {@link com.m3rcuriel.controve.components.PneumaticsControlModule.Faults} object
   * containing the stick faults of the module.
   *
   * @return the sticky faults
   */
  @Override
  public Faults compressorStickyFaults() {
    return stickyFaults;
  }

  /**
   * Clear all sticky faults.
   *
   * @return this module for method chaining
   */
  @Override
  public HardwarePneumaticsControlModule clearStickyFaults() {
    pcm.clearAllPCMStickyFaults();
    return this;
  }
}
