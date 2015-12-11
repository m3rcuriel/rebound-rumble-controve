package com.m3rcuriel.controve.components;

import com.m3rcuriel.controve.ControveRequirementException;
import edu.wpi.first.wpilibj.Utility;

/**
 * @author Lee Mracek
 */
public interface Clock {
  public long currentTimeInMicros();

  public default long currentTimeInNanos() {
    return currentTimeInMicros() * 1000;
  }

  public default long currentTimeInMillis() {
    return (long) (currentTimeInMicros() / 1000.0);
  }

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

  public static Clock fpgaOrSystem() {
    try {
      return fpga();
    } catch (ControveRequirementException e) {
      return system();
    }
  }
}
