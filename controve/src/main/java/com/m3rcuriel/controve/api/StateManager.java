package com.m3rcuriel.controve.api;

import com.m3rcuriel.controve.Executable;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Lee Mracek
 */
public class StateManager implements Executable {

  private static StateManager INSTANCE = new StateManager(null);

  private HashMap<String, StateHolder> states = new HashMap<>();

  public static void initialize() {
    INSTANCE = new StateManager(INSTANCE);
  }

  public static StateManager instance() {
    return INSTANCE;
  }

  StateManager(StateManager pastInstance) {
    if (pastInstance != null) {
      // TODO load past instance
    } else {
      // TODO create new instance
    }
  }

  public static void add(StateHolder object) {
    INSTANCE.doAdd(object);
  }

  private void doAdd(StateHolder object) {
    states.put(object.getName(), object);
  }

  public StateHolder get(String name) {
    return INSTANCE.doGet(name);
  }

  private StateHolder doGet(String name) {
    return states.get(name);
  }

  public JSONObject doGetAll() {
    JSONObject states = new JSONObject();
    Collection<String> keys = states.keySet();

    for (String key : keys) {
      StateHolder holder = this.states.get(key);
      Collection<String> stateHolderKeys = holder.keySet();
      for (String stateKey : stateHolderKeys) {
        states.put(key + "." + stateKey, holder.get(stateKey).get());
      }
    }
    return states;
  }

  @Override
  public void execute(long timeInMillis) {
    states.forEach((s, v) -> {
      if(v instanceof Subsystem) {
        ((Subsystem) v).updateState(INSTANCE);
      }
    });
  }
}
