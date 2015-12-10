package com.m3rcuriel.controve.control;

import com.m3rcuriel.controve.util.DoubleMatrix;

public class StateFeedbackController {
  private final DoubleMatrix matrixL;
  private final DoubleMatrix matrixK;
  private final StateFeedbackPlantCoefficients coefficients;

  public StateFeedbackController(int numberOfStates, int numberOfInputs, int numberOfOutputs,
      DoubleMatrix matrixL, DoubleMatrix matrixK, StateFeedbackPlantCoefficients coefficients) {
    if (matrixL.getWidth() != numberOfOutputs) {
      throw new IllegalArgumentException("The width of L must be the number of outputs");
    }
    if (matrixL.getHeight() != numberOfStates) {
      throw new IllegalArgumentException("The height of L must be the number of states");
    }
    if (matrixK.getWidth() != numberOfStates) {
      throw new IllegalArgumentException("The width of K must be the number of states");
    }
    if (matrixK.getHeight() != numberOfInputs) {
      throw new IllegalArgumentException("The height of K must be the number of inputs");
    }

    this.matrixL = matrixL;
    this.matrixK = matrixK;
    this.coefficients = coefficients;
  }

  public DoubleMatrix getMatrixL() {
    return matrixL;
  }

  public DoubleMatrix getMatrixK() {
    return matrixK;
  }

  public StateFeedbackPlantCoefficients coefficients() {
    return coefficients;
  }
}
