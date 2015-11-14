package com.mvrt.lib;

public interface StateMachine {
  public void getState(StateHolder states);

  public String getName();
}
