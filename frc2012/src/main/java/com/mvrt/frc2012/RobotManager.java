package com.mvrt.frc2012;

import com.m3rcuriel.controve.api.Conductor;
import com.m3rcuriel.controve.api.Executables;
import com.m3rcuriel.controve.components.Clock;
import com.m3rcuriel.controve.util.Metronome;
import com.mvrt.frc2012.system.Robot;
import com.mvrt.frc2012.system.RobotBuilder;
import edu.wpi.first.wpilibj.IterativeRobot;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongConsumer;

/**
 * @author Lee Mracek
 */
public class RobotManager extends IterativeRobot {
  private static Robot robot;

  private static Conductor systemConductor;
  private static Executables executables;
  private static Metronome metronome;

  private AtomicLong executorDelayCounter = new AtomicLong(0);

  public Robot get() {
    return robot;
  }

  @Override
  public void robotInit() {
    robot = RobotBuilder.buildRobot();

    Logger logger = new Logger();

    executables = new Executables();
    executables.register(logger);
    metronome = Metronome.metronome((long) (1.0 / ((int) 1000.0 / 30.0)), TimeUnit.MILLISECONDS,
        Clock.fpgaOrSystem());
    systemConductor = new Conductor("System Conductor", executables, Clock.fpgaOrSystem(), metronome,
        monitorDelay((long) (1.0 / ((int) 1000.0 / 30.0)), TimeUnit.MILLISECONDS));

    logger.register("Throttle", () -> (short) (robot.operator.throttle.read() * 1000));
    logger.register("Wheel", () -> (short) (robot.operator.wheel.read() * 1000));

    logger.registerMotor("Drive Left Front", robot.components.leftFront);
    logger.registerMotor("Drive Left Rear", robot.components.leftRear);
    logger.registerMotor("Drive Right Front", robot.components.rightFront);
    logger.registerMotor("Drive Right Rear", robot.components.rightRear);

    logger.register("Execution Delays", () -> (int) executorDelayCounter.get());

    logger.startup();
    systemConductor.start();

  }

  private LongConsumer monitorDelay(long executionInterval, TimeUnit unit) {
    long intervalInMs = unit.toMillis(executionInterval);
    return delayInMs -> {
      if (delayInMs > intervalInMs) {
        executorDelayCounter.incrementAndGet();
      }
    };
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    double throttle = robot.operator.throttle.read();
    double wheel = robot.operator.wheel.read();

    robot.drive.austinDrive(throttle, wheel, robot.operator.quickTurn.isTriggered());
  }

  @Override
  public void disabledInit() {
    System.gc();
  }
}
