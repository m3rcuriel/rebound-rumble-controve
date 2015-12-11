package com.m3rcuriel.controve.components.oi;

import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

/**
 * @author Lee Mracek
 */
public interface ContinuousRange {
  public double read();

  public default ContinuousRange invert() {
    return () -> this.read() * -1.0;
  }

  public default ContinuousRange scale(double scale) {
    return () -> this.read() * scale;
  }

  public default ContinuousRange scale(DoubleSupplier scale) {
    return () -> this.read() * scale.getAsDouble();
  }

  public default ContinuousRange map(DoubleFunction<Double> mapper) {
    return () -> mapper.apply(this.read());
  }

  public default IntSupplier scaleAsInt(double scale) {
    return () -> (int) (this.read() * scale);
  }
}
