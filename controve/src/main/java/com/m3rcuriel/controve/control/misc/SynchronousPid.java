package com.m3rcuriel.controve.control.misc;

import edu.wpi.first.wpilibj.util.BoundaryException;

// TODO add Feedforward
/**
 * Class implements a PID Control Loop.
 * <br>
 * <br>
 * Does all computation synchronously (i.e. the calculate() function must be called by the user from
 * his own thread)
 */
public class SynchronousPid {
  private double kP; // factor for "proportional" control
  private double kI; // factor for "integral" control
  private double kD; // factor for "derivative" control
  private double maximumOutput = 1.0; // |maximum output|
  private double minimumOutput = -1.0; // |minimum output|
  private double maximumInput = 0.0; // maximum input - limit setpoint to this
  private double minimumInput = 0.0; // minimum input - limit setpoint to this
  private boolean continuous = false; // do the endpoints wrap around? eg.
  // Absolute encoder
  private double prevError = 0.0; // the prior sensor input (used to compute
  // velocity)
  private double totalError = 0.0; // the sum of the errors for use in the
  // integral calc
  private double setpoint = 0.0;
  private double error = 0.0;
  private double result = 0.0;
  private double previousInput = Double.NaN;

  public SynchronousPid() {
  }

  /**
   * Allocate a PID object with the given constants for P, I, D.
   *
   * @param kP the proportional coefficient
   * @param kI the integral coefficient
   * @param kD the derivative coefficient
   */
  public SynchronousPid(double kP, double kI, double kD) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
  }

  /**
   * Read the input, calculate the output accordingly, and write to the output. This should be
   * called at a constant rate by the user (ex. in a timed thread).
   *
   * @param input the input
   * @return the calculated output
   */
  public double calculate(double input) {
    previousInput = input;
    error = setpoint - input;
    if (continuous) {
      if (Math.abs(error) > (maximumInput - minimumInput) / 2) {
        if (error > 0) {
          error -= maximumInput + minimumInput;
        } else {
          error += maximumInput - minimumInput;
        }
      }
    }

    if ((error * kP < maximumOutput) && (error * kP > minimumOutput)) {
      totalError += error;
    } else {
      totalError = 0;
    }

    result = (kP * error + kI * totalError + kD * (error - prevError));
    prevError = error;

    if (result > maximumOutput) {
      result = maximumOutput;
    } else if (result < minimumOutput) {
      result = minimumOutput;
    }
    return result;
  }

  /**
   * Set the PID controller gain parameters. Set the proportional, integral, and differential
   * coefficients.
   *
   * @param p Proportional coefficient
   * @param i Integral coefficient
   * @param d Differential coefficient
   */
  public void setPid(double p, double i, double d) {
    this.kP = p;
    this.kI = i;
    this.kD = d;
  }

  /**
   * Get the Proportional coefficient.
   *
   * @return proportional coefficient
   */
  public double getP() {
    return kP;
  }

  /**
   * Get the Integral coefficient.
   *
   * @return integral coefficient
   */
  public double getI() {
    return kI;
  }

  /**
   * Get the Differential coefficient.
   *
   * @return differential coefficient
   */
  public double getD() {
    return kD;
  }

  /**
   * Return the current PID result This is always centered on zero and constrained the the max and
   * min outs.
   *
   * @return the latest calculated output
   */
  public double get() {
    return result;
  }

  /**
   * Set the PID controller to consider the input to be continuous, Rather then using the max and
   * min in as constraints, it considers them to be the same point and automatically calculates the
   * shortest route to the setpoint.
   *
   * @param continuous Set to true turns on continuous, false turns off continuous
   */
  public void setContinuous(boolean continuous) {
    this.continuous = continuous;
  }

  /**
   * Set the PID controller to consider the input to be continuous, Rather then using the max and
   * min in as constraints, it considers them to be the same point and automatically calculates the
   * shortest route to the setpoint.
   */
  public void setContinuous() {
    this.setContinuous(true);
  }

  /**
   * Sets the maximum and minimum values expected from the input.
   *
   * @param minimumInput the minimum value expected from the input
   * @param maximumInput the maximum value expected from the output
   */
  public void setInputRange(double minimumInput, double maximumInput) {
    if (minimumInput > maximumInput) {
      throw new BoundaryException("Lower bound is greater than upper bound");
    }
    this.minimumInput = minimumInput;
    this.maximumInput = maximumInput;
    setSetpoint(setpoint);
  }

  /**
   * Sets the minimum and maximum values to write.
   *
   * @param minimumOutput the minimum value to write to the output
   * @param maximumOutput the maximum value to write to the output
   */
  public void setOutputRange(double minimumOutput, double maximumOutput) {
    if (minimumOutput > maximumOutput) {
      throw new BoundaryException("Lower bound is greater than upper bound");
    }
    this.minimumOutput = minimumOutput;
    this.maximumOutput = maximumOutput;
  }

  /**
   * Set the setpoint for the PID controller.
   *
   * @param setpoint the desired setpoint
   */
  public void setSetpoint(double setpoint) {
    if (maximumInput > minimumInput) {
      if (setpoint > maximumInput) {
        this.setpoint = maximumInput;
      } else if (setpoint < minimumInput) {
        this.setpoint = minimumInput;
      } else {
        this.setpoint = setpoint;
      }
    } else {
      this.setpoint = setpoint;
    }
  }

  /**
   * Returns the current setpoint of the PID controller.
   *
   * @return the current setpoint
   */
  public double getSetpoint() {
    return setpoint;
  }

  /**
   * Returns the current difference of the input from the setpoint.
   *
   * @return the current error
   */
  public double getError() {
    return error;
  }

  /**
   * Return true if the error is within the tolerance.
   *
   * @param tolerance check if on target within a tolerance
   * @return true if the error is less than the tolerance
   */
  public boolean onTarget(double tolerance) {
    return previousInput != Double.NaN && Math.abs(previousInput - setpoint) < tolerance;
  }

  /**
   * Reset all internal terms.
   */
  public void reset() {
    previousInput = Double.NaN;
    prevError = 0;
    totalError = 0;
    result = 0;
    setpoint = 0;
  }

  public void resetIntegrator() {
    totalError = 0;
  }

  public String getState() {
    String lState = "";

    lState += "Proportional Constant: " + kP + "\n";
    lState += "Integral Constant: " + kI + "\n";
    lState += "Derivative Constant: " + kD + "\n";

    return lState;
  }

  public String getType() {
    return "PIDController";
  }
}
