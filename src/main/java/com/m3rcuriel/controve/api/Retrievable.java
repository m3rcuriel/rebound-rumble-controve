package com.m3rcuriel.controve.api;

import com.m3rcuriel.controve.StateHolder;

public interface Retrievable {
  public void getState(StateHolder states);

  public String getName();
}
