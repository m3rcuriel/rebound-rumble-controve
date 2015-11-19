package com.m3rcuriel.controve.controllers;

public abstract class Controller {
  protected boolean enabled = false;

  public abstract void reset();

  public abstract boolean isOnTarget();
}
