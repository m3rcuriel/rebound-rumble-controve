package com.m3rcuriel.controve.sim.fake;

import com.m3rcuriel.controve.components.Clock;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lee on 12/10/15.
 */
public class FakeClock implements Clock {
  private final AtomicLong ticker = new AtomicLong(1000 * 1000 * 20); // 2 second

  public FakeClock() {

  }

  public FakeClock incrementBySeconds(long seconds) {
    return incrementByMicroseconds(1000 * 1000 * seconds);
  }

  public FakeClock incrementbyMilliseconds(long milliseconds) {
    return incrementByMicroseconds(1000 * milliseconds);
  }

  public FakeClock incrementByMicroseconds(long microseconds) {
    if (microseconds < 1) {
      throw new IllegalArgumentException("The clock increment must be positive");
    }
    ticker.accumulateAndGet(microseconds, (a, b) -> a + b);
    return this;
  }

  @Override
  public long currentTimeInMicros() {
    return ticker.get();
  }
}
