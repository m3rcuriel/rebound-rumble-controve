package com.m3rcuriel.controve.api;

/**
 * Created by lee on 12/16/15.
 */
public interface Retrievable {
  void getState(StateHolder state);

  String getName();
}
