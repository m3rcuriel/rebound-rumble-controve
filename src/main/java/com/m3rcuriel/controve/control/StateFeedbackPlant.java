package com.m3rcuriel.controve.control;

import com.m3rcuriel.controve.util.DoubleMatrix;

public class StateFeedbackPlant {
  private StateFeedbackPlantCoefficients coefficients;
  private DoubleMatrix matrixX;
  private DoubleMatrix matrixY;
  private DoubleMatrix matrixU;

  public StateFeedbackPlant(StateFeedbackPlantCoefficients coefficients) {
    this.coefficients = coefficients;
    reset();
  }

  public DoubleMatrix getMatrixA() {
    return coefficients.getMatrixA();
  }

  public DoubleMatrix getMatrixB() {
    return coefficients.getMatrixB();
  }

  public DoubleMatrix getMatrixC() {
    return coefficients.getMatrixC();
  }

  public DoubleMatrix getMatrixD() {
    return coefficients.getMatrixD();
  }

  public DoubleMatrix getUMin() {
    return coefficients.getUMin();
  }

  public DoubleMatrix getUMax() {
    return coefficients.getUMax();
  }

  public DoubleMatrix getMatrixX() {
    return matrixX;
  }

  public DoubleMatrix getMatrixY() {
    return matrixY;
  }

  public DoubleMatrix getMatrixU() {
    return matrixU;
  }

  public StateFeedbackPlantCoefficients coefficients() {
    return coefficients;
  }

  public void reset() {
    matrixX = DoubleMatrix.zeroes(matrixX.getWidth(), matrixX.getHeight());
    matrixY = DoubleMatrix.zeroes(matrixY.getWidth(), matrixY.getHeight());
    matrixU = DoubleMatrix.zeroes(matrixU.getWidth(), matrixU.getHeight());
  }

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

  public void update(DoubleMatrix matrixU) {
    this.matrixU = matrixU;
    checkU();
    matrixX = DoubleMatrix.add(DoubleMatrix.multiply(getMatrixA(), matrixX),
        DoubleMatrix.multiply(getMatrixB(), this.matrixU));
    matrixY = DoubleMatrix.add(DoubleMatrix.multiply(getMatrixC(), matrixX),
        DoubleMatrix.multiply(getMatrixD(), this.matrixU));
  }
}
