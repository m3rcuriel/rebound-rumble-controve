package com.m3rcuriel.controve.control.statespace;

import com.m3rcuriel.controve.util.DoubleMatrix;

/**
 * A representation using {@link DoubleMatrix} of the plant in State Feedback. This class should
 * be used mainly in simulation to check a plants reponse to a controller.
 *
 * @author Lee Mracek
 */
public class StateFeedbackPlant {
  private StateFeedbackPlantCoefficients coefficients;
  private DoubleMatrix matrixX;
  private DoubleMatrix matrixY;
  private DoubleMatrix matrixU;

  /**
   * Construct a new plant with the given {@link StateFeedbackPlantCoefficients}.
   * @param coefficients
   */
  public StateFeedbackPlant(StateFeedbackPlantCoefficients coefficients) {
    this.coefficients = coefficients;
    reset();
  }

  /**
   * Retrieve the A matrix.
   * @return the A matrix
   */
  public DoubleMatrix getMatrixA() {
    return coefficients.getMatrixA();
  }

  /**
   * Retrieve the B matrix.
   *
   * @return the B matrix
   */
  public DoubleMatrix getMatrixB() {
    return coefficients.getMatrixB();
  }

  /**
   * Retrieve the C matrix.
   *
   * @return the C matrix
   */
  public DoubleMatrix getMatrixC() {
    return coefficients.getMatrixC();
  }

  /**
   * Retrieve the D matrix.
   *
   * @return the D matrix
   */
  public DoubleMatrix getMatrixD() {
    return coefficients.getMatrixD();
  }

  /**
   * Gets the minimum possible input matrix.
   *
   * @return the minimum possible input matrix
   */
  public DoubleMatrix getUMin() {
    return coefficients.getUMin();
  }

  /**
   * Gets the maximum possible input matrix.
   *
   * @return the maximum possible input matrix
   */
  public DoubleMatrix getUMax() {
    return coefficients.getUMax();
  }

  /**
   * Returns the current state matrix.
   *
   * @return the X matrix
   */
  public DoubleMatrix getMatrixX() {
    return matrixX;
  }

  /**
   * Returns the current output matrix.
   *
   * @return the Y matrix
   */
  public DoubleMatrix getMatrixY() {
    return matrixY;
  }

  /**
   * Returns the current input matrix.
   *
   * @return the U matrix
   */
  public DoubleMatrix getMatrixU() {
    return matrixU;
  }

  /**
   * Retrieve the coefficients of the plant.
   *
   * @return the {@link StateFeedbackPlantCoefficients}
   */
  public StateFeedbackPlantCoefficients coefficients() {
    return coefficients;
  }

  /**
   * Reset the plant to 0 matrices.
   *
   * Zeroes the X, Y, and U matrices.
   */
  public void reset() {
    matrixX = DoubleMatrix.zeroes(matrixX.getWidth(), matrixX.getHeight());
    matrixY = DoubleMatrix.zeroes(matrixY.getWidth(), matrixY.getHeight());
    matrixU = DoubleMatrix.zeroes(matrixU.getWidth(), matrixU.getHeight());
  }

  /**
   * Sanitize U values.
   */
  public void checkU() {
    // should be sanitized by controller
    // use absolute max/min here and polytote/etc in controller
    for (int i = 0; i < getUMin().getHeight(); i++) {
      if (getMatrixU().get(i, 0) <= getUMax().get(i, 0) + 0.00001
          || getMatrixU().get(i, 0) >= getUMin().get(i, 0) + 0.00001) {
        throw new AssertionError("U should fit in real-world constraints");
      }
    }
  }

  /**
   * Update the plant for a given input matrix. Transforms the X and Y matrices.
   *
   * @param matrixU the input matrix
   */
  public void update(DoubleMatrix matrixU) {
    this.matrixU = matrixU;
    checkU();
    matrixX = DoubleMatrix.add(DoubleMatrix.multiply(getMatrixA(), matrixX),
        DoubleMatrix.multiply(getMatrixB(), this.matrixU));
    matrixY = DoubleMatrix.add(DoubleMatrix.multiply(getMatrixC(), matrixX),
        DoubleMatrix.multiply(getMatrixD(), this.matrixU));
  }
}
