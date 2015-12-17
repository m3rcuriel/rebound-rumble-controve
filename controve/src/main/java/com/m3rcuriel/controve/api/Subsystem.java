package com.m3rcuriel.controve.api;

/**
 * Created by lee on 12/16/15.
 */
public abstract class Subsystem implements Retrievable {
  protected String name;

  public Subsystem(String name) {
    this.name = name;
    SystemManager.getInstance().add(this);
  }

  public String getName() {
    return name;
  }

  public abstract void reloadConstants();
}
