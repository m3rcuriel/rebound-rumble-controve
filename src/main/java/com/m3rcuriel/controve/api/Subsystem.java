package com.m3rcuriel.controve.api;

import com.m3rcuriel.controve.Executable;
import com.m3rcuriel.controve.SystemManager;

public abstract class Subsystem implements Executable, Retrievable {

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
