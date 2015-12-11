package com.m3rcuriel.controve.control.statespace;

import com.m3rcuriel.controve.util.DoubleMatrix;

/**
 * General representation of a State Feedback Controller with L and K matrices.
 *
 * @author Lee Mracek
 */
public class StateFeedbackController {
  private final DoubleMatrix matrixL;
  private final DoubleMatrix matrixK;
  private final StateFeedbackPlantCoefficients coefficients;

  /**
   * Initializes a new StateFeedbackController wth the given control matrices.
   * @param numberOfStates the number of states in the state matrix
   * @param numberOfInputs the number of inputs in the input matrix
   * @param numberOfOutputs the number of outputs in the output matrix
   * @param matrixL the L matrix for the observer
   * @param matrixK the K matrix for the controller
   * @param coefficients the associated {@link StateFeedbackPlantCoefficients}
   */
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

  /**
   * Returns the L matrix of the observer.
   *
   * @return the L matrix
   */
  public DoubleMatrix getMatrixL() {
    return matrixL;
  }

  /**
   * Returns the K matrix of the controller.
   *
   * @return the K matrix
   */
  public DoubleMatrix getMatrixK() {
    return matrixK;
  }

  /**
   * Retrieve the coefficients for the corresponding controller plant.
   *
   * @return the {@link StateFeedbackPlantCoefficients} associated with the controller
   */
  public StateFeedbackPlantCoefficients coefficients() {
    return coefficients;
  }
}
