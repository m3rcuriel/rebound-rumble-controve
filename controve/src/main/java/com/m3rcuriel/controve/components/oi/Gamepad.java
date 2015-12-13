package com.m3rcuriel.controve.components.oi;

import com.m3rcuriel.controve.components.Switch;

import java.util.function.IntFunction;

/**
 * Created by lee on 12/10/15.
 */
public interface Gamepad extends InputDevice {
  public abstract ContinuousRange getLeftX();

  public abstract ContinuousRange getLeftY();

  public abstract ContinuousRange getRightX();

  public abstract ContinuousRange getRightY();

  public abstract ContinuousRange getLeftTrigger();

  public abstract ContinuousRange getRightTrigger();

  public abstract Switch getLeftBumper();

  public abstract Switch getRightBumper();

  public abstract Switch getA();

  public abstract Switch getB();

  public abstract Switch getX();

  public abstract Switch getY();

  public abstract Switch getStart();

  public abstract Switch getSelect();

  public abstract Switch getLeftStick();

  public abstract Switch getRightStick();

  public static Gamepad create(IntFunction<Double> axisToValue,
      IntFunction<Boolean> buttonNumberToSwitch, IntFunction<Integer> dPad, ContinuousRange leftX,
      ContinuousRange leftY, ContinuousRange rightX, ContinuousRange rightY,
      ContinuousRange leftTrigger, ContinuousRange rightTrigger, Switch leftBumper,
      Switch rightBumper, Switch buttonA, Switch buttonB, Switch buttonX, Switch buttonY,
      Switch startButton, Switch selectButton, Switch leftStick, Switch rightStick) {
    return new Gamepad() {
      @Override
      public ContinuousRange getLeftX() {
        return leftX;
      }

      @Override
      public ContinuousRange getLeftY() {
        return leftY;
      }

      @Override
      public ContinuousRange getRightX() {
        return rightX;
      }

      @Override
      public ContinuousRange getRightY() {
        return rightY;
      }

      @Override
      public ContinuousRange getLeftTrigger() {
        return leftTrigger;
      }

      @Override
      public ContinuousRange getRightTrigger() {
        return rightTrigger;
      }

      @Override
      public Switch getLeftBumper() {
        return leftBumper;
      }

      @Override
      public Switch getRightBumper() {
        return rightBumper;
      }

      @Override
      public Switch getA() {
        return buttonA;
      }

      @Override
      public Switch getB() {
        return buttonB;
      }

      @Override
      public Switch getX() {
        return buttonX;
      }

      @Override
      public Switch getY() {
        return buttonY;
      }

      @Override
      public Switch getStart() {
        return startButton;
      }

      @Override
      public Switch getSelect() {
        return selectButton;
      }

      @Override
      public Switch getLeftStick() {
        return leftStick;
      }

      @Override
      public Switch getRightStick() {
        return rightStick;
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
        return () -> dPad.apply(pad);
      }
    };
  }
}
