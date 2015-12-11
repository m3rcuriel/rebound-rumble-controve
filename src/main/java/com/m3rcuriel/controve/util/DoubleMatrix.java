package com.m3rcuriel.controve.util;

import java.util.Arrays;

/**
 * A relatively simple implementation of the Linear Algebra matrix using a 1D array, and only
 * doubles.
 *
 * @author Lee Mracek
 */
public class DoubleMatrix {
  private double[] data;
  private int width;
  private int height;

  /**
   * Construct a DoubleMatrix instance of the given size.
   *
   * @param width  the width of the new matrix
   * @param height the height of the new matrix
   */
  public DoubleMatrix(int width, int height) {
    this.width = width;
    this.height = height;
    data = new double[width * height];
    Arrays.fill(data, 0.0);
  }

  /**
   * Construct a new DoubleMatrix of the given size, initialized to 0.
   *
   * @param width  the width of the matrix
   * @param height the height of the matrix
   * @return the new DoubleMatrix
   */
  public static DoubleMatrix zeroes(int width, int height) {
    double[][] data = new double[height][width];
    for (double[] datum : data) {
      Arrays.fill(datum, 0);
    }
    return new DoubleMatrix(data);
  }

  /**
   * Construct a square DoubleMatrix of 0.
   *
   * @param dimension the dimensions of the matrix
   * @return the created DoubleMatrix
   */
  public static DoubleMatrix zeroes(int dimension) {
    return zeroes(dimension, dimension);
  }

  /**
   * Construct and identity matrix with a given height and width.
   * <p>
   * For example, creating a 3x4 matrix would give: 1 0 0 0 1 0 0 0 1 0 0 0
   *
   * @param width  the width of the matrix
   * @param height the height of the matrix
   * @return the newly created identity matrix
   */
  public static DoubleMatrix identity(int width, int height) {
    double[][] data = new double[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        data[i][j] = j == i ? 1 : 0;
      }
    }
    return new DoubleMatrix(data);
  }

  /**
   * Construct a square identity matrix with the given dimension.
   *
   * @param dimensions the dimensions of the matrix
   * @return the new square identity matrix
   */
  public static DoubleMatrix identity(int dimensions) {
    return identity(dimensions, dimensions);
  }

  /**
   * Construct a new DoubleMatrix from a 2D array. For example: {{ 0, 1, 2, 1 }, { 1, 3, 4, 0 }, {
   * 5, 1, 6, 5 }}
   * <p>
   * would get
   * <p>
   * 0 1 2 1 1 3 4 0 5 1 6 5
   *
   * @param data the 2D array containing the matrix data
   */
  public DoubleMatrix(double[][] data) {
    this.height = data.length;
    this.width = data[0].length;
    this.data = Arrays.stream(data).flatMapToDouble(Arrays::stream).toArray();
  }

  /**
   * Construct a new DoubleMatrix with the given single array data.
   *
   * @param height the height of the matrix
   * @param width  the width of the matrix
   * @param data   the data for the matrix
   */
  public DoubleMatrix(int height, int width, double[] data) {
    if (height * width != data.length) {
      throw new IllegalArgumentException("Data must match the size of the matrix");
    }
    this.height = height;
    this.width = width;
    this.data = data;
  }

  /**
   * Get the internal 1D array representation of the matrix.
   *
   * @return the 1D array representation
   */
  public double[] getArray() {
    return data;
  }

  /**
   * Get a 2D represenation of the matrix.
   *
   * @return a constructed 2D array representing the matrix
   */
  public double[][] get2dArray() {
    double[][] d2data = new double[height][width];
    for (int i = 0, z = 0; i < height; i++) {
      for (int j = 0; j < width; j++, z++) {
        d2data[i][j] = data[z];
      }
    }
    return d2data;
  }

  /**
   * Get a value from the matrix based on x and y position of the value.
   *
   * @param x the x coordinate of the value
   * @param y the y coordinate of the value
   * @return the value at the given coordinate pair
   */
  public double get(int x, int y) {
    if (x >= width || y >= height) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return data[x + y * width];
  }

  /**
   * Get a value from the matrix based on its internal data position.
   *
   * @param i the index at which the value is located
   * @return the value at the given index
   */
  public double get(int i) {
    return data[i];
  }

  /**
   * Set a value in the matrix based on the x and y position of that value.
   *
   * @param x     the x position of the value
   * @param y     the y position of the value
   * @param value the new value for the position
   */
  public void set(int x, int y, double value) {
    if (x >= width || y >= height) {
      throw new ArrayIndexOutOfBoundsException();
    }
    data[x + y * width] = value;
  }

  /**
   * Set a value in the matrix based on its internal data position.
   *
   * @param i     the index at which the value is located
   * @param value the value to set the position to
   */
  public void set(int i, double value) {
    data[i] = value;
  }

  /**
   * Retrieve a row of the matrix as a 1D array of doubles.
   *
   * @param y the location of the row
   * @return the 1D array representation of the row
   */
  public double[] getRow(int y) {
    if (y >= height) {
      throw new ArrayIndexOutOfBoundsException();
    }
    double[] row = new double[width];
    for (int i = 0; i < width; i++) {
      row[i] = this.get(i, y);
    }
    return row;
  }

  /**
   * Retrieve a column of the matrix as a 1D array of doubles.
   *
   * @param x the location of the column
   * @return the 1D array representation of the column
   */
  public double[] getColumn(int x) {
    if (x >= width) {
      throw new ArrayIndexOutOfBoundsException();
    }
    double[] column = new double[height];
    for (int i = 0; i < height; i++) {
      column[i] = get(x, i);
    }

    return column;
  }

  /**
   * Set the data of the array to a new 1D matrix.
   * <p>
   * Clones the array to avoid reference errors.
   *
   * @param data the new data for the matrix
   */
  public void flash(double[] data) {
    this.data = data.clone();
  }

  /**
   * Sets the data of the array to a new 2D matrix.
   * <p>
   * Clones the array to avoid reference errors.
   *
   * @param data the new data for the matrix
   */
  public void flash(double[][] data) {
    this.data = Arrays.stream(data).flatMapToDouble(Arrays::stream).toArray().clone();
  }

  /**
   * Get the width of this matrix.
   *
   * @return the width of the matrix
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of this matrix.
   *
   * @return the height of the matrix
   */
  public int getHeight() {
    return height;
  }

  /**
   * check if this matrix is the same size as another.
   *
   * @param matrix the other matrix
   * @return true if the matrices are the same size
   */
  public boolean sameSize(DoubleMatrix matrix) {
    return (getWidth() == matrix.getWidth() && getHeight() == matrix.getHeight());
  }

  /**
   * mutably add another matrix to this matrix.
   *
   * @param adder the matrix to be added
   */
  @Deprecated
  public void plus(DoubleMatrix adder) {
    for (int i = 0; i < adder.getArray().length; i++) {
      data[i] += adder.getArray()[i];
    }
  }

  /**
   * mutably subtract another matrix from this matrix.
   *
   * @param sub the matrix to be subtracted
   */
  @Deprecated
  public void minus(DoubleMatrix sub) {
    for (int i = 0; i < sub.getArray().length; i++) {
      data[i] -= sub.getArray()[i];
    }
  }

  /**
   * mutably multiply this matrix by another matrix.
   *
   * @param mult the matrix to multiply with
   */
  public void times(DoubleMatrix mult) {
    if (getWidth() != mult.getHeight()) {
      throw new IllegalArgumentException("Matrix A's width not equal to Matix B's height");
    }

    int destHeight = getHeight();
    int destWidth = mult.getWidth();

    double[] update = new double[destHeight * destWidth];

    int pMax = getWidth();
    int width1 = getWidth();
    int width2 = mult.getWidth();
    for (int i = 0; i < destWidth; i++) {
      for (int j = 0; j < destHeight; j++) {
        double tmp = 0.0;
        for (int p = 0; p < pMax; p++) {
          tmp += mult.data[i + width2 * p] * data[p + width1 * j];
        }
        update[i + destWidth * j] = tmp;
      }
    }
    this.height = destHeight;
    this.width = destWidth;
    data = update;
  }

  /**
   * Add two matrices together.
   *
   * @param a the first matrix
   * @param b the second matrix
   * @return the resultant matrix
   */
  public static DoubleMatrix add(DoubleMatrix a, DoubleMatrix b) {
    a = a.clone();
    a.plus(b);
    return a;
  }

  /**
   * Add any number of matrices together.
   *
   * @param matrices all the matrices to add together
   * @return the resultant matrix
   */
  public static DoubleMatrix sum(DoubleMatrix... matrices) {
    DoubleMatrix a = matrices[0].clone();
    for (DoubleMatrix matrix : Arrays.copyOfRange(matrices, 1, matrices.length)) {
      a.plus(matrix);
    }
    return a;
  }

  /**
   * Subtract one matrix from another.
   *
   * @param a the initial matrix
   * @param b the matrix to be subtracted
   * @return the resultant matrix
   */
  public static DoubleMatrix subtract(DoubleMatrix a, DoubleMatrix b) {
    a = a.clone();
    a.minus(b);
    return a;
  }

  /**
   * Multiply two matrices together.
   * <p>
   * Note that the width of the first matrix must equal the height of the second
   *
   * @param a the first matrix
   * @param b the second matrix
   * @return the resultant matrix
   */
  public static DoubleMatrix multiply(DoubleMatrix a, DoubleMatrix b) {
    a = a.clone();
    a.times(b);
    return a;
  }

  /**
   * Get a string representation of the matrix.
   *
   * @return the string representation of the matrix
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        sb.append(get(j, i));
        if (j != width - 1) {
          sb.append('\t');
        }
      }

      if (i != height - 1) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  /**
   * Safely copy a matrix without the possibility of shared pointers.
   *
   * @return a copy of the original matrix
   */
  @Override
  public DoubleMatrix clone() {
    return new DoubleMatrix(this.getHeight(), this.getWidth(), this.data.clone());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DoubleMatrix)) {
      return false;
    }
    DoubleMatrix m = (DoubleMatrix) o;

    if (!sameSize(m)) {
      return false;
    }

    if (Arrays.equals(data, m.getArray())) {
      return true;
    }
    return false;
  }
}
