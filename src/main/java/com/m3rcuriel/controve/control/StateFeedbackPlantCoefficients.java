package com.m3rcuriel.controve.control;

import com.m3rcuriel.controve.util.DoubleMatrix;

public final class StateFeedbackPlantCoefficients {
  private final DoubleMatrix matrixA;
  private final DoubleMatrix matrixB;
  private final DoubleMatrix matrixC;
  private final DoubleMatrix matrixD;
  private final DoubleMatrix uMin;
  private final DoubleMatrix uMax;

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

  public DoubleMatrix getMatrixA() {
    return matrixA;
  }

  public DoubleMatrix getMatrixB() {
    return matrixB;
  }

  public DoubleMatrix getMatrixC() {
    return matrixC;
  }

  public DoubleMatrix getMatrixD() {
    return matrixD;
  }

  public DoubleMatrix getUMin() {
    return uMin;
  }

  public DoubleMatrix getUMax() {
    return uMax;
  }
}
