package com.m3rcuriel.controve.api;

import com.m3rcuriel.controve.retrievable.Retrievable;
import com.m3rcuriel.controve.retrievable.StateHolder;

public abstract class StateControllerBase<T extends CommandsBase, K extends RobotSetpointsBase> {

  protected Routine<T, K> currentRoutine = null;
  protected K setpoints;

  private void setNewRoutine(Routine<T, K> newRoutine) {
    boolean needsCancel = newRoutine != currentRoutine && currentRoutine != null;

    boolean needsReset = newRoutine != currentRoutine && newRoutine != null;
    if (needsCancel) {
      currentRoutine.cancel();
    }
    currentRoutine = newRoutine;
    if (needsReset) {
      currentRoutine.reset();
    }
  }

  public void reset() {
    setNewRoutine(null);
  }

  public abstract void update(T commands);
}
