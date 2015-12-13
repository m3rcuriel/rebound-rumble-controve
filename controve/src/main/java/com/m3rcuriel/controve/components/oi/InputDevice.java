package com.m3rcuriel.controve.components.oi;

import com.m3rcuriel.controve.components.Switch;

import java.util.function.IntFunction;

/**
 * Created by lee on 12/10/15.
 */
public interface InputDevice {
  public ContinuousRange getAxis(int axis);

  public Switch getButton(int button);

  public DirectionalAxis getDPad(int pad);

  public static InputDevice create(IntFunction<Double> axisToValue,
      IntFunction<Boolean> buttonNumberToSwitch, IntFunction<Integer> padToValue) {
    return new InputDevice() {
      @Override
      public ContinuousRange getAxis(int axis) {
        return () -> axisToValue.apply(axis);
      }

      @Override
      public Switch getButton(int button) {
        return () -> buttonNumberToSwitch.apply(button);
      }

      @Override
      public DirectionalAxis getDPad(int pad) {
        return () -> padToValue.apply(pad);
      }
    };
  }
}
