package com.m3rcuriel.controve.components;

import com.m3rcuriel.controve.ControveRequirementException;
import edu.wpi.first.wpilibj.Utility;

/**
 * A generalized way to measure current time with generators for system and fpga.
 *
 * @author Lee Mracek
 */
public interface Clock {
  /**
   * Get the current clock time in microseconds.
   *
   * @return the long representation of time in microseconds
   */
  public long currentTimeInMicros();

  /**
   * Get the current clock time in nanoseconds.
   *
   * @return the long representation of time in nanoseconds
   */
  public default long currentTimeInNanos() {
    return currentTimeInMicros() * 1000;
  }

  /**
   * Get the current clock time in milliseconds.
   *
   * @return the long representation of time in milliseconds
   */
  public default long currentTimeInMillis() {
    return (long) (currentTimeInMicros() / 1000.0);
  }

  /**
   * Generator method to create a clock from the FPGA.
   *
   * @return the Clock representing the FPGA
   */
  public static Clock fpga() {
    try {
      Utility.getFPGATime();
      // if we get here, no exception; there is FPGA
      return Utility::getFPGATime;
    } catch (UnsatisfiedLinkError | NoClassDefFoundError e) {
      // no fpga :c
      throw new ControveRequirementException("Missing FPGA hardware/software", e);
    }
  }

  /**
   * Generator method to create a clock from the system time.
   *
   * @return the Clock representing system time
   */
  public static Clock system() {
    return new Clock() {
      @Override
      public long currentTimeInMicros() {
        return (long) (currentTimeInNanos() / 1000.0);
      }

      @Override
      public long currentTimeInNanos() {
        return System.nanoTime();
      }

      @Override
      public long currentTimeInMillis() {
        return System.currentTimeMillis();
      }
    };
  }

  /**
   * Attempts to create a clock from the FPGA. If there is no FPGA, it will fall back on the system
   * clock.
   *
   * @return the Clock representing the FPGA, or as a fallback, the system time.
   */
  public static Clock fpgaOrSystem() {
    try {
      return fpga();
    } catch (ControveRequirementException e) {
      return system();
    }
  }
}
