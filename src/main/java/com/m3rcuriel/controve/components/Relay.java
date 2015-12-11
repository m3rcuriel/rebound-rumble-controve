package com.m3rcuriel.controve.components;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author Lee Mracek
 */
public interface Relay {
  static enum State {
    SWITCHING_ON,
    ON,
    SWITCHING_OFF,
    OFF,
    UNKNOWN;
  }

  State state();

  Relay on();

  Relay off();

  default boolean isOn() {
    return state() == State.ON;
  }

  default boolean isOff() {
    return state() == State.OFF;
  }

  default boolean isSwitchingOn() {
    return state() == State.SWITCHING_ON;
  }

  default boolean isSwitchingOff() {
    return state() == State.SWITCHING_OFF;
  }

  static Relay instantaneous(Consumer<Boolean> switcher, BooleanSupplier onState) {
    return new Relay() {
      @Override
      public State state() {
        return onState.getAsBoolean() ? State.ON : State.OFF;
      }

      @Override
      public Relay on() {
        switcher.accept(Boolean.TRUE);
        return this;
      }

      @Override
      public Relay off() {
        switcher.accept(Boolean.FALSE);
        return this;
      }
    };
  }
}
