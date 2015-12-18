package com.m3rcuriel.controve.api;

import com.m3rcuriel.controve.components.Clock;
import com.m3rcuriel.controve.util.Metronome;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.LongConsumer;

/**
 * A class to invoke registered {@link Executable}s on a fixed period using a metronome.
 *
 * @author Lee Mracek
 */

public final class Conductor {

  private final String name;
  private final Clock timeSystem;
  private final Metronome met;
  private final Iterable<Executable> executables;
  private final AtomicReference<Thread> thread = new AtomicReference<>();
  private final LongConsumer delayInformer;
  private volatile boolean running = false;
  private volatile CountDownLatch stopped = null;

  public Conductor(String name, Iterable<Executable> executables, Clock timeSystem, Metronome metronome,
      LongConsumer delayInformer) {
    this.name = name;
    this.timeSystem = timeSystem;
    this.met = metronome;
    this.executables = executables;
    this.delayInformer = delayInformer != null ? delayInformer : Conductor::noDelay;
  }

  /**
   * Start the execution of the {@link Conductor} in a seperate thread. During each loop, all
   * registered {@link Executable}s will be called in the order that they were registered.
   * <p>
   * Calling this method when already started does nothing.
   *
   * @see #stop()
   */
  public void start() {
    thread.getAndUpdate(thread -> {
      if (thread == null) {
        thread = new Thread(this::run);
        thread.setName(name);
        thread.setPriority(8);
        stopped = new CountDownLatch(1);
        running = true;
        thread.start();
      }
      return thread;
    });
  }

  /**
   * Stop this conductor from executing, and block until the thread has completed all work (or the
   * timeout occurs).
   * <p>
   * Calling this method while already stopped has no effect.
   */
  public void stop() {
    // get a latch to wait for the thread to complete
    CountDownLatch latch = stopped;

    // mark thread as complete
    Thread oldThread = thread.getAndUpdate(thread -> {
      if (thread != null) {
        running = false;
      }
      return null;
    });
    if (oldThread != null && latch != null) {
      try {
        // wait at most 10 seconds for the latch to countdown
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.interrupted();
      }
    }
  }

  private void run() {
    try {
      long timeInMillis = 0L;
      long lastTimeInMillis = 0L;
      while (true) {
        timeInMillis = timeSystem.currentTimeInMillis(); // get current time
        delayInformer.accept(timeInMillis - lastTimeInMillis); // pass delay to the delay informer
        for (Executable executable : executables) {
          if (!running) {
            return;
          }
          try {
            executable.execute(timeInMillis); // iterate over each executable
          } catch (Throwable e) {
            // TODO Add handling
          }
          lastTimeInMillis = timeInMillis;
        }
        met.pause(); // wait for the loop to complete
      }
    } finally {
      CountDownLatch latch = stopped;
      latch.countDown();
    }
  }

  // "null" delay informer
  private static void noDelay(long delay) {
    // do nothing
  }
}
