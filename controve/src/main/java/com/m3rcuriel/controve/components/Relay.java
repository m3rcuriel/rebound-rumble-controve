package com.m3rcuriel.controve.components;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * An abstract representation of any object which can switch between on and off.
 *
 * @author Lee Mracek
 */
public interface Relay {
  /**
   * The set of states possible in a Relay.
   */
  static enum State {
    /**
     * Relay has been told to switch on, but is not on yet.
     */
    SWITCHING_ON,
    /**
     * Relay is on.
     */
    ON,
    /**
     * Relay has been told to switch off, but is not off yet.
     */
    SWITCHING_OFF,
    /**
     * Relay is off.
     */
    OFF,
    /**
     * Relay state is unknown.
     */
    UNKNOWN;
  }

  /**
   * Retrieve the {@link State} of the Relay.
   *
   * @return the {@link State}
   */
  State state();

  /**
   * Switch the Relay on.
   *
   * @return this Relay for method chaining
   */
  Relay on();

  /**
   * Switch the Relay off.
   *
   * @return this Relay for method chaining
   */
  Relay off();

  /**
   * Check if the Relay is {@link State}{@code .ON}.
   * @return true if the Relay is on
   */
  default boolean isOn() {
    return state() == State.ON;
  }

  /**
   * Check if the Relay is {@link State}{@code .OFF}.
   * @return true if the Relay is off
   */
  default boolean isOff() {
    return state() == State.OFF;
  }

  /**
   * Check if the Relay is {@link State}{@code .SWITCHING_ON}.
   * @return true if the Relay is switching on
   */
  default boolean isSwitchingOn() {
    return state() == State.SWITCHING_ON;
  }

  /**
   * Check if the Relay is {@link State}{@code .SWITCHING_OFF}.
   * @return true if the Relay is switching off
   */
  default boolean isSwitchingOff() {
    return state() == State.SWITCHING_OFF;
  }

  /**
   * Construct a new Relay which switching instantaneously from on to off.
   *
   * @param switcher a Consumer to switch the Relay
   * @param onState the method to read the state
   * @return the constructed Relay
   */
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
