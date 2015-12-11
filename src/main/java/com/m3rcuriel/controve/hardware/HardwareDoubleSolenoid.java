package com.m3rcuriel.controve.hardware;

import com.m3rcuriel.controve.components.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Wrapper for a WPILib {@link edu.wpi.first.wpilibj.DoubleSolenoid}.
 *
 * @author Lee Mracek
 * @see com.m3rcuriel.controve.components.Solenoid
 * @see Hardware
 * @see edu.wpi.first.wpilibj.DoubleSolenoid
 */
final class HardwareDoubleSolenoid implements Solenoid {
  private final DoubleSolenoid solenoid;

  private Direction direction;

  /**
   * Constructs a HardwareDoubleSolenoid around an existing WPILib {@link DoubleSolenoid}.
   *
   * @param solenoid the hardware to wrap
   * @param initialDirection the initial direction of the solenoid (will be checked anyway)
   */
  HardwareDoubleSolenoid(DoubleSolenoid solenoid, Direction initialDirection) {
    if (solenoid == null) {
      throw new IllegalArgumentException("Solenoid must not be null");
    }
    this.solenoid = solenoid;
    if (initialDirection != null) {
      this.direction = initialDirection;
    }
    checkState();
  }

  protected void checkState() {
    if (solenoid.get() == Value.kForward) {
      direction = Direction.EXTENDING;
    } else if (solenoid.get() == Value.kReverse) {
      direction = Direction.RETRACTING;
    } else {
      direction = Direction.STOPPED;
    }
  }

  /**
   * Get the direction of this solenoid.
   *
   * @return the {@link Direction} of the solenoid.
   */
  @Override
  public Direction getDirection() {
    checkState();
    return direction;
  }

  /**
   * Extend a solenoid.
   *
   * @return this solenoid for method chaining
   */
  @Override
  public HardwareDoubleSolenoid extend() {
    solenoid.set(Value.kForward);
    direction = Direction.EXTENDING;
    checkState();
    return this;
  }

  /**
   * Retract a solenoid.
   *
   * @return this solenoid for method chaining
   */
  @Override
  public HardwareDoubleSolenoid retract() {
    solenoid.set(Value.kReverse);
    direction = Direction.RETRACTING;
    checkState();
    return this;
  }

  @Override
  public String toString() {
    return "direction = " + direction;
  }
}
