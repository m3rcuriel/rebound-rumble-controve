package com.m3rcuriel.controve.components.oi;

import com.m3rcuriel.controve.components.Switch;

import java.util.function.IntFunction;

/**
 * Created by lee on 12/10/15.
 */
public interface FlightStick extends InputDevice {
  public ContinuousRange getPitch();

  public ContinuousRange getYaw();

  public ContinuousRange getRoll();

  public ContinuousRange getThrottle();

  public Switch getTrigger();

  public Switch getThumb();

  public static FlightStick create(IntFunction<Double> axisToValue,
      IntFunction<Boolean> buttonNumberToSwitch, IntFunction<Integer> padToValue,
      ContinuousRange pitch, ContinuousRange yaw, ContinuousRange roll, ContinuousRange throttle,
      Switch trigger, Switch thumb) {
    return new FlightStick() {
      @Override
      public ContinuousRange getPitch() {
        return pitch;
      }

      @Override
      public ContinuousRange getYaw() {
        return yaw;
      }

      @Override
      public ContinuousRange getRoll() {
        return roll;
      }

      @Override
      public ContinuousRange getThrottle() {
        return throttle;
      }

      @Override
      public Switch getTrigger() {
        return trigger;
      }

      @Override
      public Switch getThumb() {
        return thumb;
      }

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
