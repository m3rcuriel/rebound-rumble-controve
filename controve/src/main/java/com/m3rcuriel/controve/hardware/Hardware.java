package com.m3rcuriel.controve.hardware;



import com.m3rcuriel.controve.components.AngleSensor;
import com.m3rcuriel.controve.components.DistanceSensor;
import com.m3rcuriel.controve.components.Motor;
import com.m3rcuriel.controve.components.PneumaticsControlModule;
import com.m3rcuriel.controve.components.PowerDistributionPanel;
import com.m3rcuriel.controve.components.SimpleAccumulatedSensor;
import com.m3rcuriel.controve.components.Solenoid;
import com.m3rcuriel.controve.components.Switch;
import com.m3rcuriel.controve.components.oi.FlightStick;
import com.m3rcuriel.controve.components.oi.Gamepad;
import com.m3rcuriel.controve.components.oi.InputDevice;
import com.m3rcuriel.controve.util.Values;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;

import java.util.function.DoubleFunction;

/**
 * Contains factory methods to create implementations corresponding to physical hardware.
 * <br>
 * Subsystems, etc should not know how to obtain compoonents. Instead, the components should be
 * passed in via constructors; the references to components can be immutable and final.
 * <br>
 * This means subsystems can be tested off-robot without hardware via mock components and pass them
 * into the subsystem constructors.
 *
 * @author Lee Mracek
 */
public class Hardware {
  public static PowerDistributionPanel powerDistributionPanel() {
    edu.wpi.first.wpilibj.PowerDistributionPanel pdp =
        new edu.wpi.first.wpilibj.PowerDistributionPanel();
    return PowerDistributionPanel
        .create(pdp::getCurrent, pdp::getTotalCurrent, pdp::getVoltage, pdp::getTemperature);
  }

  public static PneumaticsControlModule pneumaticsControlModule() {
    return new HardwarePneumaticsControlModule(new Compressor());
  }

  public static PneumaticsControlModule pneumaticsControlModule(int canId) {
    return new HardwarePneumaticsControlModule(new Compressor(canId));
  }

  public static final class HumanInterfaceDevices {
    public static InputDevice driverStationJoystick(int port) {
      Joystick joystick = new Joystick(port);
      return InputDevice.create(joystick::getRawAxis, joystick::getRawButton, joystick::getPOV);
    }

    public static FlightStick logitechAttack3d(int port) {
      Joystick joystick = new Joystick(port);
      return FlightStick
          .create(joystick::getRawAxis, joystick::getRawButton, joystick::getPOV, joystick::getY,
              () -> joystick.getTwist() * -1, joystick::getX, joystick::getThrottle,
              () -> joystick.getRawButton(1), () -> joystick.getRawButton(2));
    }

    public static Gamepad generateXBox360(int port) {
      Joystick joystick = new Joystick(port);
      return Gamepad.create(joystick::getRawAxis, joystick::getRawButton, joystick::getPOV,
          () -> joystick.getRawAxis(0), () -> joystick.getRawAxis(1), () -> joystick.getRawAxis(4),
          () -> joystick.getRawAxis(5), () -> joystick.getRawAxis(2), () -> joystick.getRawAxis(3),
          () -> joystick.getRawButton(5), () -> joystick.getRawButton(6),
          () -> joystick.getRawButton(1), () -> joystick.getRawButton(2),
          () -> joystick.getRawButton(3), () -> joystick.getRawButton(4),
          () -> joystick.getRawButton(8), () -> joystick.getRawButton(7),
          () -> joystick.getRawButton(9), () -> joystick.getRawButton(10));
    }
  }


  public static final class Switches {
    public static Switch normallyClosed(int channel) {
      DigitalInput input = new DigitalInput(channel);
      return () -> !input.get();
    }

    public static Switch normallyOpen(int channel) {
      DigitalInput input = new DigitalInput(channel);
      return input::get;
    }

    public static enum AnalogOption {
      FILTERED, AVERAGED, NONE;
    }


    public static enum TriggerMode {
      IN_WINDOW, AVERAGED;
    }

    public static Switch analog(int channel, double lowerVoltage, double upperVoltage,
        AnalogOption option, TriggerMode mode) {
      if (option == null) {
        throw new IllegalArgumentException("The analog option must not be null");
      }
      if (mode == null) {
        throw new IllegalArgumentException("The analog mode must not be null");
      }
      AnalogTrigger trigger = new AnalogTrigger(channel);
      trigger.setLimitsVoltage(lowerVoltage, upperVoltage);
      switch (option) {
        case AVERAGED:
          trigger.setAveraged(true);
          break;
        case FILTERED:
          trigger.setFiltered(true);
          break;
        default:
          break;
      }
      return mode == TriggerMode.AVERAGED ? trigger::getTriggerState : trigger::getInWindow;
    }
  }


  public static final class AngleSensors {
    // TODO write Gyro component

    public static AngleSensor encoder(int aChannel, int bChannel, double distancePerPulse) {
      Encoder encoder = new Encoder(aChannel, bChannel);
      encoder.setDistancePerPulse(distancePerPulse);
      return AngleSensor.create(encoder::getDistance);
    }

    public static AngleSensor potentiometer(int channel, double fullVoltageRangeToDegrees) {
      return potentiometer(channel, fullVoltageRangeToDegrees, 0.0);
    }

    public static AngleSensor potentiometer(int channel, double fullVoltageRangeToDegrees,
        double offsetInDegrees) {
      AnalogPotentiometer pot =
          new AnalogPotentiometer(channel, fullVoltageRangeToDegrees, offsetInDegrees);
      return AngleSensor.create(pot::get);
    }
  }


  public static final class AccumulatedSensors {
    public static SimpleAccumulatedSensor encoder(int aChannel, int bChannel,
        double distancePerPulse) {
      Encoder encoder = new Encoder(aChannel, bChannel);
      encoder.setDistancePerPulse(distancePerPulse);
      return SimpleAccumulatedSensor.create(encoder::getDistance, encoder::getRate);
    }
  }


  public static final class DistanceSensors {
    public static DistanceSensor digitalUltrasonic(int pingChannel, int echoChannel) {
      Ultrasonic ultrasonic = new Ultrasonic(pingChannel, echoChannel);
      ultrasonic.setAutomaticMode(true);
      return DistanceSensor.create(ultrasonic::getRangeInches);
    }

    public static DistanceSensor analogUltrasonic(int channel, double voltsToInches) {
      AnalogInput sensor = new AnalogInput(channel);
      return DistanceSensor.create(() -> sensor.getVoltage() * voltsToInches);
    }

    public static DistanceSensor potentiometer(int channel, double fullVoltageRangeToInches) {
      return potentiometer(channel, fullVoltageRangeToInches, 0.0);
    }

    public static DistanceSensor potentiometer(int channel, double fullVoltageRangeToInches,
        double offsetInches) {
      AnalogPotentiometer pot =
          new AnalogPotentiometer(channel, fullVoltageRangeToInches, offsetInches);
      return DistanceSensor.create(pot::get);
    }
  }



  public static final class Motors {
    private static final DoubleFunction<Double> SPEED_LIMITER = Values.limiter(-1.0, 1.0);

    public static Motor talon(int channel) {
      return talon(channel, SPEED_LIMITER);
    }

    public static Motor talon(int channel, DoubleFunction<Double> speedLimiter) {
      return new HardwareMotor(new Talon(channel), SPEED_LIMITER);
    }

    public static Motor victor(int channel) {
      return victor(channel, SPEED_LIMITER);
    }

    public static Motor victor(int channel, DoubleFunction<Double> speedLimiter) {
      return new HardwareMotor(new Victor(channel), SPEED_LIMITER);
    }

    public static Motor jaguar(int channel) {
      return jaguar(channel, SPEED_LIMITER);
    }

    public static Motor jaguar(int channel, DoubleFunction<Double> speedLimiter) {
      if (speedLimiter == null) {
        throw new IllegalArgumentException("Must specificy speed limiter");
      }
      return new HardwareMotor(new Jaguar(channel), SPEED_LIMITER);
    }
  }


  public static final class Solenoids {
    public static Solenoid doubleSolenoid(int extendChannel, int retractChannel,
        Solenoid.Direction initialDirection) {
      DoubleSolenoid solenoid = new DoubleSolenoid(extendChannel, retractChannel);
      return new HardwareDoubleSolenoid(solenoid, initialDirection);
    }

    public static Solenoid doubleSolenoid(int module, int extendChannel, int retractChannel,
        Solenoid.Direction initialDirection) {
      DoubleSolenoid solenoid = new DoubleSolenoid(module, extendChannel, retractChannel);
      return new HardwareDoubleSolenoid(solenoid, initialDirection);
    }

    // TODO add Relay
  }
}

