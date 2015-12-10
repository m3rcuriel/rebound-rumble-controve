package com.m3rcuriel.controve.controllers;

import com.m3rcuriel.controve.control.StateFeedbackController;
import com.m3rcuriel.controve.control.StateFeedbackPlantCoefficients;
import com.m3rcuriel.controve.util.DoubleMatrix;

public abstract class StateSpaceController extends Controller {

  protected final StateFeedbackController controller;
  protected double period;

  private DoubleMatrix xHat;

  private DoubleMatrix matrixU;
  private DoubleMatrix uUncapped;

  public StateSpaceController(int numberOfStates, int numberOfInputs, int numberOfOutputs,
      StateFeedbackPlantCoefficients gains, DoubleMatrix matrixK, DoubleMatrix matrixL,
      double period) {
    controller =
        new StateFeedbackController(numberOfStates, numberOfInputs, numberOfOutputs, matrixK,
            matrixL, gains);
    this.period = period;

    xHat = new DoubleMatrix(numberOfStates, 1);
  }

  DoubleMatrix bTu;
  DoubleMatrix lTy;
  DoubleMatrix aLcX;

  public void update(DoubleMatrix matrixR, DoubleMatrix matrixY) {
    DoubleMatrix r1 = DoubleMatrix.subtract(matrixR, xHat);
    matrixU = DoubleMatrix.multiply(getMatrixK(), r1);
    uUncapped.flash(matrixU.getArray());
    capU();

    bTu = DoubleMatrix.multiply(getMatrixB(), matrixU);
    lTy = DoubleMatrix.multiply(getMatrixL(), matrixY);
    aLcX = DoubleMatrix.multiply(
        DoubleMatrix.subtract(getMatrixA(), DoubleMatrix.multiply(getMatrixL(), getMatrixC())),
        xHat);
    // TODO: Add LDU term

    xHat = DoubleMatrix.add(xHat, DoubleMatrix.sum(bTu, lTy, aLcX));
  }

  protected DoubleMatrix getMatrixA() {
    return controller.coefficients().getMatrixA();
  }

  protected DoubleMatrix getMatrixB() {
    return controller.coefficients().getMatrixB();
  }

  protected DoubleMatrix getMatrixC() {
    return controller.coefficients().getMatrixC();
  }

  protected DoubleMatrix getMatrixD() {
    return controller.coefficients().getMatrixD();
  }

  protected DoubleMatrix getMatrixL() {
    return controller.getMatrixL();
  }

  protected DoubleMatrix getMatrixK() {
    return controller.getMatrixK();
  }

  public void capU() {
    for (int i = 0; i < matrixU.getHeight(); i++) {
      double uI = matrixU.get(i);
      double uMaxI = controller.coefficients().getUMax().get(i);
      double uMinI = controller.coefficients().getUMin().get(i);
      if (uI > uMaxI) {
        uI = uMaxI;
      } else if (uI < uMinI) {
        uI = uMinI;
      }
      matrixU.set(i, uI);
    }
  }
}
