package com.m3rcuriel.controve;

import com.m3rcuriel.controve.components.Clock;
import com.m3rcuriel.controve.util.Metronome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

/**
 * Access point for higher-level Controve functionality. This class can be used within robot code or
 * unit tests.
 * <p>
 * Created by Lee Mracek on 12/10/15.
 */

public class Controve {

  public static final class Configurator {
    private Supplier<Clock> timeSystemSupplier = Clock::fpgaOrSystem;
    private LongConsumer excessiveExecutorDelayHandler = null;
    private long executionPeriodInNanos = TimeUnit.MILLISECONDS.toNanos(20);
    private boolean initialized = false;

    /**
     * Determine the time using the RoboRIO's FPGA's hardware, or system time if not available.
     *
     * @return this configurator for method chaining
     */
    public Configurator useFpgaTime() {
      timeSystemSupplier = Clock::fpgaOrSystem;
      return this;
    }

    /**
     * Determine the time using the JRE's {@link Clock#system() time system}.
     *
     * @return this configurator for method chaining
     */
    public Configurator useSystemTime() {
      timeSystemSupplier = Clock::system;
      return this;
    }

    /**
     * Determine the time using a custom {@link Clock} implementation.
     *
     * @param clock the custom time system; may not be null.
     * @return this configurator for method chaining
     */
    public Configurator useCustomTime(Clock clock) {
      if (clock == null) {
        throw new IllegalArgumentException("The custom clock may not be null");
      }
      timeSystemSupplier = () -> clock; // supplier based on the clock
      return this;
    }


    /**
     * Configure Controve to use the specified interval in the specified time unit for its system
     * metronome.
     *
     * @param interval the interval for the system to loop on
     * @param unit     the time unit of the interval
     * @return this configurator for method chaining
     */
    public Configurator useExecutionPeriod(long interval, TimeUnit unit) {
      if (interval <= 0) {
        throw new IllegalArgumentException("The execution interval must be positive");
      }
      if (unit == null) {
        throw new IllegalArgumentException("The time unit may not be null");
      }
      if (TimeUnit.MILLISECONDS.toNanos(1) > unit.toNanos(interval)) {
        throw new IllegalArgumentException("The interval must be at least 1 millisecond");
      }
      executionPeriodInNanos = unit.toNanos(interval);
      return this;
    }

    /**
     * Configure a handler to monitor excessive execution periods.
     * <p>
     * This handler will be called every time the {@link Conductor} takes longer than the {@link
     * #useExecutionPeriod(long, TimeUnit) execution period} to execute each interval.
     *
     * @param handler the handler of excessive execution times
     * @return this configurator for method chaining
     */
    public Configurator reportExcessiveExecutionPeriods(LongConsumer handler) {
      excessiveExecutorDelayHandler = handler;
      return this;
    }

    /**
     * Complete the configuration and initialize Controve.
     */
    public synchronized void initialize() {
      if (initialized) {
        System.out.println("Controve has already been initialized...");
      }

      initialized = true;
      INSTANCE = new Controve(this, Controve.INSTANCE);
    }
  }

  /**
   * Start the core Controve functionality. This does nothing if Controve has already been started.
   * This is useful to call in {@code IterativeRobot.robotInit()} to start Controve and being
   * recording events.
   *
   * @see #restart()
   */
  public static void start() {
    INSTANCE.doStart();
  }

  /**
   * Ensure that Controve is {@link #start() started} and, it if was already running, refresh all
   * commands.
   * <p>
   * This would be used in {@code IterativeRobot.teleopInit()} to ensure that Controve is running as
   * well as to cancel autonomous executors.
   *
   * @see #start
   */
  public static void restart() {
    INSTANCE.doRestart();
  }

  public static void shutdown() {
    INSTANCE.doShutdown();
  }

  /**
   * Get the currently used timer for Controve internal actions.
   *
   * @return Controve's time system; never null
   * @see Configurator#useFpgaTime()
   * @see Configurator#useSystemTime()
   * @see Configurator#useCustomTime(Clock)
   */
  public static Clock timeSystem() {
    return INSTANCE.clock;
  }

  /**
   * Fetch the currently counted times when the main Conductor has taken an excessive amount of time
   * to execute a loop.
   *
   * @return the number of excessive executions
   */
  public static long excessiveExecutionTimeCounts() {
    return INSTANCE.conductorDelayCounter.get();
  }

  /**
   * Generates {@link Conductor} for a given {@link Executables} instance for a given period.
   * <p>
   * Uses system time.
   *
   * @param name          the name of the {@link Conductor}
   * @param executables   the {@link Executables} to iterate over. Must not be null.
   * @param periodInNanos the period to loop
   * @return the generated {@link Conductor}
   */
  public static Conductor generateConductor(String name, Executables executables,
      long periodInNanos) {
    if (executables == null || executables.isEmpty()) {
      throw new IllegalArgumentException("Executables cannot be null or empty.");
    }
    return new Conductor(name, executables, CONFIG.timeSystemSupplier.get(),
        Metronome.metronome(periodInNanos, TimeUnit.NANOSECONDS, CONFIG.timeSystemSupplier.get()),
        CONFIG.excessiveExecutorDelayHandler);
  }

  private static final Configurator CONFIG = new Configurator();
  private static volatile Controve INSTANCE = new Controve(CONFIG, null);

  public static Configurator configure() {
    return CONFIG;
  }

  private final Clock clock;
  private final Executables executables;
  private final Conductor conductor;
  private final Metronome metronome;
  private final AtomicBoolean started = new AtomicBoolean(false);
  private final LongConsumer excessiveExecutionHandler;
  private final AtomicLong conductorDelayCounter = new AtomicLong();

  private Controve(Configurator config, Controve pastInstance) {
    boolean start = false;
    if (pastInstance != null) {
      start = pastInstance.started.get();
      pastInstance.doShutdown();
      executables = pastInstance.executables;
      excessiveExecutionHandler = pastInstance.excessiveExecutionHandler;
    } else {
      executables = new Executables();
      excessiveExecutionHandler = CONFIG.excessiveExecutorDelayHandler;
    }

    clock = config.timeSystemSupplier.get();
    metronome = Metronome.metronome(CONFIG.executionPeriodInNanos, TimeUnit.NANOSECONDS, clock);
    conductor = new Conductor("Controve Conductor", executables, clock, metronome,
        monitorDelay(CONFIG.executionPeriodInNanos, TimeUnit.NANOSECONDS));
    if (pastInstance != null && start) {
      doStart();
    }
  }

  private LongConsumer monitorDelay(long executionInterval, TimeUnit unit) {
    long intervalInMs = unit.toMillis(executionInterval);
    return delayInMs -> {
      if (delayInMs > intervalInMs) {
        conductorDelayCounter.incrementAndGet();
        if (excessiveExecutionHandler != null) {
          try {
            excessiveExecutionHandler.accept(delayInMs);
          } catch (Throwable t) {
            System.out.println("Error with custom handler for execution times\n" + t);
          }
        } else {
          System.err.println(
              "Unable to execute all activities within " + intervalInMs + " milliseconds!");
        }
      }
    };
  }

  private void doRestart() {
    if (started.get()) {
      // TODO: Kill running stuff
    } else {
      try {
        // TODO: restart stuff
      } finally {
        try {
          conductor.start();
        } finally {
          started.set(true);
        }
      }
    }
  }

  private void doStart() {
    // launch any system loopers (tbd)
    if (!started.get()) {
      try {
        conductor.start();
      } finally {
        started.set(true);
      }
    }
  }

  private void doShutdown() {
    try {
      conductor.stop();
    } finally {
      started.set(false);
    }
  }
}
