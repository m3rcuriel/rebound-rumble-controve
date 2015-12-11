package com.m3rcuriel.controve.components;

import org.junit.Before;

import java.util.function.DoubleSupplier;

import static org.junit.Assert.assertEquals;

/**
 * Created by lee on 12/10/15.
 */
abstract class AbstractDoubleValueTest {
  private static double value;

  protected static double getValue() {
    return value;
  }

  protected static void setValue(double value) {
    AbstractDoubleValueTest.value = value;
  }

  protected static void assertValue(double value, DoubleSupplier getter, double result) {
    setValue(value);
    assertEquals(getter.getAsDouble(), result, 0.0001);
  }

  @Before
  public void beforeEach() {
    value = 0;
  }
}
