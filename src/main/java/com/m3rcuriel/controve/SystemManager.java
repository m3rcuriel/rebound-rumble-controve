package com.m3rcuriel.controve;

import com.m3rcuriel.controve.api.Retrievable;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * The manager for the set of all states currently on the robot. Essentially acts as a giant log
 * collector. At each point in time, it knows all the relevant values for each subsystem as well as
 * any other object submitted to the SystemManager instance which inherits {@link Retrievable}.
 *
 * @author Lee Mracek
 */
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

  /**
   * Get the current instance of the SystemManager, as there can only be one instance.
   *
   * @return the current instance
   */
  public static SystemManager getInstance() {
    if (inst == null) {
      inst = new SystemManager();
    }
    return inst;
  }

  public SystemManager() {
    this.retrievables = new HashMap<String, RetrievableHolder>();
  }

  /**
   * Hook a {@link Retrievable} into the SystemManager.
   *
   * @param retrievable the object to be monitored by the SystemManager
   */
  public void add(Retrievable retrievable) {
    RetrievableHolder retrievableHolder = new RetrievableHolder(retrievable);
    retrievables.put(retrievable.getName(), retrievableHolder);
  }

  /**
   * Hook a collection of {@link Retrievable}s into the SystemManager.
   *
   * @param values the retrievables to be added
   */
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

  /**
   * Retrieve all states from the robot and dump them into a {@link JSONObject} for easy logging or
   * viewing.
   *
   * @return the {@link JSONObject} representing all states
   */
  public JSONObject get() {
    JSONObject states = new JSONObject();

    updateAllStates();

    retrievables.forEach((key, rh) -> rh.getStateHolder()
        .forEach((entry) -> states.put(key + "." + entry.getKey(), entry.getValue())));
    return states;
  }

  /**
   * Retrieve a single Retrievable's states.
   *
   * @param k the key for the retrievable
   * @return the {@link JSONObject} containing the retrievable's states
   */
  public JSONObject get(String k) {
    return get(new String[] {k});
  }

  /**
   * Retrieve the states for an array of Retrievable.
   *
   * @param args the names of the retrievables to retrieve
   * @return the {@link JSONObject} representation of the states
   */
  public JSONObject get(String[] args) {
    updateAllStates();
    JSONObject states = new JSONObject();
    for (String k : args) {
      states.put(k, getValueForKey(k));
    }
    return states;
  }
}
