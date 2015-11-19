package com.m3rcuriel.controve.api;

public abstract class Routine<T extends CommandsBase, K extends RobotSetpointsBase> {
  public abstract void reset();

  public abstract K update(T command, K existing);

  public abstract void cancel();

  public abstract boolean isFinished();

  public abstract String getName();
}
