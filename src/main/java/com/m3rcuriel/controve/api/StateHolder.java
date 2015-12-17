package com.m3rcuriel.controve.api;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by lee on 12/16/15.
 */
public class StateHolder {
  private HashMap<String, Object> states = new HashMap<>();

  public void put(String key, Object value) {
    states.put(key, value);
  }

  public Object get(String key) {
    return states.get(key);
  }

  public Set<String> keySet() {
    return states.keySet();
  }
}
