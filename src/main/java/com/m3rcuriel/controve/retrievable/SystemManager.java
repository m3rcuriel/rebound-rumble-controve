package com.m3rcuriel.controve.retrievable;

import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SystemManager {
  private static SystemManager inst = null;


  private class RetrievableHolder {
    Retrievable machine;
    StateHolder stateHolder = new StateHolder();

    public RetrievableHolder(Retrievable retrievable) {
      machine = retrievable;
    }

    public StateHolder getStateHolder() {
      return stateHolder;
    }

    public void update() {
      machine.getState(stateHolder);
    }
  }


  private HashMap<String, RetrievableHolder> retrievables;

  public static SystemManager getInstance() {
    if (inst == null) {
      inst = new SystemManager();
    }
    return inst;
  }

  public SystemManager() {
    this.retrievables = new HashMap<String, RetrievableHolder>();
  }

  public void add(Retrievable retrievable) {
    RetrievableHolder retrievableHolder = new RetrievableHolder(retrievable);
    retrievables.put(retrievable.getName(), retrievableHolder);
  }

  public void add(Collection<Retrievable> values) {
    for (Retrievable retrievable : values) {
      RetrievableHolder smh = new RetrievableHolder(retrievable);
      retrievables.put(retrievable.getName(), smh);
    }
  }

  private void updateStates(String systemKey) {
    retrievables.get(systemKey).update();
  }

  private void updateAllStates() {
    retrievables.forEach((key, rh) -> rh.update());
  }

  private Object getValueForKey(String k) {
    String[] pieces = k.split(Pattern.quote("."));
    if (pieces.length != 2) {
      return null;
    }
    String base = pieces[0];
    String key = pieces[1];

    RetrievableHolder retrievableHolder = retrievables.get(base);
    if (retrievableHolder == null) {
      return null;
    }

    StateHolder sh = retrievableHolder.getStateHolder();

    if (sh == null) {
      return null;
    }

    return sh.get(key);
  }

  public JSONObject get() {
    JSONObject states = new JSONObject();

    updateAllStates();

    retrievables.forEach((key, rh) -> rh.getStateHolder()
        .forEach((entry) -> states.put(key + "." + entry.getKey(), entry.getValue())));
    return states;
  }

  public JSONObject get(String k) {
    return get(new String[] {k});
  }

  public JSONObject get(String[] args) {
    updateAllStates();
    JSONObject states = new JSONObject();
    for (String k : args) {
      states.put(k, getValueForKey(k));
    }
    return states;
  }
}
