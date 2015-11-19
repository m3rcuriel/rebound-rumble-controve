package com.m3rcuriel.controve.api;

import com.m3rcuriel.controve.retrievable.Retrievable;
import com.m3rcuriel.controve.retrievable.SystemManager;

public abstract class Subsystem implements Retrievable {

  protected String name;

  public Subsystem(String name) {
    this.name = name;
    SystemManager.getInstance().add(this);
  }

  @Override
  public String getName() {
    return name;
  }

  public abstract void reloadConstants();
}
