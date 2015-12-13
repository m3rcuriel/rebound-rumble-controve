package com.m3rcuriel.controve.control.statespace;

import com.m3rcuriel.controve.util.DoubleMatrix;

/**
 * A container for the coefficients of a State Feedback Plant.
 *
 * @author Lee Mracek
 */
public final class StateFeedbackPlantCoefficients {
  private final DoubleMatrix matrixA;
  private final DoubleMatrix matrixB;
  private final DoubleMatrix matrixC;
  private final DoubleMatrix matrixD;
  private final DoubleMatrix uMin;
  private final DoubleMatrix uMax;

  /**
   * Construct a new object containing the relevant coefficients.
   *
   * Matrices are cloned to prevent pointer problems.
   *
   * @param numberOfStates the number of system states.
   * @param numberOfInputs the number of system inputs.
   * @param numberOfOutputs the number of system outputs.
   * @param matrixA the A matrix
   * @param matrixB the B matrix
   * @param matrixC the C matrix
   * @param matrixD the D matrix
   * @param uMin the minimum possible input matrix
   * @param uMax the maximum possible input matrix
   */
  public StateFeedbackPlantCoefficients(int numberOfStates, int numberOfInputs, int numberOfOutputs,
      DoubleMatrix matrixA, DoubleMatrix matrixB, DoubleMatrix matrixC, DoubleMatrix matrixD,
      DoubleMatrix uMin, DoubleMatrix uMax) {
    if (matrixA.getWidth() != numberOfStates) {
      throw new IllegalArgumentException("Width of A must be the number of states");
    }
    if (matrixA.getHeight() != numberOfStates) {
      throw new IllegalArgumentException("Height of A must be the number of states");
    }
    if (matrixB.getWidth() != numberOfInputs) {
      throw new IllegalArgumentException("Width of B must be the number of states");
    }
    if (matrixB.getHeight() != numberOfStates) {
      throw new IllegalArgumentException("Height of B must be the number of inputs");
    }
    if (matrixC.getWidth() != numberOfStates) {
      throw new IllegalArgumentException("Width of C must be the number of states");
    }
    if (matrixC.getHeight() != numberOfOutputs) {
      throw new IllegalArgumentException("Height of C must be the number of outputs");
    }
    if (matrixD.getWidth() != numberOfInputs) {
      throw new IllegalArgumentException("Width of D must be the number of inputs");
    }
    if (matrixD.getHeight() != numberOfOutputs) {
      throw new IllegalArgumentException("Height of D must be the number of outputs");
    }
    if (uMin.getHeight() != numberOfInputs) {
      throw new IllegalArgumentException("Height of uMin must be the number of inputs");
    }
    if (uMax.getHeight() != numberOfInputs) {
      throw new IllegalArgumentException("Height of uMax must be the number of inputs");
    }
    if (uMin.getWidth() != 1) {
      throw new IllegalArgumentException("Width of uMin must be 1");
    }
    if (uMax.getWidth() != 1) {
      throw new IllegalArgumentException("Width of uMax must be 1");
    }

    this.matrixA = matrixA.clone();
    this.matrixB = matrixB.clone();
    this.matrixC = matrixC.clone();
    this.matrixD = matrixD.clone();
    this.uMin = uMin.clone();
    this.uMax = uMax.clone();
  }

  /**
   * Retrieve the A matrix.
   * @return the A matrix
   */
  public DoubleMatrix getMatrixA() {
    return matrixA;
  }

  /**
   * Retrieve the B matrix.
   * @return the B matrix
   */
  public DoubleMatrix getMatrixB() {
    return matrixB;
  }

  /**
   * Retrieve the C matrix.
   * @return the C matrix
   */
  public DoubleMatrix getMatrixC() {
    return matrixC;
  }

  /**
   * Retrieve the D matrix.
   * @return the D matrix
   */
  public DoubleMatrix getMatrixD() {
    return matrixD;
  }

  /**
   * Get the minimum possible U matrix
   * @return the UMin matrix
   */
  public DoubleMatrix getUMin() {
    return uMin;
  }

  /**
   * Gets the maximum possible U matrix.
   * @return the maximum U matrix.
   */
  public DoubleMatrix getUMax() {
    return uMax;
  }
}
