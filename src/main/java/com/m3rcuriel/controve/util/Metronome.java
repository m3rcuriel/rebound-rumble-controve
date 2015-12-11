package com.m3rcuriel.controve.util;

import com.m3rcuriel.controve.components.Clock;

import java.util.concurrent.TimeUnit;

/**
 * @author Lee Mracek
 */
public interface Metronome {

  /**
   * Pause until the next tick of the metronome.
   *
   * @return true if the pause completed successfully
   */
  public boolean pause();

  /**
   * Spawns a new metronome that starts ticking immediately using a busy loop to keep the thread
   * active.
   * <p>
   * Theoretically this is the most accurate method for ticking, but it may take more system load.
   *
   * @param period     the period of time the metronome ticks and for which the {@link #pause()}
   *                   waits.
   * @param unit       unit the unit of time, may not be null
   * @param timeSystem the time system which provides the current time (system, fpga, etc)
   * @return the constructed Metronome instance
   */
  public static Metronome metronome(long period, TimeUnit unit, Clock timeSystem) {
    long periodInNanos = unit.toNanos(period);

    return new Metronome() {

      private long next = timeSystem.currentTimeInNanos() + periodInNanos;

      @Override
      public boolean pause() {
        while (next - timeSystem.currentTimeInNanos() > 0) {
        }
        next = next + periodInNanos;
        return false;
      }

      @Override
      public String toString() {
        return "Metronome (busy period " + TimeUnit.NANOSECONDS.toMillis(periodInNanos) + " ms)";
      }
    };
  }
}
