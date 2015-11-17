package com.m3rcuriel.controve.util;

import java.util.Arrays;

public class DoubleMatrix {
	private double data[];
	private int width;
	private int height;

	public DoubleMatrix(int width, int height) {
		this.width = width;
		this.height = height;
		data = new double[width * height];
		Arrays.fill(data, 0.0);
	}

	public static DoubleMatrix zeroes(int n, int m) {
		double[][] data = new double[m][n];
		for (double[] datum : data) {
			Arrays.fill(datum, 0);
		}
		return new DoubleMatrix(data);
	}

	public static DoubleMatrix zeroes(int n) {
		return zeroes(n, n);
	}

	public static DoubleMatrix identity(int n, int m) {
		double[][] data = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				data[i][j] = j == i ? 1 : 0;
			}
		}
		return new DoubleMatrix(data);
	}

	public static DoubleMatrix identity(int n) {
		return identity(n, n);
	}

	/*
	 * {{ 0, 1, 2, 1 }, { 1, 3, 4, 0 }, { 5, 1, 6, 5 }}
	 *
	 */
	public DoubleMatrix(double[][] data) {
		this.height = data.length;
		this.width = data[0].length;
		this.data = Arrays.stream(data).flatMapToDouble(Arrays::stream).toArray();
	}

	public DoubleMatrix(int height, int width, double[] data) {
		this.height = height;
		this.width = width;
		this.data = data;
	}

	public double[] getArray() {
		return data;
	}

	public double[][] get2dArray() {
		double[][] d2data = new double[height][width];
		for (int i = 0, z = 0; i < height; i++) {
			for (int j = 0; j < width; j++, z++) {
				d2data[i][j] = data[z];
			}
		}
		return d2data;
	}

	public double get(int x, int y) {
		if (x >= width || y >= height) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return data[x + y * width];
	}

	public double get(int i) {
		return data[i];
	}

	public void set(int x, int y, double value) {
		if (x >= width || y >= height) {
			throw new ArrayIndexOutOfBoundsException();
		}
		data[x + y * width] = value;
	}

	public void set(int i, double value) {
		data[i] = value;
	}

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

	public void flash(double[] d) {
		data = d.clone();
	}

	public void flash(double[][] d) {
		data = Arrays.stream(d).flatMapToDouble(Arrays::stream).toArray().clone();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean sameSize(DoubleMatrix m) {
		return (getWidth() == m.getWidth() && getHeight() == m.getHeight());
	}

	public void plus(DoubleMatrix adder) {
		for (int i = 0; i < adder.getArray().length; i++) {
			data[i] += adder.getArray()[i];
		}
	}

	public void minus(DoubleMatrix sub) {
		for (int i = 0; i < sub.getArray().length; i++) {
			data[i] -= sub.getArray()[i];
		}
	}

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

	public static DoubleMatrix add(DoubleMatrix a, DoubleMatrix b) {
		a = a.clone();
		a.plus(b);
		return a;
	}

	public static DoubleMatrix sum(DoubleMatrix... matrices) {
		DoubleMatrix a = matrices[0].clone();
		for (DoubleMatrix matrix : Arrays.copyOfRange(matrices, 1, matrices.length)) {
			a.plus(matrix);
		}
		return a;
	}

	public static DoubleMatrix subtract(DoubleMatrix a, DoubleMatrix b) {
		a = a.clone();
		a.minus(b);
		return a;
	}

	public static DoubleMatrix multiply(DoubleMatrix a, DoubleMatrix b) {
		a = a.clone();
		a.times(b);
		return a;
	}

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

	@Override
	public DoubleMatrix clone() {
		return new DoubleMatrix(this.getHeight(), this.getWidth(), this.data.clone());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DoubleMatrix))
			return false;
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
