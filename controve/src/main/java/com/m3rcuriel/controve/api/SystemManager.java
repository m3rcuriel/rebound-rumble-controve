package com.m3rcuriel.controve.api;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by lee on 12/16/15.
 */
public class SystemManager {
  private static SystemManager INSTANCE = null;


  private class RetrievableHolder {
    private Retrievable retrievable;
    private StateHolder stateHolder = new StateHolder();

    public RetrievableHolder(Retrievable r) {
      this.retrievable = r;
    }

    public StateHolder getStateHolder() {
      return stateHolder;
    }

    public void update() {
      retrievable.getState(stateHolder);
    }
  }


  private HashMap<String, RetrievableHolder> retrievables;

  public static SystemManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SystemManager();
    }
    return INSTANCE;
  }

  public SystemManager() {
    this.retrievables = new HashMap<>();
  }

  public void add(Retrievable r) {
    retrievables.put(r.getName(), new RetrievableHolder(r));
  }

  public void add(Retrievable... rs) {
    for (Retrievable r : rs) {
      add(r);
    }
  }

  public void updateStates(String systemKey) {
    retrievables.get(systemKey).update();
  }

  public void updateAllStates() {
    retrievables.forEach((key, r) -> r.update());
  }

  public JSONObject getAllJson() {
    JSONObject states = new JSONObject();
    Set<String> systemKeys = this.retrievables.keySet();

    updateAllStates();

    for (String systemKey : systemKeys) {
      RetrievableHolder retrievableHolder = retrievables.get(systemKey);
      StateHolder stateHolder = retrievableHolder.getStateHolder();

      stateHolder.keySet()
          .forEach((key) -> states.put(systemKey + "." + key, stateHolder.get(key)));
    }

    return states;
  }

  public Optional<Object> getValueForKey(String key) {
    String[] pieces = key.split(Pattern.quote("."));
    if (pieces.length != 2) {
      throw new IllegalArgumentException("Key must contain 2 sections separated by a '.'\n"
          + "In the format 'systemKey'.'state'\n");
    }
    String base = pieces[0];
    String stateKey = pieces[1];

    RetrievableHolder retrievableHolder = retrievables.get(base);
    if (retrievableHolder == null) {
      return Optional.empty();
    }

    StateHolder stateHolder = retrievableHolder.getStateHolder();
    if (stateHolder == null) {
      return Optional.empty();
    }

    return Optional.of(stateHolder.get(stateKey));
  }

  public JSONObject getJson(String key) {
    return getJson(key);
  }

  public JSONObject getJson(String... keys) {
    updateAllStates();
    JSONObject states = new JSONObject();
    for (String key : keys) {
      states.put(key, getValueForKey(key).orElse(""));
    }
    return states;
  }
}
