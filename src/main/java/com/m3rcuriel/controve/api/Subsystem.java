package com.m3rcuriel.controve.api;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @author Lee Mracek
 */
public abstract class Subsystem implements StateHolder {
  private String name;

  protected HashMap<String, Supplier> states = new HashMap<>();

  public Subsystem(String name) {
    this.name = name;
    StateManager.add(this);
  }

  public abstract void updateState(StateManager states);

  public abstract void reloadConstants();
}
